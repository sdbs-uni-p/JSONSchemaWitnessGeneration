package validator;

import generateInternSchemas.GenerateInternSchemas;
import generateInternSchemas.helper.CreateWriteFile;
import generateInternSchemas.helper.PathUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestValidator {

    private final static Logger logger = LoggerFactory.getLogger(TestValidator.class);
    private static final Validator validator = new Validator();

    @Test
    public void testRefPathValidation() throws ParseException {
        String pathToSchema =  "/Users/luca/Projekte/SHK/JSONAlgebra/JsonSchema_To_Algebra/testsuites/containment_tests_internal_schemas/src/main/resources/broken_schemas/broken_ref.json";
        String schema =  CreateWriteFile.fileToSchema(pathToSchema);

        Object obj = new JSONParser().parse(schema);

        JSONObject jsonObject = (JSONObject) obj;
        boolean valid = validator.validateJson(jsonObject);

        Assertions.assertEquals(false, valid);
    }
}
