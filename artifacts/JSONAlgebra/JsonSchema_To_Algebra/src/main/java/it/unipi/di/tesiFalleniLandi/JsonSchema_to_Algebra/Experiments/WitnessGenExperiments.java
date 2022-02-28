package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Experiments;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra.GenEnv;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class WitnessGenExperiments {


	private HashMap<String, String> schemaWitness = new HashMap<>();
	private HashMap<String, String> errorMap = new HashMap<>();
	private HashMap<String, String> resultMap = new HashMap<>();
	private final List<String> ops = Arrays.asList("2Full","2Witness","notElim","merge1","groupize","DNF1",
			"separation","DNF2","expansion","merge2","toOrPattReq","objPrep","arrPrep","InitGenv","genWitness");


	private boolean genWitnessForSchemaFile(File input, int timeout) throws IOException {

		JSONSchema root;
		JsonObject object;

		try {
			object = parseJsonObjectFromFile(input);
		} catch (Exception e) {
			System.err.println(e.toString());
			return false;
		}

		root = extractSchemaElement(object);
		if (root == null)
			return false;

		JSONSchema finalRoot = root;

		final String[] witness = {null};

		final Runnable generate = new Thread() {
			@Override
			public void run() {

				Assertion fullAlgebra = null;
				try {
					long start = System.currentTimeMillis();
					fullAlgebra = Utils_JSONSchema.normalize(finalRoot).toGrammar();          
					long end = System.currentTimeMillis();
					resultMap.put("2Full", String.valueOf(end - start));

				} catch (Exception e) {
					resultMap.put("2Full",e.getClass().getSimpleName());
					return; // Do not continue.
				}

				WitnessEnv env = null;
				try {
					long start = System.currentTimeMillis();
					env = Utils_FullAlgebra.getWitnessAlgebra(fullAlgebra);
					long end = System.currentTimeMillis();
					resultMap.put("2Witness", String.valueOf(end - start));

				} catch (Exception e) {
					resultMap.put("2Witness",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis();
					env.buildOBDD_notElimination();                   
					long end = System.currentTimeMillis();
					resultMap.put("notElim", String.valueOf(end - start));
				}catch (Exception e) {
					resultMap.put("notElim",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis();
					env = (WitnessEnv) env.merge(null, null);
					long end = System.currentTimeMillis();
					resultMap.put("merge1", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("merge1",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis();
					env = env.groupize();   
					long end = System.currentTimeMillis();
					resultMap.put("groupize", String.valueOf(end - start));
				} catch (Exception we) {
					resultMap.put("groupize",we.getClass().getSimpleName());
					return;
				} 


				try {
					long start = System.currentTimeMillis();
					env = env.DNF();               
					long end = System.currentTimeMillis();
					resultMap.put("DNF1", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("DNF1",e.getClass().getSimpleName());
					return;
				}



				try {
					long start = System.currentTimeMillis();
					env.varNormalization_separation(null, null);
					long end = System.currentTimeMillis();
					resultMap.put("separation", String.valueOf(end - start));
				} catch (Exception we) {
					resultMap.put("separation",we.getClass().getSimpleName());
					return;
				} 


				try {
					long start = System.currentTimeMillis();
					env = env.DNF();
					long end = System.currentTimeMillis();
					resultMap.put("DNF2", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("DNF2",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis();
					env = env.varNormalization_expansion(null);
					long end = System.currentTimeMillis();
					resultMap.put("expansion", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("expansion",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis();
					env = (WitnessEnv) env.merge(null, null);   
					long end = System.currentTimeMillis();
					resultMap.put("merge2", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("merge2",e.getClass().getSimpleName());
					return;
				} 



				try {
					long start = System.currentTimeMillis();
					env.toOrPattReq();  
					long end = System.currentTimeMillis();
					resultMap.put("toOrPattReq", String.valueOf(end - start));
				}catch (Exception e) {
					resultMap.put("toOrPattReq",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis(); // attention   
					env.objectPrepare();                
					long end = System.currentTimeMillis();
					resultMap.put("objPrep", String.valueOf(end - start));
				} catch (Exception re) {
					resultMap.put("objPrep",re.getClass().getSimpleName());
					return;
				} 


				try {
					long start = System.currentTimeMillis();
					env.arrayPreparation();
					long end = System.currentTimeMillis();
					resultMap.put("arrPrep", String.valueOf(end - start));
				} catch (Exception re) {
					resultMap.put("arrPrep",re.getClass().getSimpleName());
					return;
				} 


				GenEnv genv = null;

				try {
					long start = System.currentTimeMillis();
					genv = new GenEnv(env);
					long end = System.currentTimeMillis();
					resultMap.put("InitGenv", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("InitGenv",e.getClass().getSimpleName());
					return;
				}


				try {
					long start = System.currentTimeMillis();
					witness[0] = genv.generate().toString();
					long end = System.currentTimeMillis();
					resultMap.put("genWitness", String.valueOf(end - start));
				} catch (Exception e) {
					resultMap.put("genWitness",e.getClass().getSimpleName());
					return;
				}

				schemaWitness.put(input.getName(), witness[0]);
				
				// TODO - remove
				System.out.println("Witness for Schema " + input.getName() + ":");
				System.out.println(witness[0]);
				System.out.println();

			}
		};



		final ExecutorService executor = Executors.newFixedThreadPool(6);
		final Future future = executor.submit(generate);
		try {
			future.get(timeout, TimeUnit.SECONDS);
		}
		catch (InterruptedException | ExecutionException | TimeoutException ie) {
			return false;
		}
		executor.shutdown();


		if (!executor.isTerminated())
			executor.shutdownNow();


		return witness[0]!=null;

	}





	private JSONSchema extractSchemaElement(JsonObject object) {
		JsonElement schemaElement = null;

		Iterator<?> it = object.keySet().iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			switch (key) {
			case "line":
			case "id":
				resultMap.put("objectId", String.valueOf(object.get(key)));
				break;
			case "schema_file":
				schemaElement = object.get(key);
				break;
			}
		}

		if (schemaElement == null) {
			// TODO - Should we raise an exception here?
			// We run into this case if the schema is not wrapped in a property
			// named "schema_file".
			System.err.println("Input file did not have the expected format.");
			return null;
		}
		
		return new JSONSchema(schemaElement);		
	}





	private JsonObject parseJsonObjectFromFile(File input) throws IOException {    	
		String shortFilename = input.getName().replace(".json","").replace("j","");

		JsonObject object;

		try (Reader reader = new FileReader(input)) {
			try {
				object = new Gson().fromJson(reader, JsonObject.class);
			} catch (JsonSyntaxException e) {
				errorMap.put(shortFilename,e.getClass().getSimpleName());
				throw e;
			}
		}catch (FileNotFoundException e) {
			errorMap.put(shortFilename,e.getClass().getSimpleName());
			throw e;
		} catch (IOException e) {
			errorMap.put(shortFilename,e.getClass().getSimpleName());
			throw e;
		}
		return object;
	}


	public static void main(String[] args) throws IOException {

		WitnessGenExperiments obj = new WitnessGenExperiments();

		// Read path with JSON instances from input args.
		if (args.length == 0) {
			System.out.println("Usage: <path>");
			return;
		}
		String path = args[0];
		File [] files = new File(path).listFiles();


		BufferedWriter csvFile = obj.createBufferedWriter(path,"result.csv");
		obj.setOutputHeader(csvFile);
		BufferedWriter schemaWitnessFile = obj.createBufferedWriter(path, "schemaWitness.txt");


		int success =0;
		int nbFiles=0;
//		int i=0;

		for (File file: files){
//			i++;
//			System.out.println(file+ " ---- "+i);
			if (!file.getName().endsWith(".json")) {
				System.out.println("skipping non-json file: " + file.getName());
				continue;
			}

			nbFiles++;
			boolean result = obj.genWitnessForSchemaFile(file, 1);
			if (result) 
				success++;

			obj.fillEmptyCells();

			StringBuilder r = obj.getRow(obj);
			r.append(","+result+"\n");

			System.out.println("Row ---> "+r.toString());


			csvFile.write(r.toString());

			StringBuilder w = new StringBuilder();

			obj.schemaWitness.forEach((k,v) -> w.append(k).append(" ---> ").append(v).append("\n\n****************************************************\n\n"));
			schemaWitnessFile.write(w.toString());

			obj.schemaWitness.clear();
			obj.resultMap.clear();

			schemaWitnessFile.flush();

			csvFile.flush();


		}

		csvFile.close();
		schemaWitnessFile.close();

		if (nbFiles == 0)
			nbFiles = 1;
		System.out.println("nb files : "
			+ nbFiles + "\nnb files with witnessGen success: "+success +"\n"+(float)success/nbFiles);
	}













	private BufferedWriter createBufferedWriter(String path,String filename) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(path+"/"+filename));
			return bw;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private void setOutputHeader(BufferedWriter res){
		try {
			res.write("objectId,2Full,2Witness,notElim,merge1,groupize,DNF1,separation,DNF2,expansion,merge2,toOrPattReq,objPrep,arrPrep,InitGenv,genWitness,genSuccess\n");
		}catch (IOException e){
			e.printStackTrace();
		}

	}

	
	private StringBuilder getRow(WitnessGenExperiments obj) {
		StringBuilder sb = new StringBuilder();

		sb.append(obj.resultMap.get("objectId"))
		.append(",")
		.append(obj.resultMap.get("2Full"))
		.append(",")
		.append(obj.resultMap.get("2Witness"))
		.append(",")
		.append(obj.resultMap.get("notElim"))
		.append(",")
		.append(obj.resultMap.get("merge1"))
		.append(",")
		.append(obj.resultMap.get("groupize"))
		.append(",")
		.append(obj.resultMap.get("DNF1"))
		.append(",")
		.append(obj.resultMap.get("separation"))
		.append(",")
		.append(obj.resultMap.get("DNF2"))
		.append(",")
		.append(obj.resultMap.get("expansion"))
		.append(",")
		.append(obj.resultMap.get("merge2"))
		.append(",")
		.append(obj.resultMap.get("toOrPattReq"))
		.append(",")
		.append(obj.resultMap.get("objPrep"))
		.append(",")
		.append(obj.resultMap.get("arrPrep"))
		.append(",")
		.append(obj.resultMap.get("InitGenv"))
		.append(",")
		.append(obj.resultMap.get("genWitness"));


		return  sb;
	}


	private void fillEmptyCells(){

		for (String s : ops){
			if (!resultMap.keySet().contains(s)) {
				resultMap.put(s,"");
			}
		}

	}


}
