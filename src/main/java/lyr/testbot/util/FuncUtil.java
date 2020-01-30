package lyr.testbot.util;

public class FuncUtil {
    public static <T> T it(T t){  // TODO: Probably bugged with JDK 8, move to JDK 9.
        return t;
    }

    public static <T> T identity(T t){
        return t;
    }

    public static void noop(){
        // no-op
    }

    public static <T> void noop(T t){
        // no-op
    }

    public static <T,U> void noop(T t, U u){
        // no-op
    }

    public static <T,U,V> void noop(T t, U u, V v){
        // no-op
    }

    public static boolean negate(boolean b){
        return !b;
    }
}
