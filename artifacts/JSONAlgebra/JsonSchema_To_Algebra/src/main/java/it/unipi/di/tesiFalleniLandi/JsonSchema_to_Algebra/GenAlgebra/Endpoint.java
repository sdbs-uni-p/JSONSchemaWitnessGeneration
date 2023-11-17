package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.OneOf;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Utils_WitnessAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;


/**
 * Logging documentation: https://logging.apache.org/log4j/2.x/manual/configuration.html#JSON
 */

public class Endpoint {
    private static Logger logger = LogManager.getLogger(Endpoint.class);

    SpecVersion.VersionFlag version;
    String schemaString;
    String testFiles = "/testFiles/",
            witnessGenTestFiles = "/witnessGenTestFiles/",
            gitcleanfiles = "/../tests/Testing/gitcleanfiles/",
            userPath = System.getProperty("user.dir");


    public WitnessEnv algebraFromFile() throws IOException, REException {
        String path = userPath + testFiles;
        String file = "test";
        String extension = ".algebra";
        String inputFileName = path+file+extension;

        System.out.println("Parsing " + inputFileName);

        Assertion schema = Utils_FullAlgebra.parseFile(inputFileName);
        return Utils_FullAlgebra.getWitnessAlgebra(schema);
    }

    public Assertion assertionFromAlgebra() throws IOException {
        String path = userPath + testFiles;
        String file = "nested"; //"test";
        String extension = ".algebra";
        String inputFileName = path+file+extension;

        System.out.println("Parsing " + inputFileName);
        return Utils_FullAlgebra.parseFile(inputFileName);
    }

    private void showEnv(WitnessEnv env) {
        String result = Utils.beauty(env.getFullAlgebra().toGrammarString());
        System.out.println(result);
//        System.exit(0);
    }


    public Assertion assertionFromJSONSchema(){
        String subdir = "anotherWitness/";//"oneTypeSchemas/array/";//"LALE/";//"misc/";//"misc/";//"oneTypeSchemas/array/";//"mixedTypesSchemas/";
        // "NPproblems/";//"demopaper/";// "oneTypeSchemas/object/";//
        //"schemastore/";//"oneTypeSchemas/enum/"; //
        //"jsonSchemaTestSuiteDraft6/";//"jsonSchemaOrg/";//
        String path ="/expdataset/testing/"; // "/expdataset/github/ourSat/"; //"/oneOfAnyOf/InvalidWitnessException/InvalidWitnessException/"; // "/expDataset/failures/"; //"/expDataset/realWorldSchemas/processedFiles/"; // "/expDataset/realWorldSchemas/processedFiles/"; //"/expDataset/containment/sat/nonValid/"; //"/expDataset/unprocessedFiles/ok/"; // gitcleanfiles+"containment/unsat/witness/";//userPath+witnessGenTestFiles+subdir; //
        //"containment/sat/noWitness/";//"unsat-pb/";//containment/sat/nonValid/";//"containment/sat/noWitness/";//"unsat-pb/test";//"unsat-pb/o"+"24470";//"unsat-pb/o"+"25731";//"o4413";//"o48024";//"47";//"o69888";//"weka_j48";//31//"hittingset";//"1Another";//"2Another3";//"2bis";//"BizTalkServerApplicationSchema";//
        String file = "test";//"problem_minItems";//"c_problem_items";//"o79743";//"o"+"46176";//"o"+"13707";//"uplimit";//"test";//""+"13707";//"draft6_reflexive_contains_id5_subschema1_not_2";//"tmp"; //"draft6_reflexive_items_id7_subschema1_not_2";//
        String extension = ".json";


        //        problem minitems : o62756, o62779, o33729, o59981, o79743

        String inputFileName = userPath+path+file+extension;
        System.out.println("Parsing " + inputFileName);

        JSONSchema root;
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        Assertion jsonSchema = null;
        try (Reader reader = new FileReader(inputFileName)) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);

            //set version

            this.version = it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.Utils.getVersionFlag(inputFileName);
            //set schema string
            schemaString = object.toString();
//            System.out.println("Schema " + schemaString);

