from hypothesis import given, settings, HealthCheck
from hypothesis_jsonschema import from_schema
import hypothesis
import argparse
import os
import json 
import time
import math
import multiprocessing
from jsonschema import validate, Draft4Validator, Draft6Validator
from datetime import timedelta

import warnings
warnings.simplefilter(action='ignore', category=FutureWarning)

def determine_schema_version(schema, draft4=False, draft6=False):
    is_valid_draft4 = False
    is_valid_draft6 = False
    
    if draft6:
        try:
            Draft6Validator.check_schema(schema)
            is_valid_draft6 = True
        except Exception as e:
            pass
        
        if is_valid_draft6:
            return "http://json-schema.org/draft-06/schema#"
    
    if draft4: 
        try:
            Draft4Validator.check_schema(schema)
            is_valid_draft4 = True
        except Exception as e:
            pass
        if is_valid_draft4:
            return "http://json-schema.org/draft-04/schema#"
        
    return False

def validate_instance(instance, schema):
    is_valid = True
    draft = ""
    # Some of our schemas have the wrong draft specified. Try both draft 4 and draft 6
    if "$schema" not in schema:
        draft = determine_schema_version(schema, draft4=True, draft6=True)
        if draft == False:
            print("Schema " + json_file + " is not valid for draft 4 or draft 6")
        else:
            schema["$schema"] = draft
    elif "draft-04" in schema["$schema"] and not determine_schema_version(schema, draft4=True):
        schema["$schema"] = "http://json-schema.org/draft-06/schema#"
        if not determine_schema_version(schema, draft6=True):
            print("Schema " + json_file + " is not valid for draft 4 or draft 6")
    elif "draft-06" in schema["$schema"] and not determine_schema_version(schema, draft6=True):
        schema["$schema"] = "http://json-schema.org/draft-04/schema#"
        if not determine_schema_version(schema, draft4=True):
            print("Schema " + json_file + " is not valid for draft 4 or draft 6")
    try:
        validate(instance=witness[0], schema=schema)
    except Exception as e:
        if "draft-06" in draft:
            schema["$schema"] = "http://json-schema.org/draft-04/schema#"
            is_valid = validate_instance(instance, schema)
        else:
            is_valid = False
        
    return is_valid

def run_test(schema, timeout):
    def test_json_runner(queue):
        witness = []
        deadline = timedelta(milliseconds=timeout) if timeout > 0 else None
        @given(from_schema(schema))
        @settings(
            suppress_health_check=(HealthCheck.filter_too_much, HealthCheck.too_slow, HealthCheck.data_too_large, HealthCheck.large_base_example), 
            max_examples=1, 
            verbosity=hypothesis.Verbosity.quiet, 
            deadline=deadline
        )
        def test_json(instance):
            witness.append(instance)

        try:
            test_json()
            queue.put((True, "", "", witness))
        except hypothesis.errors.Unsatisfiable:
            queue.put((False, "", "", []))
        except Exception as e:
            queue.put(("", str(e.__class__.__name__), str(e), []))

    queue = multiprocessing.Queue()
    process = multiprocessing.Process(target=test_json_runner, args=(queue,))
    process.start()

    process.join(math.ceil(timeout / 1000) if timeout > 0 else None)

    if process.is_alive():
        print(f"Timeout reached ({timeout}ms). Killing process.")
        process.terminate()  # Forcefully kill the process
        process.join()  # Ensure cleanup
        return "", "TimeoutException", f"Timeout after {timeout}ms", []

    return queue.get() if not queue.empty() else ("", "UnknownError", "Process exited without response", [])


if __name__ == '__main__':
    arg_parser = argparse.ArgumentParser()
    arg_parser.add_argument('-i', '--input', help='Input directory or file', required=True)
    arg_parser.add_argument('-o', '--output', help='Output directory', required=False)
    arg_parser.add_argument('-c', '--csv', help='Name of the CSV file', default='hypothesis-results.csv')
    arg_parser.add_argument('-e', '--errors', help='Name of the errors file', default='hypothesis-errors.txt')
    arg_parser.add_argument('-w', '--witness', help='Where to save the witness. Witness is not saved when unspecified', default="")
    arg_parser.add_argument('-q', '--quiet', help='Suppress output', action='store_true')
    arg_parser.add_argument('-t', '--timeout', help='Timeout for each test (ms)', default=0)
    input_path = arg_parser.parse_args().input
    
    output_dir = arg_parser.parse_args().output
    os.makedirs(output_dir, exist_ok=True)
    csv_file = arg_parser.parse_args().csv
    errors_file = arg_parser.parse_args().errors
    
    witness_dir = arg_parser.parse_args().witness
    
    is_quiet = arg_parser.parse_args().quiet
    verbosity = hypothesis.Verbosity.quiet if is_quiet else hypothesis.Verbosity.normal
    
    timeout = int(arg_parser.parse_args().timeout) if arg_parser.parse_args().timeout else 0
    if os.path.isfile(input_path):
        json_files = [input_path]
    elif os.path.isdir(input_path):
        json_files = [os.path.join(input_path, f) for f in os.listdir(input_path) if f.endswith('.json') and os.path.isfile(os.path.join(input_path, f))]
    else:
        raise ValueError("Input path is not a valid JSON file or directory containing JSON files.")
        
    csv = "objectId,totalTime,genSuccess,valid,error\n"
    with open(os.path.join(output_dir, csv_file), 'w') as file:
        file.write(csv)
    with open(os.path.join(output_dir, errors_file), 'w') as file:
        file.write("")
    for json_file in json_files:
        with open(json_file, 'r') as file:
            try:
                schema = json.load(file)
            except Exception as e:
                print("Error loading schema: ", json_file)
                csv_line = f"{os.path.basename(json_file)},0,False,False,SchemaLoadingError\n"
                with open(os.path.join(output_dir, csv_file), 'a') as file:
                    file.write(csv_line)
                raise e
        start = time.time()
        gen_success, error, error_message, witness = run_test(schema, timeout)
        total_time = round((time.time() - start) * 1000) 
        if gen_success:
            valid = validate_instance(witness[0], schema)
            witness_str = json.dumps(witness[0])
            if witness_dir:
                if not os.path.exists(witness_dir):
                    os.makedirs(witness_dir)
                witness_file = os.path.join(witness_dir, os.path.basename(json_file))
                witness_file = witness_file.replace('.json', '.witness.json')
                with open(witness_file, 'w') as file:
                    json.dump(witness[0], file, indent=4)
        else:
            valid = ""

        csv_line = f"{os.path.basename(json_file).replace('.json', '')},{total_time},{gen_success},{valid},{error}\n"
        with open(os.path.join(output_dir, csv_file), 'a') as file:
            file.write(csv_line)

        if error:
            with open(os.path.join(output_dir, errors_file), 'a') as file:
                file.write(f"File: {os.path.basename(json_file)}\nError: {error}\nMessage: {error_message}\n\n")
