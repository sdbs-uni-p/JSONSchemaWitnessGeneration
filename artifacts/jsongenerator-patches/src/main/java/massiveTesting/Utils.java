package massiveTesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.networknt.schema.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    final static String objectId = "objectId";
    final static String inSize = "inSize";
    final static String totalTime = "totalTime";
    final static String loadSchema = "loadSchema";
    final static String initGenerator = "initGenerator";
    final static String generation = "generation";
    final static String genSuccess = "genSuccess";
    final static String valid = "valid";

    final static LinkedList<String> ops = new LinkedList<>(Arrays.asList(objectId,
            inSize, totalTime, loadSchema, initGenerator, generation, genSuccess, valid));

    final static String exceptionName = "exceptionName";
    final static String message = "message";


    private static Pattern p4 = Pattern.compile("/draft-04/");
    private static Pattern p6 = Pattern.compile("/draft-06/");
    private static Pattern p7 = Pattern.compile("/draft-07/");
    private static Pattern regexXbool = Pattern.compile("(\"exclusiveMinimum\"|\"exclusiveMaximum\"):(false|False|true|True)");


    //*****************************************************************************************************************

    public static String getSchemaId(File file){
        return file.getName().replaceAll(".json","");
    }


    static void initMaps(LinkedHashMap<String, String> resultMap){
        for(String op: ops) {
            resultMap.put(op,"");
        }
    }



    static void addValidationException(String id, Exception e, LinkedHashMap<String, String> validationException) {
        validationException.put(objectId,id);
        validationException.put(exceptionName,e.getClass().getSimpleName());
        validationException.put(message, StringEscapeUtils.escapeCsv(e.getMessage()));
    }


    //****************************************************************************************************************
    public static SpecVersion.VersionFlag getVersionFlag(String pathToFile) throws IOException {
        SpecVersion.VersionFlag version = SpecVersion.VersionFlag.V201909;


        String s = Files.readString(Path.of(pathToFile));
        s = s.replace(" ","");
        System.out.println(s);
        if(p4.matcher(s).find() || regexXbool.matcher(s).find())
            version = SpecVersion.VersionFlag.V4;
        else if(p6.matcher(s).find())
            version = SpecVersion.VersionFlag.V6;
        else if(p7.matcher(s).find())
            version = SpecVersion.VersionFlag.V7;

        return version;
    }

    public static Set<ValidationMessage> validateStringWitness(String schemaString, String witness, SpecVersion.VersionFlag version,
                                                               String id, LinkedHashMap<String,String> validationException)
    {

        System.out.println(version);
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
            System.out.println(id+" ___Here ---> "+e.getMessage());
            addValidationException(id,e,validationException);
        }

//        System.out.println(schema);
        JsonNode node = null;
        try {
            node = mapper.readTree(witness);
        } catch (JsonProcessingException e) {
            System.out.println(id+" ---> "+e.getMessage());
            addValidationException(id,e,validationException);
        }
        Set<ValidationMessage> errors = schema.validate(node);

//        for (ValidationMessage m: errors){
//            System.out.println(m);
//        }


        return errors;
    }

    //*****************************************************************************************************************
    static void setCSVHeader(BufferedWriter res){
        try {
            String header = String.join(",",ops);
            res.write(header+"\n");
        }catch (IOException e){
            e.printStackTrace();
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



    //*****************************************************************************************************************


    static void writeResultsOnCSV(LinkedHashMap<String, String> resultMap, BufferedWriter csvFile) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append(resultMap.get(objectId))
                .append(",")
                .append(resultMap.get(inSize))
                .append(",")
                .append(resultMap.get(totalTime))
                .append(",")
                .append(resultMap.get(loadSchema))
                .append(",")
                .append(resultMap.get(initGenerator))
                .append(",")
                .append(resultMap.get(generation))
                .append(",")
                .append(resultMap.get(genSuccess))
                .append(",")
                .append(resultMap.get(valid))
                .append("\n");

        csvFile.write(sb.toString());
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
}
