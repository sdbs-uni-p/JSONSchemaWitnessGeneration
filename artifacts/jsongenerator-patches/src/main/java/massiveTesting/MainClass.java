package massiveTesting;

import com.networknt.schema.ValidationMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class MainClass {

    public static void main(String[] args) throws InterruptedException, IOException {
        long timeout = 1000 * 60 * 180;
        int nbProcs = Runtime.getRuntime().availableProcessors();
        System.out.println(nbProcs);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        ExecutorService writerExec = Executors.newSingleThreadExecutor();

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss");
        Date date = new Date();
        String info = "jsongenerator_"+formatter.format(date)+"_"+timeout+"ms_";
        if (args.length == 0) {
            System.out.println("Usage: <path>");
            return;
        }
        String path = args[0];
        File[] files = new File(path).listFiles();
        System.out.println(files);
        Arrays.sort(files, Comparator.comparing(f -> f.length()));

        File resultsFolder = new File(path+"/results");
        if (!resultsFolder.exists())
            resultsFolder.mkdir();

        BufferedWriter csvFile = Utils.createBufferedWriter(resultsFolder.getPath(),info+"results.csv");
        Utils.setCSVHeader(csvFile);

        BufferedWriter witnessFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"witness.csv");
        witnessFile.write("objectId,witness\n");

        BufferedWriter validationErrorsFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"validation.csv");
        validationErrorsFile.write("objectId,nbErrors,errorsTypes,errorsMessages\n");

        BufferedWriter validationExceptionFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"validationException.csv");
        validationExceptionFile.write("objectId,exceptionName,message\n");

        long start = System.currentTimeMillis();
        System.out.println(start);


        LinkedHashMap<File,GenerationTask> fileTaskMap = new LinkedHashMap<>();


        for (File file : files){
            if (!file.getName().endsWith(".json")) {
                System.out.println("skipping non-json file: " + file.getName()+"\n\n");
                continue;
            }
            GenerationTask task = new GenerationTask(file);
            fileTaskMap.put(file,task);
        }


        List<Future<Boolean>> futures = null;

        try {
            futures = executor.invokeAll(fileTaskMap.values()); // execution without a timeout
//            futures = executor.invokeAll(fileTaskMap.values(),timeout,TimeUnit.MILLISECONDS); // execution with a timeout
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();


        for(int i=0;i<futures.size();i++) {
            Future<Boolean> future = futures.get(i);
            File file = (File) fileTaskMap.keySet().toArray()[i];
            GenerationTask task = fileTaskMap.get(file);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException | CancellationException e) {
                task.resultMap.put(task.currentOperation,e.getClass().getSimpleName());
            }

            LinkedHashMap<String, String> resultMap = task.resultMap;

            LinkedHashMap<String, String> witnessMap = task.witnessMap;

            LinkedHashMap<String, Set<ValidationMessage>> validationErrorsMap = task.errorsMap;

            LinkedHashMap<String,String> validationException = task.validationException;


            Runnable writer = new WriteResults(resultMap,witnessMap,validationErrorsMap,csvFile,witnessFile,
                    validationErrorsFile, task.witnessGenerationSuccess, task.hasValidationErrors,file,
                    validationException, validationExceptionFile);

            writerExec.submit(writer);
        }

        writerExec.shutdown();

        while(!writerExec.isTerminated())
            System.out.print("");




        csvFile.flush();
        csvFile.close();
        witnessFile.flush();
        witnessFile.close();
        validationErrorsFile.flush();
        validationErrorsFile.close();
        validationExceptionFile.flush();
        validationExceptionFile.close();


        long totalTimeInMS = System.currentTimeMillis()-start;
        long mn = (totalTimeInMS / 1000) / 60;
        long s = (totalTimeInMS / 1000) % 60;
        System.out.println("\n\n***************************************************");
        System.out.println("Total time : "+mn+" minutes and "+s+ " seconds");
        System.out.println("***************************************************\n\n");


        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        // Exiting the program when a thread from the previous pool is still running (when stuck in dnf for example)
        // Didn't find any other alternative to stop the thread from running
        for(Thread t : threadSet)
            if(t.getName().contains("pool")) {
//                System.out.println(t + "   " + t.getName() + "   " + t.getPriority() + "   " + t.getThreadGroup().getName());
                System.exit(-1);
            }

    }
}
