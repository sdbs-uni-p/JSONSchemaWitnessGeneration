package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.networknt.schema.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Utils {

    //Operations (columns of the csv file)
    final static String objectId = "objectId";
    final static String parsing = "parsing";
    final static String extractSchema = "extractSchema";
    final static String toFull = "2Full";
    final static String toWitness = "2Witness";
    final static String notElim = "notElim";
    final static String merge1 = "merge1";
    final static String groupize = "groupize";
    final static String separation = "separation";
    final static String expansion = "expansion";
    final static String dnf = "DNF";
    final static String merge2 = "merge2";
    final static String prepration = "preparation";
    final static String arrPrep = "arrPrep";
    final static String merge3 = "merge3";
    final static String initGEnv = "initGEnv";
    final static String genWitness = "genWitness";

    // Additional columns
    final static String genSuccess = "genSuccess";
    final static String noWitness = "noWitness";
    final static String valid = "valid";


    final static String inSize = "inSize";
    final static String outSize = "outSize";
    final static String totalTime = "totalTime";
    final static String dnfSize = "dnfSize";

    final static String dateAndTime = "dateAndTime";
    final static String timeout = "timeout";
    final static String git = "git";
    final static String machine = "machine";

    final static String nbIterations = "#iterations";
    final static String cancelledAt = "cancelledAt";


    final static LinkedList<String> ops = new LinkedList<>(Arrays.asList(objectId, inSize, outSize, //dnfSize,
            totalTime, parsing, extractSchema, toFull, toWitness, notElim, merge1, groupize,
            separation, expansion, //merge2,
            prepration,
            merge3, initGEnv, genWitness, genSuccess, noWitness,
            valid, nbIterations, cancelledAt, dateAndTime, timeout, git, machine));




    // Exception
    final static  String operation = "operation";
    final static String exceptionName = "exceptionName";
    final static String message = "message";
    final static String stackTrace = "stackTrace";
    final static String stackSize = "stackSize";

    // size columns
    final static String genEnv = "genEnv";
    final static  String witness = "witness";

    final static LinkedList<String> sizeOps = new LinkedList<>(Arrays.asList(objectId,inSize,toFull,toWitness,notElim,
            merge1,groupize,separation,expansion, //merge2,
            prepration, merge3,genEnv,witness));


    static boolean clean=false;


    static String emptyWitnessSymbol = "{\"Problem\":\"Empty witness\"}";

    private static Pattern p4 = Pattern.compile("/draft-04/");
    private static Pattern p6 = Pattern.compile("/draft-06/");
    private static Pattern p7 = Pattern.compile("/draft-07/");
    private static Pattern regexXbool = Pattern.compile("(\"exclusiveMinimum\"|\"exclusiveMaximum\"):(false|False|true|True)");




    public static String getEmptyWitnessSymbol() {
        return emptyWitnessSymbol;
    }

//    change [^0-9] to .json if not working with the schemas that have names o[0-9]+.json
    public static String getSchemaId(File file){
        return file.getName().replaceAll(".json","");
    }


    static void initMaps(LinkedHashMap<String, String> resultMap, LinkedHashMap<String, String> sizeMap){
        for(String op: ops) {
            resultMap.put(op,"");
        }

        for(String op: sizeOps) {
            sizeMap.put(op,"");
        }
    }







    static BufferedWriter createBufferedWriter(String path, String filename) {
        BufferedWriter bw;
        File file = new File(path + "/" + filename);
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            return bw;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    static void setCSVHeader(BufferedWriter res){
        try {
            String header = String.join(",",ops);
            res.write(header+"\n");
        }catch (IOException e){
            e.printStackTrace();
        }

    }



    static void addExceptionInformation(String id, Exception e, LinkedHashMap<String, String> exceptionMap, String op) {
        exceptionMap.put(objectId, id);
        exceptionMap.put(operation,op);
        exceptionMap.put(exceptionName,e.getClass().getSimpleName());
        exceptionMap.put(message,StringEscapeUtils.escapeCsv(e.getMessage()));
        exceptionMap.put(stackSize,String.valueOf(e.getStackTrace().length));
        StringBuilder stack = new StringBuilder();

        for (int i=0; i<e.getStackTrace().length;i++) {
            StackTraceElement STE = e.getStackTrace()[i];
            String s = STE.getFileName()+":"+STE.getMethodName()+":"+STE.getLineNumber();
            stack.append(StringEscapeUtils.escapeCsv(s));
            if(i<e.getStackTrace().length-1)
                stack.append("\n,,,,,");

        }
        exceptionMap.put(stackTrace,stack.toString());

    }

    static void addValidationException(String id, Exception e, LinkedHashMap<String, String> validationException) {
        validationException.put(objectId,id);
        validationException.put(exceptionName,e.getClass().getSimpleName());
        validationException.put(message,StringEscapeUtils.escapeCsv(e.getMessage()));
    }

    //********************************************************************************************



    static void writeOnExceptionsFile(LinkedHashMap<String, String> exceptionMap, BufferedWriter exceptionsFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(exceptionMap.get(objectId))
                .append(",")
                .append(exceptionMap.get(operation))
                .append(",")
                .append(exceptionMap.get(exceptionName))
                .append(",")
                .append(exceptionMap.get(message))
                .append(",")
                .append(exceptionMap.get(stackSize))
                .append(",")
                .append(exceptionMap.get(stackTrace))
                .append("\n");

        exceptionsFile.write(sb.toString());

    }


    static void writeOnValidationException(LinkedHashMap<String, String> validationException, BufferedWriter validationExceptionFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(validationException.get(objectId))
                .append(",")
                .append(validationException.get(exceptionName))
                .append(",")
                .append(validationException.get(message))
                .append("\n");

        validationExceptionFile.write(sb.toString());

    }




    static void writeOnErrorsFile(LinkedHashMap<String, Set<ValidationMessage>> errorsMap, BufferedWriter errorsFile) throws IOException {
        Map.Entry<String, Set<ValidationMessage>> entry = errorsMap.entrySet().iterator().next();
        String id = entry.getKey();
        Set<ValidationMessage> errors = entry.getValue();

        String w = "";




        List<String> errorsTypes = errors.stream().map(m -> m.getType()).collect(Collectors.toList());

        String s1 = "";
        for (int i=0; i<errorsTypes.size();i++) {
            String errorType = errorsTypes.get(i);
            s1 = s1.concat(errorType);
            if(i<errorsTypes.size()-1)
                s1 = s1.concat(",");
        }

        s1 = StringEscapeUtils.escapeCsv(s1);



        String s2 = "";
        int i=0;
        for(Iterator<ValidationMessage> it = errors.iterator(); it.hasNext(); ){
            i++;
            String message = it.next().getMessage();
            s2 = s2.concat(message);
            if(i<errors.size()-1)
                s2 = s2.concat(",");
        }

//        String errorsMessagesString = StringEscapeUtils.escapeCsv(sb2.toString());


        s2 = s2.replace(",","    ");
        w = w.concat(id+","+errors.size()+","+s1+","+s2+"\n");
        errorsFile.write(w);
    }


    static void writeOnWitnessFile(LinkedHashMap<String, String> witnessMap, BufferedWriter witnessFile) throws IOException {
        Map.Entry<String, String> entry = witnessMap.entrySet().iterator().next();
        String id = entry.getKey();
        String witness = entry.getValue();

        StringBuilder w = new StringBuilder();
        witness = StringEscapeUtils.escapeCsv(witness);
        w.append(id+","+witness+"\n");
        witnessFile.write(w.toString());
    }


    static void writeOnSizeFile(LinkedHashMap<String,String> sizeMap, BufferedWriter sizeFile) throws  IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(sizeMap.get(objectId))
                .append(",")
                .append(sizeMap.get(inSize))
                .append(",")
                .append(sizeMap.get(toFull))
                .append(",")
                .append(sizeMap.get(toWitness))
                .append(",")
                .append(sizeMap.get(notElim))
                .append(",")
                .append(sizeMap.get(merge1))
                .append(",")
                .append(sizeMap.get(groupize))
                .append(",")
                .append(sizeMap.get(separation))
                .append(",")
                .append(sizeMap.get(expansion))
                .append(",")
//                .append(sizeMap.get(dnf))
//                .append(",")
//                .append(sizeMap.get(merge2))
//                .append(",")
                .append(sizeMap.get(prepration))
                .append(",")
//                .append(sizeMap.get(arrPrep))
//                .append(",")
                .append(sizeMap.get(merge3))
                .append(",")
                .append(sizeMap.get(genEnv))
                .append(",")
                .append(sizeMap.get(witness))
                .append("\n");

        sizeFile.write(sb.toString());


    }


    static void writeResultsOnCSV(LinkedHashMap<String, String> resultMap, BufferedWriter csvFile) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(resultMap.get(objectId))
                .append(",")
                .append(resultMap.get(inSize))
                .append(",")
                .append(resultMap.get(outSize))
                .append(",")
//                .append(resultMap.get(dnfSize))
//                .append(",")
                .append(resultMap.get(totalTime))
                .append(",")
                .append(resultMap.get(parsing))
                .append(",")
                .append(resultMap.get(extractSchema))
                .append(",")
                .append(resultMap.get(toFull))
                .append(",")
                .append(resultMap.get(toWitness))
                .append(",")
                .append(resultMap.get(notElim))
                .append(",")
                .append(resultMap.get(merge1))
                .append(",")
                .append(resultMap.get(groupize))
                .append(",")
                .append(resultMap.get(separation))
                .append(",")
                .append(resultMap.get(expansion))
                .append(",")
//                .append(resultMap.get(dnf))
//                .append(",")
//                .append(resultMap.get(merge2))
//                .append(",")
                .append(resultMap.get(prepration))
                .append(",")
//                .append(resultMap.get(arrPrep))
//                .append(",")
                .append(resultMap.get(merge3))
                .append(",")
                .append(resultMap.get(initGEnv))
                .append(",")
                .append(resultMap.get(genWitness))
                .append(",")
                .append(resultMap.get(genSuccess))
                .append(",")
                .append(resultMap.get(noWitness))
                .append(",")
                .append(resultMap.get(valid))
                .append(",")
                .append(resultMap.get(nbIterations))
                .append(",")
                .append(resultMap.get(cancelledAt))
                .append(",")
                .append(resultMap.get(dateAndTime))
                .append(",")
                .append(resultMap.get(timeout))
                .append(",")
                .append(resultMap.get(git))
                .append(",")
                .append(resultMap.get(machine))
                .append("\n");
        csvFile.write(sb.toString());
    }






    //******************************************************************************************************************


    // The 2 following methods :
    //      1- get the version of the schema
    //      2- validate the witness

    public static SpecVersion.VersionFlag getVersionFlag(String pathToFile) throws IOException {
        SpecVersion.VersionFlag version = SpecVersion.VersionFlag.V201909;


        String s = Files.readString(Path.of(pathToFile));
        s = s.replace(" ","");

        if(p4.matcher(s).find() || regexXbool.matcher(s).find())
            version = SpecVersion.VersionFlag.V4;
        else if(p6.matcher(s).find())
            version = SpecVersion.VersionFlag.V6;
        else if(p7.matcher(s).find())
            version = SpecVersion.VersionFlag.V7;

        return version;
    }


    /**
     * simplest version of validation: returns errors caught by validator
     * @param schemaString
     * @param witness
     * @param version
     * @return
     * @throws IOException
     */
    public static Set<ValidationMessage> validateStringWitness(String schemaString, String witness, SpecVersion.VersionFlag version) throws IOException {

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(version);
        ObjectMapper mapper = new ObjectMapper();

        JsonSchema schema = factory.getSchema(schemaString);

        JsonNode node = mapper.readTree(witness);
        Set<ValidationMessage> errors = schema.validate(node);

//        for (ValidationMessage m: errors){
//            System.out.println(m);
//        }


        return errors;
    }

    /**
     * more elaborated version of validation
     * @param schemaString
     * @param witness
     * @param version
     * @param id
     * @param validationException
     * @return
     * @throws IOException
     * @throws JsonSchemaException
     */
    public static Set<ValidationMessage> validateStringWitness(String schemaString, String witness, SpecVersion.VersionFlag version,
                                                               String id, LinkedHashMap<String,String> validationException)
    {

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(version);
//        System.out.println(schemaString);
//        JsonSchemaFactory factory = null;
//        try {
//            factory = getJsonSchemaFactory(version);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        ObjectMapper mapper = new ObjectMapper();


        JsonSchema schema = null;
        try {
            schema = factory.getSchema(schemaString);
        }catch (JsonSchemaException e) {
//            e.printStackTrace();
            addValidationException(id,e,validationException);
        }

//        System.out.println(schema);
        JsonNode node = null;
        try {
            node = mapper.readTree(witness);
        } catch (JsonProcessingException e) {
            addValidationException(id,e,validationException);
        }
        Set<ValidationMessage> errors = schema.validate(node);

//        for (ValidationMessage m: errors){
//            System.out.println(m);
//        }


        return errors;
    }







    //*************************************************************************************

    public static void schemaValidation(File file,  BufferedWriter schemaValidation) throws IOException {
        String path = System.getProperty("user.dir")+"/JsonSchema_To_Algebra/metaSchemas/";
        String metaSchema_V4 = Files.readString(Path.of(path+"V4.json"));
        String metaSchema_V6 = Files.readString(Path.of(path+"V6.json"));
        String metaSchema_V7 = Files.readString(Path.of(path+"V7.json"));
        String metaSchema_2019 = Files.readString(Path.of(path+"2019-09.json"));


        String defaultVersion = "";
        String id = getSchemaId(file);
        SpecVersion.VersionFlag version;

        Set<ValidationMessage> errors = null;

        JsonObject schema = null;

        try (Reader reader = new FileReader(file)) {
            try {
                schema = new Gson().fromJson(reader, JsonObject.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                schemaValidation.write(id+",,\n");
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            schemaValidation.write(id+",,\n");
        } catch (IOException e) {
            e.printStackTrace();
            schemaValidation.write(id+",,\n");
        }

        version = getVersionFlag(file.getAbsolutePath());
            
        if(version == SpecVersion.VersionFlag.V4)
            errors = validateStringWitness(metaSchema_V4,schema.toString(),version);
        else if(version == SpecVersion.VersionFlag.V6)
            errors = validateStringWitness(metaSchema_V6,schema.toString(),version);
        else if(version == SpecVersion.VersionFlag.V7)
            errors = validateStringWitness(metaSchema_V7,schema.toString(),version);
        else if(version == SpecVersion.VersionFlag.V201909)
            errors = validateStringWitness(metaSchema_2019,schema.toString(),version);
            

        boolean validOrNo = errors.size()==0;

        if(!schema.toString().contains("draft-04") && !schema.toString().contains("draft-06") &&
                !schema.toString().contains("draft-07") && !schema.toString().contains("2019-09"))
            defaultVersion = "default ";

        schemaValidation.write(id+","+defaultVersion+version+","+validOrNo+"\n");

    }


}