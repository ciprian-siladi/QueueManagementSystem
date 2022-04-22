package queuemanagement.controller;

import java.io.FileWriter;
import java.io.IOException;

public class FileService {
    private static String out;
    private static FileWriter writer;

    /**
     * Function to set the output file
     * @param file the file name to be created
     */
    public static void setFile(String file) {
        out = file;
        try {
            writer = new FileWriter(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String s){
        try {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeFile(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
