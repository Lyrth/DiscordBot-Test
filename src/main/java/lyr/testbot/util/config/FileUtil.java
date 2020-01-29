package lyr.testbot.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lyr.testbot.util.FuncUtil;
import lyr.testbot.util.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class FileUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();  // TODO: Cached files!

    private HashMap<String,Mono<?>> cache = new HashMap<>();

    public static <T> Mono<T> readFileM(String fileName, Class<T> clazz){
        return Mono.using(() -> Files.readAllBytes(Paths.get(fileName)), Mono::just, FuncUtil::noop)
            .map(String::new)
            .doOnNext($ -> Log.debugFormat("Reading from %s...",fileName))
            .map(json -> gson.fromJson(json,clazz))
            .onErrorResume($ -> Mono.empty());
    }

    public static <T> Mono<T> createFileM(String fileName, T t){
        return Mono.using(
            () -> new PrintStream(fileName),
            ps -> Mono.fromRunnable(() -> ps.print(gson.toJson(t))).thenReturn(t),
            PrintStream::close);
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
                return 0;
            })
            .then(createFileM(fileName,t));
    }
    public static Flux<String> listFilesF(String path){
        return Mono.fromCallable(() -> new File(path).list((cur, name) -> new File(cur, name).isFile()))
            .map(Arrays::asList)
            .flatMapIterable(FuncUtil::it);
    }

    public static Flux<String> listDirsF(String path){
        return Mono.fromCallable(() -> new File(path).list((cur, name) -> new File(cur, name).isDirectory()))
            .map(Arrays::asList)
            .flatMapIterable(FuncUtil::it)
            .doOnError($ -> Log.error(">>> listDirs failed!"))
            .doOnError(Throwable::printStackTrace);
    }

    public static Mono<Boolean> isDirectory(String path){
        return Mono.fromCallable(() -> new File(path).isDirectory())
            .doOnError($ -> Log.error(">>> isDirectory failed!"))
            .doOnError(Throwable::printStackTrace)
            .onErrorReturn(false);
    }

    public static Mono<Boolean> createDir(String path){
        return Mono.fromCallable(() -> new File(path).mkdirs())
            .doOnError($ -> Log.error(">>> createDir failed!"))
            .doOnError(Throwable::printStackTrace)
            .onErrorReturn(false);
    }
}
