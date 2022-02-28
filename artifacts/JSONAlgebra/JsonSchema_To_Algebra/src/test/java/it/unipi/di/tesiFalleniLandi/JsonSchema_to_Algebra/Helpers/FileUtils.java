package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtils {

    public static String fileToSchema(String path, String fileName) throws FileNotFoundException {

        File file = new File(path + fileName);
        StringBuilder fileContents = new StringBuilder((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }
}
