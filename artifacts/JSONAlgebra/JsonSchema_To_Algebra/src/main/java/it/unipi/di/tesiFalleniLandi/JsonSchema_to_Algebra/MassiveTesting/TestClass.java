package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.MassiveTesting;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClass {

    public static void main(String[] args) throws IOException {
        String path = "/home/lyes/Bureau/dataset/o34383.json";
        File file = new File(path);
        JsonObject schema = null;

        try (Reader reader = new FileReader(file)) {
            try {
                schema = new Gson().fromJson(reader, JsonObject.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("1");
        System.out.println(Utils.getVersionFlag(file.getAbsolutePath()));
    }
}