            root = new JSONSchema(object);
            root = Utils_JSONSchema.normalize(root);
            //OneOf.asAnyOf = true;
            jsonSchema = root.toGrammar();
//            jsonSchema = Utils_JSONSchema.normalize(root).toGrammar();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonSchema;
    }

    public static void main(String[] args) throws WitnessException, REException, IOException {

        Endpoint obj  = new Endpoint();
        boolean fromJS = true;//false; // false; //
        WitnessEnv env ;
        String witness = null;

        int attempts = 0;

        //by default attempt optimization
        OneOf.asAnyOf = true;
        int level = 2; //_outAll = 0,  _outLast= 1, _outRes = 2

        do{
            if(fromJS)
                env = Utils_WitnessAlgebra.getWitnessEnv3(obj.assertionFromJSONSchema(),level);
            else
                env = Utils_WitnessAlgebra.getWitnessEnv3(obj.assertionFromAlgebra(),level);

            GenEnv genv = null;
            try {
                genv = new GenEnv(env);

                witness = genv.generate().toString();
                System.out.flush();
                System.out.println("==witness (attempt" + attempts + ")== \n"+ witness);

            } catch (Exception e) {
                e.printStackTrace();
            }


            /*validate against the schema*/
            if(fromJS && witness.compareTo(it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.Utils.getEmptyWitnessSymbol())!=0)
            {
                Set<ValidationMessage> errors =
                        it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting.Utils.validateStringWitness(obj.schemaString,witness,obj.version);
                if(errors.size()==0){
                    System.out.println("++ valid witness ");
                    break;
                }
                else{
                    System.out.println("-- invalid witness ");
                    for (ValidationMessage m: errors){
                        System.out.println(m);
                    }
                    //TODO check if the problem is due to the oneOf optimization
                    OneOf.asAnyOf = false; //retry w/o the optimization
                }

            }
            else break;

            attempts++;
            if(attempts>2) break; //TODO remove after testing is finished
        }
        while(true);






    }
//    public static void main(String[] args) throws IOException, WitnessException, REException {
//        Endpoint obj  = new Endpoint();
//
//        //option 1 read from algebra
//        WitnessEnv env1 = obj.algebraFromFile();
//
//        //option 2 read from json schema
//        WitnessEnv env2 = obj.algebraFromJSONSchema();
//
//
//        //TODO choose here
//        WitnessEnv env = env2 ; //env1; //
//
//        obj.showEnv(env);
//
//        env.buildOBDD_notElimination();
//
////        obj.showEnv(env);
//
//        env.checkLoopRef(null, null);
//
//        env = (WitnessEnv) env.merge(null, null);
//
//        env = env.groupize();
//
//        env = env.DNF();
//
//        env.varNormalization_separation(null, null);
//
//        env = env.DNF();
//
//        env = env.varNormalization_expansion(null);
//
//
//
//        System.out.println("\r\n\r\n Preparation: \r\n");
//
//        env = (WitnessEnv) env.merge(null, null);
//
//        env.toOrPattReq();
//        env.objectPrepare();
//        env.arrayPreparation();
////        env = env.DNF();
////        env.varNormalization_separation(null, null);
////        env = env.varNormalization_expansion(null);
//
//
//
//        String result = Utils.beauty(env.getFullAlgebra().toGrammarString());
//
//        String outputFileName = obj.userPath + obj.testFiles + "prep_test.algebra";
//        FileWriter fw = new FileWriter(outputFileName);
//        fw.write(result);
//        fw.close();
//        System.out.println("output "+ outputFileName);
//
////        System.exit(0);//TODO remove
//
//
//        System.out.println("\r\n\r\n witness generation: \r\n");
//        System.out.flush();
//        GenEnv genv = null;
//        try {
//            genv = new GenEnv(env);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            String witness = genv.generate().toString();
//            System.out.flush();
//            System.out.println("==witness== \n"+ witness);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        GenAndValidate genVal = new GenAndValidate();
//
//    }
}
