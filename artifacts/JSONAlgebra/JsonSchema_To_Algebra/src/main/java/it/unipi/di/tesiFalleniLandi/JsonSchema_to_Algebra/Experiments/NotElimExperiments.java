package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import de.uni_passau.sds.patterns.REException;

public class NotElimExperiments
{
	private static HashMap<Integer,String> operations = new HashMap<>();

	private  HashMap<String,String> errorsMap; //filename->exception code
	private  HashMap<String, Long> resultMap; //key->value

	private static String _objectid = "objectid";
	private static String _size_before = "size_before";
	private static String _size_after = "size_after";
	private static String _exec_time = "exec_time";
	private static String _error = "error";
	private static String _operation = "operation";
	private static String _idrun = "idrun";


	public void addError(String fname, String exception) {
		errorsMap.put(fname,exception.replace("\n", " ").replace("\r", " "));
	}
	public void addError(String fname, Exception exception) {
		try{
			errorsMap.put(fname,exception.getMessage().replace("\n", " ").replace("\r", " "));
		}
		catch (Exception e){
			errorsMap.put(fname, e.toString());
		}
		exception.printStackTrace();
	}

	public void addResult(String key, Long val) {
		resultMap.put(key,val);
	}



	public NotElimExperiments() {
		operations.put(1, "JsonSchema2fullAlgebra");
		operations.put(2, "JsonSchema2witnessAlgebra");
		operations.put(3, "JsonSchema2fullAlgebra_notElimination");
//		operations.put(4, "JsonSchema2witnessAlgebra_notElimination");

		errorsMap = new HashMap<>();
		resultMap = new HashMap<>();

//		errors.put(0l, "FileNotFoundException");
		
//		errors.put(1l, "IOException");
//		errors.put(2l, "JsonSyntaxException");
//		errors.put(3l, "TimeoutException");
//		errors.put(4l, "NotEliminationCode");
//		errors.put(99l, "Exception");

	}


	/**
	 * returns true and populates resultMap is execution succeeds
	 * otherwise, returns false when an exception is caught and populates the errorsMap
	 * @param file
	 * @param code
	 * @param idrun
	 * @param timeout
	 * @return
	 */
	public boolean operation(File file, Integer code, Integer idrun, Integer timeout)  {
		Instant start = Instant.now();
		String extension = ".algebra";
		final Assertion[] fullAlgebra = {null};
		final WitnessAssertion[] witnessAlgera = {null};
//		final String[] outputSchema = {null};
//		final Defs_Assertion[] fullAlgebra = {null};
		JSONSchema root;
		String filename = file.getName();

//		Gson gson = new GsonBuilder()
//				.disableHtmlEscaping()
//				.setPrettyPrinting()
//				.serializeNulls()
//				.create();

		Gson gson = new Gson();

		JsonElement schemaElement = null;
		JsonObject object=null;
		try (Reader reader = new FileReader(file.getAbsolutePath())) {
			try {
				object = gson.fromJson(reader, JsonObject.class);
			} catch (JsonSyntaxException e) {
				this.addError(filename,e);
				return false;
			}
		}catch (FileNotFoundException e) {
			this.addError(filename,e);
			return false;
		} catch (IOException e) {
			this.addError(filename,e);
			return false;
		}

		try{
			//extract line and  schema_file
			Iterator<?> it = object.keySet().iterator();
			while(it.hasNext()) {
				String key = (String) it.next();
				switch (key) {
					case "line":
						resultMap.put(_objectid, object.get(key).getAsLong());
						break;
					case "id":
						resultMap.put(_objectid, object.get(key).getAsLong());
						break;
					case "schema_file":
						// Do not insist on extracting a JsonObject, extract JsonElement.
						schemaElement = object.get(key); 
//						System.out.println(schemaElement);
						break;
				}
			}

			root = new JSONSchema(schemaElement);
			
		}catch (Exception e) {
			System.err.println(e.getStackTrace());
			this.addError(filename,e);
			return false;
		}

		//timeout

		final Runnable JsonSchema2fullAlgebra = new Thread() {
			@Override
			public void run() {
				switch (code){
					case 1: //JsonSchema2fullAlgebra
						try{
							if(root!=null)
								Utils_JSONSchema.normalize(root).toGrammar();
							else
								return;
//							fullAlgebra[0] = Utils_FullAlgebra.parseString(Utils_JSONSchema.normalize(root).toGrammar().toGrammarString());
						}catch (Exception e){
							addError(filename,e);
							return;
						}
						break;
					case 2: //JsonSchema2witnessAlgebra
						try{
							if(root!=null)
								Utils_JSONSchema.normalize(root).toGrammar().toWitnessAlgebra(null,null,null);
							else
								return;
//							WitnessEnv env = Utils_FullAlgebra.getWitnessAlgebra(fullAlgebra[0]);
//							outputSchema[0] = Utils.beauty(env.getFullAlgebra().toGrammarString());
//							Utils_FullAlgebra.parseString(Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root)))
//									.toWitnessAlgebra(null,null, null);
						}catch (REException e) {
							addError(filename,e);
							return;
						}
						break;
					case 3: //fullAlgebra_notElimination
						try {
							if(root != null) {
								fullAlgebra[0] = Utils_JSONSchema.normalize(root).toGrammar();
								String fstr = Utils.beauty(fullAlgebra[0].toGrammarString());
								addResult(_size_before, (long) fstr.length());
								String nfstr = Utils.beauty(Utils_FullAlgebra.parseString(fstr).notElimination().toGrammarString());
								addResult(_size_after,  (long) nfstr.length());
							}else
								return;

						}catch (Exception e){
							addError(filename,e);
							return;
						}

						break;
//					case 4: //witnessAlgebra_notElimination
//						try {
//							WitnessEnv env = Utils_FullAlgebra.getWitnessAlgebra(fullAlgebra[0]);
//							String before = Utils.beauty(env.getFullAlgebra().toGrammarString());
//							addResult(_size_before, (long) before.length());
//							env.buildOBDD_notElimination(); //modify in-place
//							outputSchema[0] = Utils.beauty(env.getFullAlgebra().toGrammarString());
//							addResult(_size_after, (long) outputSchema[0].length());
//						} catch (REException e) {
//							addError(filename,e);
//							return;
//						}
//						break;

				}

				}
		};
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future future = executor.submit(JsonSchema2fullAlgebra);
		executor.shutdown(); // This does not cancel the already-scheduled task.

		try {
			future.get(timeout, TimeUnit.MINUTES);
		}
		catch (InterruptedException ie) {
			this.addError(filename,ie);
			return false;
		}
		catch (ExecutionException ee) {
			this.addError(filename,ee);
			return false;
		}
		catch (TimeoutException te) {
			this.addError(filename,"Timeout");
			return false;
		}
		if(errorsMap.containsKey(filename))
			return false;

		if (!executor.isTerminated())
			executor.shutdownNow();


		Instant end = Instant.now();
		addResult(_exec_time, Duration.between(start, end).toMillis());
		addResult(_operation,(long) code);
		addResult(_idrun,(long) idrun);
		return true;
	}


