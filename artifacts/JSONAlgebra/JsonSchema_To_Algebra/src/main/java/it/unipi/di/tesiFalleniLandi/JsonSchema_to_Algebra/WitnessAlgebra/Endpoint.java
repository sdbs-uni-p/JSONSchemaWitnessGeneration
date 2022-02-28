package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Endpoint {
    private static Logger logger = LogManager.getLogger(Endpoint.class);

    public static void main(String[] args) throws IOException, WitnessException, REException {
        main2();
    }
    public static void main2() throws IOException, WitnessException, REException{
        String testFiles = "/testFiles/",
                witnessGenTestFiles = "/witnessGenTestfiles/oneTypeSchemas/";
        String path = System.getProperty("user.dir")+ witnessGenTestFiles ;
        String file = "number/15";
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
            Assertion jsonSchema = Utils_JSONSchema.normalize(root).toGrammar();
            WitnessEnv env = Utils_FullAlgebra.getWitnessAlgebra(jsonSchema);
            /*operations*/
//            env = (WitnessEnv) env.merge(null, null);
            /**/


            String outputSchema = Utils.beauty(env.getFullAlgebra().toGrammarString());

            String outputFileName = path+file+".walgebra";
            FileWriter fw = new FileWriter(outputFileName);
            fw.write(outputSchema);
            fw.close();
            System.out.println("output "+ outputFileName);


        }
    }
    public void main1() throws IOException, WitnessException, REException {
        String path = System.getProperty("user.dir")+ "/testFiles/";
        String file = "test"//"canon1" //"example_4-5"//
                ;
        String extension = ".algebra";
        String inputFileName = path+file+extension;

        Assertion schema = Utils_FullAlgebra.parseFile(inputFileName);
        System.out.println("parsed "+inputFileName);
        System.out.println(schema.toGrammarString());

        //System.out.println(schema.toGrammarString());

        WitnessEnv env = Utils_FullAlgebra.getWitnessAlgebra(schema);

        //WitnessEnv env = (WitnessEnv) schema.toWitnessAlgebra();

        env.buildOBDD_notElimination();

        env.checkLoopRef(null, null);

        System.out.println("\r\n\r\n Algebra: \r\n");

        System.out.println(Utils.beauty(env.getFullAlgebra().toGrammarString()));

        env = (WitnessEnv) env.merge(null, null);

        System.out.println("\r\n\r\n Merge: \r\n");
        System.out.flush();
        System.out.println(Utils.beauty(env.getFullAlgebra().toGrammarString()));

        System.out.println("\r\n\r\n Groupize: \r\n");
        System.out.flush();

        env = env.groupize();
        System.out.println(Utils.beauty(env.getFullAlgebra().toGrammarString()));

        System.out.println("\r\n\r\n DNF: \r\n");
        System.out.flush();

        env = env.DNF();
        System.out.println(Utils.beauty(env.getFullAlgebra().toGrammarString()));

        System.out.println("\r\n\r\n Separation: \r\n");
        System.out.flush();

        env.varNormalization_separation(null, null);

        System.out.println(Utils.beauty(env.getFullAlgebra().toGrammarString()));

        System.out.println("\r\n\r\n Expansion: \r\n");
        System.out.flush();

        env = env.varNormalization_expansion(null);

        System.out.println(Utils.beauty(env.getFullAlgebra().toGrammarString()));

        System.out.println("\r\n\r\n objectPrepare: \r\n");
        System.out.flush();

        env = (WitnessEnv) env.merge(null, null);

        env.toOrPattReq();
        env.objectPrepare();

//        //another round of normalization
//        env = env.varNormalization_expansion(null);

        String result = env.toString();
//        String result = Utils.beauty(env.getFullAlgebra().toGrammarString());
//
        String outputFileName = path+"text_"+file+extension;
        FileWriter fw = new FileWriter(outputFileName);
        fw.write(result);
        fw.close();
        System.out.println("output "+ outputFileName);
    }
}
