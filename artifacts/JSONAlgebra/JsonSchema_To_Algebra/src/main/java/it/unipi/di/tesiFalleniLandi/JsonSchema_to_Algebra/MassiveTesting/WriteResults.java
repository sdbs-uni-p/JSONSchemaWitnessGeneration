package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting;

import com.networknt.schema.ValidationMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.LinkedHashMap;
import java.util.Set;

public class WriteResults implements  Runnable {
    LinkedHashMap<String, String> resultMap;
    LinkedHashMap<String, String> witnessMap;
    LinkedHashMap<String, Set<ValidationMessage>> validationErrorsMap;
    LinkedHashMap<String, String> exceptionMap;
    LinkedHashMap<String, String> validationException;
    LinkedHashMap<String, String> sizeMap;
    BufferedWriter csvFile;
    BufferedWriter witnessFile;
    BufferedWriter validationErrorsFile;
    BufferedWriter exceptionFile;
    BufferedWriter validationExceptionFile;
    BufferedWriter sizeFile;
    Boolean witnessGenerationSuccess;
    Boolean hasValidationErrors;
    Boolean hasException;
    File file;
    int i;

    public WriteResults(LinkedHashMap<String, String> resultMap, LinkedHashMap<String, String> witnessMap,
                        LinkedHashMap<String, Set<ValidationMessage>> validationErrorsMap, LinkedHashMap<String, String> exceptionMap,
                        LinkedHashMap<String, String> sizeMap, BufferedWriter csvFile,  BufferedWriter witnessFile,
                        BufferedWriter validationErrorsFile, BufferedWriter exceptionFile, BufferedWriter sizeFile,
                        Boolean witnessGenerationSuccess, Boolean hasValidationErrors, Boolean hasException, File file,
                        int i,LinkedHashMap<String, String> validationException,BufferedWriter validationExceptionFile) {

        this.resultMap = resultMap;
        this.witnessMap = witnessMap;
        this.validationErrorsMap = validationErrorsMap;
        this.exceptionMap = exceptionMap;
        this.sizeMap = sizeMap;
        this.csvFile = csvFile;
        this.witnessFile = witnessFile;
        this.validationErrorsFile = validationErrorsFile;
        this.exceptionFile = exceptionFile;
        this.sizeFile = sizeFile;
        this.witnessGenerationSuccess = witnessGenerationSuccess;
        this.hasValidationErrors = hasValidationErrors;
        this.hasException = hasException;
        this.file = file;
        this.i = i;
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

        if(this.hasException) {
            try {
                Utils.writeOnExceptionsFile(this.exceptionMap, this.exceptionFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.exceptionMap.clear();

        if(this.validationException.size()>0) {
            try {
                Utils.writeOnValidationException(this.validationException, this.validationExceptionFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.validationException.clear();

        try {
            Utils.writeOnSizeFile(this.sizeMap, this.sizeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sizeMap.clear();

    }
}