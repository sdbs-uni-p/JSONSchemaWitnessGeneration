package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Endpoint {
	private static Logger logger = LogManager.getLogger(Endpoint.class);

	public static void main(String[] args) throws IOException {
		logger.debug("JSONSchema endpoint");
		String path = System.getProperty("user.dir")+ "/testFiles/";
		String file = "1pp";
		String extension = ".json";
		String inputFileName = path+file+extension;

		JSONSchema root;
		Gson gson = new GsonBuilder()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.serializeNulls()
				.create();

		try (Reader reader = new FileReader(inputFileName)) {
			JsonObject object = gson.fromJson(reader, JsonObject.class);
			root = new JSONSchema(object);
			System.out.println("parsed "+inputFileName);
		}

//		System.out.println(root);

//		System.out.println(gson.toJson(root.toJSON()));
//		System.out.println("NORMALIZZATO: " + (Utils_JSONSchema.normalize(root).toJSON()));
//		System.out.println("NORMALIZZATO algebra: " + (Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root))));

		boolean outputAlgebra = true; // false ; //
		//normalized
		if(outputAlgebra){
			String outputFileName = path+file+".algebra";
			FileWriter fw = new FileWriter(outputFileName);
			fw.write(Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root)));
			fw.close();
			System.out.println("output "+ outputFileName);
		}
		else
		{
			String outputFileName = path+file+"_norm"+".json";
			FileWriter fw = new FileWriter(outputFileName);
			fw.write(Utils_JSONSchema.normalize(root).toJSON().toString());
			fw.close();
			System.out.println("output "+ outputFileName);
		}


//		System.out.println("\nParsing\n" + (Utils_FullAlgebra.parseString(Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root)))));
	}
}