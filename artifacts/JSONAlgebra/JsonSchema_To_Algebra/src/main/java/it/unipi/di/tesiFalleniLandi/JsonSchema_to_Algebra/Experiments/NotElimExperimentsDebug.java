package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Experiments;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Iterator;

public class NotElimExperimentsDebug
{

	private static Logger logger = LogManager.getLogger(NotElimExperimentsDebug.class);


	private static long nbLines(String str){
		String[] lines = str.split("\r\n|\r|\n");
		return lines.length;
	}


	public static void main(String[] args ) throws IOException{
		String userPath = System.getProperty("user.home"),
				path = userPath+"/Documents/WORK/DATA/JSONSCHEMA/extract";
		File dir = new File(path);
		File [] files = dir.listFiles();

		System.out.println("Path " + path);
		Gson gson = new Gson();
		JsonObject schemaObject = null,object=null;
		Assertion fullAlgebra, fullAlgebraNegated = null;

		JSONSchema root = null;
		String filename="";

		for (File file : files) {
			logger.info(file);
			filename=file.getName();
			System.out.println("processing " + filename);

			if (!filename.endsWith(".json")) {
				System.out.println("skipping non-json file...");
				continue;
			}

			try (Reader reader = new FileReader(file.getAbsolutePath())) {
				try {
					object = gson.fromJson(reader, JsonObject.class);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			try{
				//extract line and  schema_file
				Iterator<?> it = object.keySet().iterator();
				while(it.hasNext()) {
					String key = (String) it.next();
					switch (key) {
						case "line":
							logger.info("line " + object.get(key).getAsLong());
							break;
						case "id":
							logger.info("id " + object.get(key).getAsLong());
							break;
						case "schema_file":
							schemaObject = object.getAsJsonObject(key);
							break;
					}
				}

				root = new JSONSchema(schemaObject);
			}catch (Exception e) {
				e.printStackTrace();
			}


//			String alg = file.getAbsolutePath()+".alg",
//					notelim = file.getAbsolutePath()+".algNot";
//			FileWriter fw1 = new FileWriter(alg),
//						fw2 = new FileWriter(notelim);


			if(root!=null)
				fullAlgebra = Utils_JSONSchema.normalize(root).toGrammar();
			else
				continue;
			String fstr = Utils.beauty(fullAlgebra.toGrammarString());
			System.out.println(fstr);
//			fw1.write(fstr);
			logger.info("algebraic form size {} is written to disk",nbLines(fstr));
//			fw1.close();
			fullAlgebraNegated = Utils_FullAlgebra.parseString(fstr).notElimination();
			String nfstr = Utils.beauty(fullAlgebraNegated.toGrammarString());
			System.out.println(nfstr);
//			fw2.write(nfstr);
//			fw2.close();
			logger.info("algebraic form after not elimination size {} written to disk",nbLines(nfstr));

		}




	}


}