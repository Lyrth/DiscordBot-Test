package lyr.testbot.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lyr.testbot.util.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class FileUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();  // TODO: Cached files!

    private HashMap<String,Mono<?>> cache = new HashMap<>();

    public static <T> T readFile(String fileName, Class<T> clazz){
        try {
            String json = new String(Files.readAllBytes(Paths.get(fileName)));
            Log.debugFormat("Reading from %s...",fileName);
            return gson.fromJson(json,clazz);
        } catch(Exception e) {
            return null;
        }
    }

    public static <T> Mono<T> readFileM(String fileName, Class<T> clazz){
        return Mono.using(() -> Files.readAllBytes(Paths.get(fileName)), Mono::just, $ -> {})
            .map(String::new)
            .doOnNext($ -> Log.debugFormat("Reading from %s...",fileName))
            .map(json -> gson.fromJson(json,clazz))
            .onErrorResume(err -> Mono.empty());
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

    public static <T> Mono<T> createFileM(String fileName, T t){
        return Mono.using(
            () -> new PrintStream(fileName),
            ps -> Mono.fromRunnable(() -> ps.print(gson.toJson(t))).thenReturn(t),
            PrintStream::close);
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

    public static <T> Mono<T> updateFileM(String fileName, T t){
        return Mono.fromCallable(() -> {
                File current = new File(fileName);
                File backup  = new File(fileName + ".bak");
                if (backup.exists())
                    if (!backup.delete())
                        Log.warnFormat(">> Cannot delete backup file %s.", fileName);
                    else if (!current.renameTo(backup))
                        Log.warnFormat(">> Cannot rename file %s. Overwriting.", fileName);
                if (current.exists() && !current.delete()) {
                    Log.errorFormat(">>> Cannot modify file %s.", fileName);
                    throw new IOException("Cannot modify file " + fileName);
                }
                return Mono.empty();
            })
            .then(createFileM(fileName,t));
    }

    public static String[] listFiles(String path){
        try {
            return new File(path).list(
                (current, name) -> new File(current, name).isFile());
        } catch(Exception e){
            return null;
        }
    }

    public static Flux<String> listFilesF(String path){
        return Mono.fromCallable(() -> new File(path).list((cur, name) -> new File(cur, name).isFile()))
            .map(Arrays::asList)
            .flatMapIterable(arr -> arr);
    }

    public static Mono<Boolean> isFile(String path){
        return Mono.fromCallable(() -> new File(path).isFile())
            .doOnError(err -> Log.error(">>> isFile failed!"))
            .doOnError(Throwable::printStackTrace)
            .onErrorReturn(false);
    }

    public static String[] listDirs(String path){
        try {
            return new File(path).list(
                (current, name) -> new File(current, name).isDirectory());
        } catch(Exception e){
            return null;
        }
    }

    public static Flux<String> listDirsF(String path){
        return Mono.fromCallable(() -> new File(path).list((cur, name) -> new File(cur, name).isDirectory()))
            .map(Arrays::asList)
            .flatMapIterable(arr -> arr)
            .doOnError(err -> Log.error(">>> listDirs failed!"))
            .doOnError(Throwable::printStackTrace);
    }

    public static Mono<Boolean> isDirectory(String path){
        return Mono.fromCallable(() -> new File(path).isDirectory())
            .doOnError(err -> Log.error(">>> isDirectory failed!"))
            .doOnError(Throwable::printStackTrace)
            .onErrorReturn(false);
    }

    public static Mono<Boolean> createDir(String path){
        return Mono.fromCallable(() -> new File(path).mkdirs())
            .doOnError(err -> Log.error(">>> createDir failed!"))
            .doOnError(Throwable::printStackTrace)
            .onErrorReturn(false);
    }
}
