package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.networknt.schema.JsonSchemaException;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra.GenEnv;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenerateWitnessTask implements Callable<Boolean> {


    private File inputSchema;

    String id;

    SpecVersion.VersionFlag version = SpecVersion.VersionFlag.V201909;
    String schema =null;

    LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> witnessMap = new LinkedHashMap<>();
    LinkedHashMap<String, Set<ValidationMessage>> errorsMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> exceptionMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> validationException = new LinkedHashMap<>();
    LinkedHashMap<String, String> sizeMap = new LinkedHashMap<>();

    boolean hasValidationErrors = false;

    boolean witnessGenerationSuccess = false;

    boolean hasException = false;


    String currentOperation;

    int outSize; // Size of the grammar returned by the last successful step (between : toFull - merge3)
    // if problem in 2witness, then outSize = size of the algebra returned in 2Full ...

//    boolean processed=false; // Flag to check if the task ended (used to split the files into processed/unprocessed files)



    // Constructor
    public GenerateWitnessTask(File inputSchema) throws IOException {
        this.inputSchema = inputSchema;
        this.id = Utils.getSchemaId(inputSchema);
        Utils.initMaps(this.resultMap,this.sizeMap);
        this.resultMap.put(Utils.inSize,String.valueOf(inputSchema.length()));
        this.outSize=-1;
        this.resultMap.put(Utils.objectId,this.id); // To have the objectId in the csv
        // before: if there is an error in the parsing we don't have the objectId
        this.version = Utils.getVersionFlag(this.inputSchema.getAbsolutePath());
        this.sizeMap.put(Utils.objectId,this.id);
        this.sizeMap.put(Utils.inSize,String.valueOf(inputSchema.length()));
    }



    // Util methods


    String getCurrentOperation() {
        return this.currentOperation;
    }

    boolean hasValidationErrors() {
        return  this.hasValidationErrors;
    }

    LinkedHashMap<String, String> getResultMap(){
        return  this.resultMap;
    }

    LinkedHashMap<String, String> getWitnessMap(){
        return  this.witnessMap;
    }

    LinkedHashMap<String, Set<ValidationMessage>> getErrorsMap(){
        return  this.errorsMap;
    }

    LinkedHashMap<String, String> getExceptionMap() {
        return this.exceptionMap;
    }

    LinkedHashMap<String,String> getValidationException() { return this.validationException; }

    LinkedHashMap<String,String> getSizeMap() { return this.sizeMap; }



    private JSONSchema extractSchemaElement(JsonObject object) {
        JsonElement schemaElement = null;


        if(Utils.clean) {
            this.schema = object.toString();
            this.resultMap.put(Utils.objectId,this.id);
            return new JSONSchema(object);

        }
        Iterator<?> it = object.keySet().iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            switch (key) {
                case "line":
                case "id":
                    this.resultMap.put(Utils.objectId,String.valueOf(object.get(key)));
                    break;
                case "schema_file":
                    schemaElement = object.get(key);
                    this.schema = schemaElement.deepCopy().toString();
                    break;
            }
        }

        JSONSchema js = new JSONSchema(schemaElement);
        return js;
    }






    private JsonObject parseJsonObjectFromFile() throws IOException {

        JsonObject object;

        try (Reader reader = new FileReader(this.inputSchema)) {
            try {
                object = new Gson().fromJson(reader, JsonObject.class);
            } catch (JsonSyntaxException e) {
//                processed=true;
                throw e;
            }
        }catch (FileNotFoundException e) {
//            processed=true;
            throw e;
        } catch (IOException e) {
//            processed=true;
            throw e;
        }
        return object;
    }




    // Task to run

    @Override
    public Boolean call() throws IOException {


        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date)+" :  started processing  " + this.inputSchema.getName()+" of size "+this.inputSchema.length()+" bytes"+"  by thread  "+Thread.currentThread().getName());

        JSONSchema root;
        JsonObject object;

        long execStart = System.currentTimeMillis();

        try {
            this.currentOperation = Utils.parsing;
            long start = System.currentTimeMillis();
            object = parseJsonObjectFromFile();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.parsing,String.valueOf(end - start));
        } catch (Exception e) {
            this.resultMap.put(Utils.parsing,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.parsing);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.extractSchema;


        try {
            long start = System.currentTimeMillis();
            root = extractSchemaElement(object);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.extractSchema,String.valueOf(end - start));
        } catch (RuntimeException e) {
            this.resultMap.put(Utils.extractSchema,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.extractSchema);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }



        this.currentOperation = Utils.toFull;

        Assertion fullAlgebra;
        try {
            long start = System.currentTimeMillis();
            fullAlgebra = Utils_JSONSchema.normalize(root).toGrammar();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.toFull,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(fullAlgebra.toGrammarString().length()));
            this.sizeMap.put(Utils.toFull,String.valueOf((fullAlgebra.toGrammarString().length())));
        } catch (Exception e) {
            this.resultMap.put(Utils.toFull,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.toFull);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.toWitness;

        WitnessEnv env;
        try {
            long start = System.currentTimeMillis();
            env = Utils_FullAlgebra.getWitnessAlgebra(fullAlgebra);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.toWitness,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.toWitness,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.toWitness,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.toWitness);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.notElim;

        try {
            long start = System.currentTimeMillis();
            env.buildOBDD_notElimination();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.notElim,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.notElim,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        }catch (Exception e) {
            this.resultMap.put(Utils.notElim,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.notElim);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.merge1;


        try {
            long start = System.currentTimeMillis();
            env = (WitnessEnv) env.merge(null, null);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.merge1,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.merge1,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.merge1,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.merge1);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.groupize;


        try {
            long start = System.currentTimeMillis();
            env = env.groupize();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.groupize,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.groupize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.groupize,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.groupize);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }



        this.currentOperation = Utils.separation;

        try {
            long start = System.currentTimeMillis();
            env.varNormalization_separation(null, null);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.separation,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.separation,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.separation,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.separation);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.expansion;




        try {
            long start = System.currentTimeMillis();
            env = env.varNormalization_expansion(null);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.expansion,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.expansion,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.expansion,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.expansion);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.dnf;

        try {
            long start = System.currentTimeMillis();
            env = env.DNF();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.dnf, String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.resultMap.put(Utils.dnfSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.dnf,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.dnf,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.dnf);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.merge2;

        try {
            long start = System.currentTimeMillis();
            env = (WitnessEnv) env.merge(null, null);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.merge2,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.merge2,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.merge2,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.merge2);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.prepration;

        try {
            long start = System.currentTimeMillis();
            env.objectPrepare();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.prepration,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.prepration,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception re) {
            this.resultMap.put(Utils.prepration,re.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,re,this.exceptionMap,Utils.prepration);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.arrPrep;

        try {
            long start = System.currentTimeMillis();
            env.arrayPreparation();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.arrPrep,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.arrPrep,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception re) {
            this.resultMap.put(Utils.arrPrep,re.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,re,this.exceptionMap,Utils.arrPrep);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }



        this.currentOperation = Utils.merge3;

        try {
            long start = System.currentTimeMillis();
            env = (WitnessEnv) env.merge(null, null);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.merge3,String.valueOf(end - start));
            this.resultMap.put(Utils.outSize,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
            this.sizeMap.put(Utils.merge3,String.valueOf(env.getFullAlgebra().toGrammarString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.merge3,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.merge3);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.initGEnv;
        GenEnv genv;

        try {
            long start = System.currentTimeMillis();
            genv = new GenEnv(env);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.initGEnv,String.valueOf(end - start));
            this.sizeMap.put(Utils.genEnv,String.valueOf(genv.toString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.initGEnv,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.initGEnv);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }


        this.currentOperation = Utils.genWitness;

        JsonElement witness;
        try {
            long start = System.currentTimeMillis();
            witness = genv.generate();
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.genWitness,String.valueOf(end - start));
            this.sizeMap.put("witness",String.valueOf(witness.toString().length()));
        } catch (Exception e) {
            this.resultMap.put(Utils.genWitness,e.getClass().getSimpleName());
            this.resultMap.put(Utils.cancelledAt,this.currentOperation);
            this.hasException = true;
            Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.genWitness);
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
//            processed=true;
            return false;
        }

        this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
        this.currentOperation = Utils.valid;




        if (witness==null) {
            this.resultMap.put(Utils.genSuccess,"false");
            this.resultMap.put(Utils.noWitness,"false");
            this.resultMap.put(Utils.valid,"");
//            processed=true;
            return false;
        }
        if(witness.toString().equals(Utils.getEmptyWitnessSymbol())) {
            this.resultMap.put(Utils.genSuccess,"false");
            this.resultMap.put(Utils.noWitness,"true");
            this.resultMap.put(Utils.valid,"");
//            processed=true;
            return false;
        }

        if(!witness.toString().equals(Utils.getEmptyWitnessSymbol())) {
            this.witnessMap.put(this.id,witness.toString());
            this.witnessGenerationSuccess = true;
            this.resultMap.put(Utils.genSuccess,"true");
            this.resultMap.put(Utils.noWitness,"false");



//            processed=true;
//            String parsedSchema = null;
//            try {
//                parsedSchema = Utils_JSONSchema.parse(this.inputSchema.getPath()).toJSON().toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Set<ValidationMessage> errors;
            String schema2 = Files.readString(Path.of(this.inputSchema.toString()));

            try {
//                System.out.println(this.version+"\n"+parsedSchema);
//                errors = Utils.validateStringWitness(parsedSchema,witness.toString(),this.version,this.id,this.validationException);
                errors = Utils.validateStringWitness(schema2,witness.toString(),this.version,this.id,this.validationException);
            } catch (JsonSchemaException e) {
                this.resultMap.put(Utils.valid,e.getClass().getSimpleName());
                this.resultMap.put(Utils.cancelledAt,this.currentOperation);
                this.hasException = true;
                Utils.addExceptionInformation(this.id,e,this.exceptionMap,Utils.valid);
                Utils.addValidationException(this.id,e,this.validationException);
                return  false;
            }




            boolean err = errors.size()==0;
            this.resultMap.put(Utils.valid,String.valueOf(err));

            if (!err)  {
                this.hasValidationErrors = true;
                this.errorsMap.put(this.id, errors);
            }

        }


        return true;


    }



}