package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class MainClass_JSONSchema {
	private static Logger logger = LogManager.getLogger(MainClass_JSONSchema.class);

	public static void main(String[] args) throws IOException {
		String path = "test.json";
		JSONSchema root;
		Gson gson = new GsonBuilder()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.serializeNulls()
				.create();

		try (Reader reader = new FileReader(path)) {
			JsonObject object = gson.fromJson(reader, JsonObject.class);
			root = new JSONSchema(object);
		}

		System.out.println(gson.toJson(root.toJSON()));
		System.out.println("NORMALIZZATO: " + (Utils_JSONSchema.normalize(root).toJSON()));
		System.out.println("NORMALIZZATO algebra: " + (Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root))));

		FileWriter fw = new FileWriter("output.json");
		fw.write(Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root)));
		fw.close();

		System.out.println("\nParsing\n" + (Utils_FullAlgebra.parseString(Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(root)))));
	}
}