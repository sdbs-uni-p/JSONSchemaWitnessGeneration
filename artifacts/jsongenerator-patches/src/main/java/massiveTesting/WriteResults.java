package massiveTesting;

import com.networknt.schema.ValidationMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

public class WriteResults implements  Runnable{

    LinkedHashMap<String, String> resultMap;
    LinkedHashMap<String, String> witnessMap;
    LinkedHashMap<String, Set<ValidationMessage>> validationErrorsMap;
    LinkedHashMap<String, String> validationException;
    BufferedWriter csvFile;
    BufferedWriter witnessFile;
    BufferedWriter validationErrorsFile;
    BufferedWriter validationExceptionFile;
    Boolean witnessGenerationSuccess;
    Boolean hasValidationErrors;
    File file;


    public WriteResults(LinkedHashMap<String, String> resultMap, LinkedHashMap<String, String> witnessMap,
                        LinkedHashMap<String, Set<ValidationMessage>> validationErrorsMap,
                        BufferedWriter csvFile,  BufferedWriter witnessFile, BufferedWriter validationErrorsFile,
                        Boolean witnessGenerationSuccess, Boolean hasValidationErrors, File file,
                        LinkedHashMap<String, String> validationException,BufferedWriter validationExceptionFile) {

        this.resultMap = resultMap;
        this.witnessMap = witnessMap;
        this.validationErrorsMap = validationErrorsMap;
        this.csvFile = csvFile;
        this.witnessFile = witnessFile;
        this.validationErrorsFile = validationErrorsFile;
        this.witnessGenerationSuccess = witnessGenerationSuccess;
        this.hasValidationErrors = hasValidationErrors;
        this.file = file;
        this.validationException = validationException;
        this.validationExceptionFile = validationExceptionFile;
    }





    @Override
    public void run() {

        try {
            Utils.writeResultsOnCSV(this.resultMap,this.csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.resultMap.clear();

        if(this.witnessGenerationSuccess) {
            try {
                Utils.writeOnWitnessFile(this.witnessMap, this.witnessFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.witnessMap.clear();


        if(this.hasValidationErrors) {
            try {
                Utils.writeOnErrorsFile(this.validationErrorsMap, this.validationErrorsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.validationErrorsMap.clear();


        if(this.validationException.size()>0) {
            try {
                Utils.writeOnValidationException(this.validationException, this.validationExceptionFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.validationException.clear();

    }
}
