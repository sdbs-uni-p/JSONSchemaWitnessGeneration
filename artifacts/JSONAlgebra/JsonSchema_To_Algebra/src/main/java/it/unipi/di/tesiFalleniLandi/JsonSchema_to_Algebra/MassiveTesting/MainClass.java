package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting;


import com.networknt.schema.ValidationMessage;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


public class MainClass {





    public static void main(String[] args) throws InterruptedException, IOException {


        // Get the git version
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("git rev-parse HEAD");
        String revision;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        )) {
            revision = reader.readLine();
        }

        // Get the machine's name
        String systemName = "";
        try {
            systemName = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception E) {
            System.err.println(E.getMessage());
        }



        Utils.clean = true; // comment if we are not executing on the files cleaned by Giorgio


//        int nbProcs = Runtime.getRuntime().availableProcessors();
//        System.out.println(nbProcs);




        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss");
        Date date = new Date();


        if (args.length < 3) {
            System.out.println("Usage: <path>");
            return;
        }

        String path = args[0];
        File[] files = new File(path).listFiles();
        Arrays. sort(files, Comparator.comparing(f -> f.length()));

        int nbProcs = Integer.parseInt(args[1]);


        String info = formatter.format(date)+"_";

        boolean withTimout = Boolean.valueOf(args[2]);
        long timeout = 0;
        if (withTimout) {
            timeout = Long.parseLong(args[3]);
            info = info+timeout+"ms_";
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(nbProcs,Integer.MAX_VALUE,1,TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        ExecutorService writerExec = Executors.newSingleThreadExecutor();


        File resultsFolder = new File(path+"/results");
        if (!resultsFolder.exists())
            resultsFolder.mkdir();


//        BufferedWriter schemaValidation = Utils.createBufferedWriter(resultsFolder.getPath(),"schemaValidation.csv");
//        schemaValidation.write("objectId,draftVersion,valid\n");
//        for (File file : files){
//            if (!file.getName().endsWith(".json")) {
//                System.out.println("skipping non-json file: " + file.getName()+"\n\n");
//                continue;
//            }
//            try {
//                System.out.println(file.getName() +" ----> "+file.length());
//                Utils.schemaValidation(file,schemaValidation);
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//        schemaValidation.flush();
//        schemaValidation.close();


//        File processedFilesFolder = new File(path+"/processedFiles");
//        if (!processedFilesFolder.exists())
//            processedFilesFolder.mkdir();


//        File unprocessedFilesFolder = new File(path+"/unprocessedFiles");
//        if (!unprocessedFilesFolder.exists())
//            unprocessedFilesFolder.mkdir();



        BufferedWriter csvFile = Utils.createBufferedWriter(resultsFolder.getPath(),info+"results.csv");
        Utils.setCSVHeader(csvFile);

        BufferedWriter witnessFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"witness.csv");
        witnessFile.write("objectId,witness\n");

        BufferedWriter validationErrorsFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"validation.csv");
        validationErrorsFile.write("objectId,nbErrors,errorsTypes,errorsMessages\n");

        BufferedWriter exceptionFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"exception.csv");
        exceptionFile.write("objectId,operation,exceptionName,message,stackSize,stackTrace(class:method:line)\n");

        BufferedWriter validationExceptionFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"validationException.csv");
        validationExceptionFile.write("objectId,exceptionName,message\n");

        BufferedWriter sizeFile = Utils.createBufferedWriter(resultsFolder.getPath(), info+"size.csv");
        sizeFile.write("objectId,inSize,2Full,2Witness,notElim,merge1,groupize,separation,expansion," +
                //"merge2,"
                "preparation,arrPrep,merge3,genEnv,witness\n");




        long start = System.currentTimeMillis();
        System.out.println(start);

        LinkedHashMap<File,GenerateWitnessTaskV3> fileTaskMap = new LinkedHashMap<>();


        for (File file : files){
            if (!file.getName().endsWith(".json")) {
                System.out.println("skipping non-json file: " + file.getName()+"\n\n");
                continue;
            }
            GenerateWitnessTaskV3 task = new GenerateWitnessTaskV3(file);
            fileTaskMap.put(file,task);
        }

        List<Future<Boolean>> futures = null;

        try {
            if (withTimout)
                futures = executor.invokeAll(fileTaskMap.values(),timeout,TimeUnit.MILLISECONDS); // execution with a timeout
            else
                futures = executor.invokeAll(fileTaskMap.values()); // execution without a timeout

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();




        for(int i=0;i<futures.size();i++) {
//            Boolean gotResult = null;
            Future<Boolean> future = futures.get(i);
            File file = (File) fileTaskMap.keySet().toArray()[i];
            GenerateWitnessTaskV3 task = fileTaskMap.get(file);
            if (i==0) {
                task.getResultMap().put(Utils.dateAndTime,formatter.format(date));
                task.getResultMap().put(Utils.timeout,String.valueOf(timeout));
                task.getResultMap().put(Utils.git,revision);
                task.getResultMap().put(Utils.machine,systemName);
            }
            try {
//                gotResult = future.get();
                future.get();
            } catch (InterruptedException | ExecutionException | CancellationException e) {
//                if(task.processed)
//                    gotResult=true;
                task.getResultMap().put(task.getCurrentOperation(),e.getClass().getSimpleName()); // Add to results the operation and the exception happened during this operation
                task.hasException = true;
                Utils.addExceptionInformation(task.id,e,task.exceptionMap,task.currentOperation);
                task.getResultMap().put(Utils.cancelledAt,task.currentOperation);
            }

            LinkedHashMap<String, String> resultMap = task.getResultMap();


            LinkedHashMap<String, String> witnessMap = task.getWitnessMap();

            LinkedHashMap<String, Set<ValidationMessage>> validationErrorsMap = task.getErrorsMap();

            LinkedHashMap<String, String> exceptionMap = task.getExceptionMap();

            LinkedHashMap<String,String> validationException = task.getValidationException();

            LinkedHashMap<String,String> sizeMap = task.getSizeMap();

            Runnable writer = new WriteResults(resultMap,witnessMap,validationErrorsMap,exceptionMap,sizeMap,
                    csvFile,witnessFile,validationErrorsFile,exceptionFile,sizeFile,task.witnessGenerationSuccess,
                    task.hasValidationErrors(), task.hasException,file,i, validationException, validationExceptionFile);

            writerExec.submit(writer);

//            if(gotResult!=null)
//                Files.move(Path.of(file.getPath()), Path.of(processedFilesFolder + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
//            else
//                if(witnessMap.get(Utils.objectId)!=null && !witnessMap.get(Utils.objectId).equals(""))
//                    Files.move(Path.of(file.getPath()), Path.of(unprocessedFilesFolder + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);

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
        exceptionFile.flush();
        exceptionFile.close();
        validationExceptionFile.flush();
        validationExceptionFile.close();
        sizeFile.flush();
        sizeFile.close();




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


//
    }



}
