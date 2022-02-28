package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;
import de.uni_passau.sds.patterns.REException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Endpoint {




	public static void main(String[] args) throws IOException {


		String path = System.getProperty("user.dir")+ "/witnessGenTestfiles/anotherWitness/";
		String file = "29"//"example_4-5"
				;
		String extension = ".json";
		String inputFileName = path+file+extension;
		System.out.println("parsed "+inputFileName);

		WitnessEnv env = null;


		JSONSchema root;
		Gson gson = new GsonBuilder()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.serializeNulls()
				.create();

		try (Reader reader = new FileReader(inputFileName)) {
			JsonObject object = gson.fromJson(reader, JsonObject.class);
			root = new JSONSchema(object);
			System.out.println("===original schema===");
			System.out.println(root);
			Assertion jsonSchema = Utils_JSONSchema.normalize(root).toGrammar();
			env = Utils_FullAlgebra.getWitnessAlgebra(jsonSchema);
		} catch (IOException | REException e) {
			e.printStackTrace();
		}

		String result = Utils.beauty(env.getFullAlgebra().toGrammarString());

		System.out.println("===algebra===");
		System.out.println(result);


	}
}
