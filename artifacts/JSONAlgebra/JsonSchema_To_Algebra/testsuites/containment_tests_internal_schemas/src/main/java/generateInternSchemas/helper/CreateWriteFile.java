package generateInternSchemas.helper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Luca Escher
 */
public class CreateWriteFile {

    /**
     * Create a file at your desired location.
     *
     * @param pathname
     * @param fileName
     * @param type
     */
    public void createFile(String pathname, String fileName, String type) {
        try {
            File myObj = new File( pathname + fileName + "." + type);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * This method write either to an existing file or if the file does not already exist it will create
     * a file containing the entered message.
     *
     * @param pathname
     * @param filename
     * @param text
     */
    public void writeToFile(String pathname, String filename, String text){
        try {
            FileWriter myWriter = new FileWriter(pathname + filename);
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Get all Files from which you want to receive your data.
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public List<String> getFiles(String filePath) throws IOException {
        List<String> filePaths = new ArrayList<>();

        Files.walk(Paths.get(filePath))
                .filter(Files::isRegularFile)
                .forEach((path -> filePaths.add(path.toString())));

        return filePaths;
    }

    /**
     * Convert data from file into a string schema.
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static String fileToSchema(String fileName) {

        File file = new File(fileName);
        StringBuilder fileContents = new StringBuilder((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            try {
                var check = fileName.substring(0, 54);
                if (check.equals(PathUtils.normalized_path)){
                    CreateWriteFile.writeResultToFile(PathUtils.malformed_path, "Normalization Error occurred --> " + fileName);
                }
            } catch (Exception ex){
                CreateWriteFile.writeResultToFile(PathUtils.malformed_path, "File not found. Path was --> " + fileName);
            }
            CreateWriteFile.writeResultToFile(PathUtils.malformed_path, "File not found. Path was --> " + fileName);
        }
        return fileContents.toString();
    }

    /**
     * used to delete all files in one dir.
     *
     * @param dir
     */
    public static void deleteDirectoryData(File dir) {
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    /**
     * Method creates a dir.
     *
     * @param path
     * @param name
     */
    public void mkDir(String path, String name){
        File theDir = new File(path + "/" +name);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    /**
     * This method is writing and appending your input message to a specified file.
     *
     * @param targetPath
     * @param message
     */
    public static void writeResultToFile(String targetPath, String message) {
        try (FileWriter fw = new FileWriter(targetPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            RandomAccessFile raf = new RandomAccessFile(targetPath, "rw");
            long length = raf.length();
            System.out.println("File Length=" + raf.length());
            raf.setLength(length);
            System.out.println("File Length=" + raf.length());
            raf.close();

            out.println(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method deletes all data from the directories satisfiable and unsatisfiable.
     * @param args
     */
    public static void main(String[] args) {
        CreateWriteFile createWriteFile = new CreateWriteFile();
        File dir1 = new File(PathUtils.satisfiable_path + PathUtils.DRAFT3);
        File dir2 = new File(PathUtils.satisfiable_path + PathUtils.DRAFT4);
        File dir3 = new File(PathUtils.satisfiable_path + PathUtils.DRAFT6);
        File dir4 = new File(PathUtils.satisfiable_path + PathUtils.DRAFT7);
        File dir5 = new File(PathUtils.satisfiable_path + PathUtils.DRAFT2020_12);
        File dir6 = new File(PathUtils.satisfiable_path + PathUtils.DRAFT2019_09);

        File dir1a = new File(PathUtils.unsatisfiable_path + PathUtils.DRAFT3);
        File dir2a = new File(PathUtils.unsatisfiable_path + PathUtils.DRAFT4);
        File dir3a = new File(PathUtils.unsatisfiable_path + PathUtils.DRAFT6);
        File dir4a = new File(PathUtils.unsatisfiable_path + PathUtils.DRAFT7);
        File dir5a = new File(PathUtils.unsatisfiable_path + PathUtils.DRAFT2020_12);
        File dir6a = new File(PathUtils.unsatisfiable_path + PathUtils.DRAFT2019_09);

        deleteDirectoryData(dir1);
        deleteDirectoryData(dir2);
        deleteDirectoryData(dir3);
        deleteDirectoryData(dir4);
        deleteDirectoryData(dir5);
        deleteDirectoryData(dir6);

        deleteDirectoryData(dir1a);
        deleteDirectoryData(dir2a);
        deleteDirectoryData(dir3a);
        deleteDirectoryData(dir4a);
        deleteDirectoryData(dir5a);
        deleteDirectoryData(dir6a);
    }
}