	public static void main(String[] args ) throws IOException{
//		int timeout = 1; //in minutes

		NotElimExperiments obj = new NotElimExperiments();


		if(args.length != 4){
			System.out.println("Expects 4 arguments: directory path,  experiments code, idrun, timeout limit");
			operations.forEach((k,v) -> System.out.println("code: "+k+"  corresponds to: "+v));
			System.exit(-1);
		}
		String path = args[0];
		int op = Integer.parseInt(args[1]);
		File dir = new File(path);
		File [] files = dir.listFiles();
		Integer idrun = Integer.parseInt(args[2]);
		int timeout = Integer.parseInt(args[3]); //in minutes

		String _output =  "output";
		String _op = "_" + operations.get(op) ;


		BufferedWriter res = new BufferedWriter(new FileWriter(path+"/"+ _output + _op + ".csv"));
		BufferedWriter err = new BufferedWriter(new FileWriter(path+"/" + _output+ _op + ".log"));


		boolean headerOut = false;
		boolean b = false;

		String filename="";
		for (File file : files) {
			filename=file.getName();
			System.out.println(file);

			if (!filename.endsWith(".json")) {
				System.out.println("skipping non-json file...");
				continue;
			}

			try{
				b = obj.operation(file, op, idrun, timeout);
			}
			catch (OutOfMemoryError e) {
				obj.addError(filename,"OOM");
//				obj.addError(file.getName(),e.getMessage());
			}
			StringBuilder r = new StringBuilder();
			StringBuilder e = new StringBuilder();

			if(b)
			{
				//write result
				if(!headerOut){
					obj.resultMap.forEach((k, v) -> r.append(k).append(","));
					r.replace(r.length()-1,r.length(),"");//remove last comma
					r.append("\r\n");
					headerOut=true;
				}
				obj.resultMap.forEach((k, v) -> r.append(v).append(","));
				r.replace(r.length()-1,r.length(),"");//remove last comma
				r.append("\r\n");
				res.write(r.toString());
				//clear
				obj.resultMap.clear();
				res.flush();
			}
			else{
				//write error
				obj.errorsMap.forEach((k, v) ->e.append(idrun).append("\t").append(op).append("\t").append(k).append("\t").append(v));
				e.append("\r\n");
				err.write(e.toString());
				obj.errorsMap.clear();
				err.flush();
			}
		}

		err.close();
		res.close();

	}

//	public void writeResultsErrors(String path) throws IOException {
//
//		BufferedWriter res = new BufferedWriter(new FileWriter(path+"/output.csv"));
//		BufferedWriter err = new BufferedWriter(new FileWriter(path+"/output.log"));
//
//		StringBuilder r = new StringBuilder();
//		StringBuilder e = new StringBuilder();
//
//		boolean headerOut = false;
//
//		if(!headerOut){
//			this.resultMap.forEach((k, v) -> r.append(k).append(","));
//			r.replace(r.length()-1,r.length(),"");//remove last comma
//			r.append("\r\n");
//			headerOut=true;
//		}
//		this.resultMap.forEach((k, v) -> r.append(v).append(","));
//		r.replace(r.length()-1,r.length(),"");//remove last comma
//		r.append("\r\n");
//		res.write(r.toString());
//
//		errorsMap.forEach((k, v) ->e.append(k).append("\t").append(v));
//		e.append("\r\n");
//		err.write(e.toString());
//
//		err.close();
//		res.close();
//
//	}
}