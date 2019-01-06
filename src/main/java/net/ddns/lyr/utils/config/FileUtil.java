package net.ddns.lyr.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ddns.lyr.utils.Log;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T readFile(String fileName, Class<T> clazz){
        try {
            String json = new String(Files.readAllBytes(Paths.get(fileName)));
            Log.logfDebug("> Reading from %s...",fileName);
            return gson.fromJson(json,clazz);
        } catch(Exception e) {
            return null;
        }
    }

    public static <T> T readFile(String fileName, Type type){
        try {
            String json = new String(Files.readAllBytes(Paths.get(fileName)));
            Log.logfDebug("> Reading from %s...",fileName);
            return gson.fromJson(json,type);
        } catch(Exception e) {
            return null;
        }
    }

    public static <T> boolean createFile(String fileName, T t){
        String json = gson.toJson(t);
        try(PrintStream ps = new PrintStream(fileName)) {
            ps.print(json);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    /*
        0: All OK
        1: Cannot delete .bak file.
        2: Cannot rename file, overwriting.
        4: Cannot modify file. [ERR]
        8: Cannot create file. [ERR]
     */
    public static <T> int updateFile(String fileName, T t){
        int err = 0;
        File current = new File(fileName);
        File backup  = new File(fileName + ".bak");
        if (backup.exists()) if (!backup.delete()) err = err + 1;
        else if (!current.renameTo(backup)) err = err + 2;
        if (current.exists() && !current.delete()) {
            err = err + 4;
            return err;
        }
        String json = gson.toJson(t);
        try(PrintStream ps = new PrintStream(fileName)) {
            ps.print(json);
        } catch(FileNotFoundException ee){
            err = err + 8;
        }
        return err;
    }

    public static String[] listFiles(String path){
        try {
            return new File(path).list(
                (current, name) -> new File(current, name).isFile());
        } catch(Exception e){
            return null;
        }
    }

    public static boolean isFile(String path){
        return new File(path).isFile();
    }

    public static String[] listDirs(String path){
        try {
            return new File(path).list(
                (current, name) -> new File(current, name).isDirectory());
        } catch(Exception e){
            return null;
        }
    }

    public static boolean isDirectory(String path){
        return new File(path).isDirectory();
    }

    public static boolean createDir(String path){
        return new File(path).mkdirs();
    }
}
