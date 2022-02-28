package massiveTesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.networknt.schema.JsonSchemaException;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import net.jimblackler.jsongenerator.Configuration;
import net.jimblackler.jsongenerator.Generator;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenerationTask implements Callable<Boolean> {

    private final File inputSchema;
    private final String id;
    SpecVersion.VersionFlag version = SpecVersion.VersionFlag.V201909;

    LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> witnessMap = new LinkedHashMap<>();
    LinkedHashMap<String, Set<ValidationMessage>> errorsMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> validationException = new LinkedHashMap<>();

    boolean witnessGenerationSuccess = false;
    boolean hasValidationErrors = false;
    String currentOperation;



    public GenerationTask(File inputSchema) {
        this.inputSchema = inputSchema;
        this.id = Utils.getSchemaId(inputSchema);
        Utils.initMaps(this.resultMap);
        this.resultMap.put(Utils.objectId,this.id);
        this.resultMap.put(Utils.inSize,String.valueOf(inputSchema.length()));

    }


    @Override
    public Boolean call() throws IOException {

        long execStart = System.currentTimeMillis();



        SchemaStore schemaStore = new SchemaStore();
        Schema schema;

        this.currentOperation = Utils.loadSchema;
        try {
            long start = System.currentTimeMillis();
            schema = schemaStore.loadSchema(this.inputSchema);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.loadSchema,String.valueOf(end - start));
        }catch (Exception e) {
            this.resultMap.put(Utils.loadSchema,e.getClass().getSimpleName());
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
            return false;
        }



        String s = schema.toString();
        int i = s.indexOf("{");
        String schemaAsString = null;
        if(i!=-1)
            schemaAsString = s.substring(i);

        if (schemaAsString==null)
            return  false;

        JsonPrimitive schemaObject = new JsonPrimitive(schemaAsString);

        this.version = Utils.getVersionFlag(this.inputSchema.getAbsolutePath());

        this.currentOperation = Utils.initGenerator;
        Generator generator;
        try {
            long start = System.currentTimeMillis();
            generator = new Generator(new Configuration() {
                @Override
                public boolean isPedanticTypes() {
                    return false;
                }

                @Override
                public boolean isGenerateNulls() {
                    return false;
                }

                @Override
                public boolean isGenerateMinimal() {
                    return false;
                }

                @Override
                public float nonRequiredPropertyChance() {
                    return 0.5f;
                }
            }, schemaStore, new Random(1));

            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.initGenerator,String.valueOf(end - start));
        }catch (Exception e) {
            this.resultMap.put(Utils.initGenerator,e.getClass().getSimpleName());
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
            return false;
        }

        this.currentOperation = Utils.generation;
        Object object;
        try {
            long start = System.currentTimeMillis();
            object = generator.generate(schema, 1);
            long end = System.currentTimeMillis();
            this.resultMap.put(Utils.generation,String.valueOf(end - start));
        }catch (Exception e) {
            this.resultMap.put(Utils.generation,e.getClass().getSimpleName());
            this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
            return false;
        }

        this.resultMap.put(Utils.totalTime,String.valueOf(System.currentTimeMillis()-execStart));
        this.currentOperation = Utils.valid;

        if(object==null) {
            this.resultMap.put(Utils.genSuccess, "false");
            this.resultMap.put(Utils.valid, "");
            return false;
        }

        else {
            String witness;
            JsonPrimitive js;
            if(object instanceof  String) {
                js = new JsonPrimitive(object.toString());
                witness = js.toString();
            }
            else
                witness = object.toString();
            this.witnessGenerationSuccess = true;
            this.witnessMap.put(this.id,object.toString());
            this.resultMap.put(Utils.genSuccess,"true");

            Set<ValidationMessage> errors;
            String schema2 = Files.readString(Path.of(this.inputSchema.toString()));
            try {
                errors = Utils.validateStringWitness(schema2,witness,this.version,this.id,this.validationException);
            } catch (JsonSchemaException e) {
                this.resultMap.put(Utils.valid,e.getClass().getSimpleName());
                Utils.addValidationException(id,e,validationException);
                return false;
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
