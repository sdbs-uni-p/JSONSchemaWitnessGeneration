package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Utils_WitnessAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;

import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;


public class GenAndValidate {

    private String path;
    private boolean algebraic = false;

    public GenAndValidate(String path){
        this.path=path;
    }

    public GenAndValidate(String path, boolean algebraic){
        this.path=path;
        this.algebraic=algebraic;
    }


    /**
     *
     * @return witness in a string format
     * @throws IOException
     * @throws WitnessException
     * @throws REException
     */

    public String genWitness(String file) throws IOException, WitnessException, REException {

        Assertion schema;

        if (algebraic) {
            System.out.println("processing " +this.path + file);
            schema = Utils_FullAlgebra.parseFile(this.path + file);
            System.out.println("______________________________________");
            System.out.println(Utils.beauty(schema.toGrammarString()));
        }
        else {
            System.out.println("processing " +this.path + file);
            JSONSchema js = Utils_JSONSchema.parse(this.path + file);
            System.out.println("______________________________________");
            System.out.println("Schema ---> " + js.toJSON() + "\n");
            JSONSchema jsi = Utils_JSONSchema.normalize(js);
            schema = jsi.toGrammar();
        }



        // Previous pipeline
//        WitnessEnv env1 = Utils_WitnessAlgebra.getWitnessEnv1(schema);

        // New pipeline
        WitnessEnv env2 = Utils_WitnessAlgebra.getWitnessEnv2(schema);

        //System.out.println(Utils.beauty(env2.getFullAlgebra().toGrammarString())+"\n");

        GenEnv genv = null;
        try {
            genv = new GenEnv(env2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String witness = null;
        try {
            witness = genv.generate().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nWitness ---> "+witness);
        System.out.println("______________________________________\n\n");
        return witness;

    }


    // Validator used: networknt/Json-schema-validator

    /**
     *
     * @param witness
     * @return number of errors found during validation
     * @throws IOException
     * @throws WitnessException
     * @throws REException
     */
    public int validateWitness(String file, String witness) throws IOException, WitnessException, REException {

        String JSchemaSchema;

        if (algebraic) {
            Assertion algebraic = Utils_FullAlgebra.parseFile(this.path + file);
            JSchemaSchema = algebraic.toJSONSchema(null).toString();
        } else {
            JSchemaSchema = Files.readString(Path.of(this.path + file));
        }

        return validateStringWitness(JSchemaSchema,witness);
    }


    protected int validateStringWitness(String schemaString, String witness) throws IOException, WitnessException, REException {

        Pattern p4 = Pattern.compile("/draft-04/");
        Pattern p6 = Pattern.compile("/draft-06/");
        Pattern p7 = Pattern.compile("/draft-07/");
        Pattern regexXbool = Pattern.compile("(\"exclusiveMinimum\"|\"exclusiveMaximum\"):(false|False|true|True)");

        SpecVersion.VersionFlag version;

        schemaString = schemaString.replace(" ","");

        if(p4.matcher(schemaString).find() || regexXbool.matcher(schemaString).find())
            version = SpecVersion.VersionFlag.V4;
        else if(p6.matcher(schemaString).find())
            version = SpecVersion.VersionFlag.V6;
        else if(p7.matcher(schemaString).find())
            version = SpecVersion.VersionFlag.V7;
        else
            version = SpecVersion.VersionFlag.V201909;

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(version);
//        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(VersionFlag.V4);
        ObjectMapper mapper = new ObjectMapper();

        JsonSchema schema = factory.getSchema(schemaString);

        JsonNode node = mapper.readTree(witness);
        Set<ValidationMessage> errors = schema.validate(node);

        for (ValidationMessage m: errors){
            System.out.println(m);
        }

        return errors.size();
    }


}
