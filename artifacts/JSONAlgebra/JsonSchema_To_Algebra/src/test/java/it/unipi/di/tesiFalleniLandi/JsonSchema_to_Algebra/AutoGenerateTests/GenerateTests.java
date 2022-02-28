package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.AutoGenerateTests;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * This class is an helper class to generate Unit Tests.
 * It lists all files from directory @contentPath.
 * Then it uses the given template @testTemplate to generate a structure for the Unit Tests.
 * After this it generates the Unit Tests to @targetPath.
 *
 * @contentPath here is /witnessGenTestfiles/jsonSchemaTestSuiteDraft6
 * @targetPath here is GenTestSuiteTest.java
 *
 * @author Luca Escher
 */
public class GenerateTests {

    String contentPath;
    String targetPath;

    public GenerateTests(String contentPath, String targetPath) {
        this.contentPath = contentPath;
        this.targetPath = targetPath;
    }


    public List<String> getFiles() throws IOException {
        List<String> filePaths = new ArrayList<>();

        Files.walk(Paths.get(contentPath))
                .filter(Files::isRegularFile)
                .forEach((path -> filePaths.add(path.toString())));

        return filePaths;
    }

    public List<String> readFiles(List<String> files) {
        List<String> schemas = new ArrayList<>();
        files.listIterator().forEachRemaining((schema) -> {
            try {
                schemas.add(fileToSchema(schema) + "------------------");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return schemas;
    }

    public String fileToSchema(String fileName) throws FileNotFoundException {

        File file = new File(fileName);
        StringBuilder fileContents = new StringBuilder((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }

    /**
     * @param testName represents the name of the  generated Test.
     * @param schema represents the path to the schema, which will be tested.
     * @return String, which will be the layout for the tests.
     */
    public String testTemplate(String testName, String schema) {
        String template = "    @Test\n" +
                "    public void generatedTest" + testName + "() throws Exception {\n" +
                "        String witness = genVal.genWitness(\"" + schema + "\");\n" +
                "        assertEquals(0,genVal.validateWitness(\"" + schema + "\",witness));\n" +
                "    }";

        return template;
    }

    public void generateTests(List<String> schemas) {
        try (FileWriter fw = new FileWriter(targetPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            //Delete last curly bracket and append tests
            RandomAccessFile raf = new RandomAccessFile(targetPath, "rw");
            long length = raf.length();
            System.out.println("File Length=" + raf.length());
            raf.setLength(length - 2);
            System.out.println("File Length=" + raf.length());
            raf.close();

            schemas.listIterator().forEachRemaining((schema) -> {
                //schemaName = number of test + .json
                String schemaName = schema.substring(68);
                //testName = the number of the generatedTest
                String testName = schemaName.substring(0, schemaName.length() - 5);
                out.println();
                out.println(testTemplate(testName, schemaName));
            });
            out.println("}");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *     start this method if you want to generate Unit Tests form @contentPath to @targetPath
     */
    public static void main(String[] args) throws IOException {
        String contentPath = "JsonSchema_To_Algebra/witnessGenTestfiles/jsonSchemaTestSuiteDraft6/";
        String targetPath = "JsonSchema_To_Algebra/src/test/java/it/unipi/di/tesiFalleniLandi/" +
                        "JsonSchema_to_Algebra/GenAlgebra/GenTestSuiteTest.java";

        GenerateTests generate = new GenerateTests(contentPath, targetPath);
        List<String> filePaths = generate.getFiles();
        filePaths.sort(Comparator.comparingInt(s -> Integer.parseInt(s.substring(68, s.length() - 5))));
        generate.generateTests(filePaths);
    }
}
