package lyr.testbot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);
    private static final int LEN = 47; // Class name length in Debug logs.

    public static void screamA(Object... o){
        info("AAAAA");
    }
    public static void screamB(Object... o){
        info("BBBBB");
    }
    public static void screamC(Object... o){
        info("CCCCC");
    }

    public static void print(String str) {
        logger.info(str);
    }

    public static void printf(String str, Object... o) {
        logger.info(String.format(str,o));
    }

    /**
     Info-level
     */
    public static void info(Object o){
        logger.info(o.toString());
    }

    public static void info(Iterable<Object> arr){
        for (Object val : arr)
            logger.info(val.toString());
    }

    public static void info(Object... oo){
        for (Object o : oo)
            logger.info(o.toString());
    }

    public static void infoFormat(String str, Object... o){
        logger.info(String.format(str,o));
    }

    private static Exception stackTrace(Exception e){
        StackTraceElement element = e.getStackTrace()[1];
        String formatted = String.format("at %s", element.getClassName());
        String detail = String.format("%s(%s:%d)",
            element.getMethodName(),
            element.getFileName(),
            element.getLineNumber()
        );
        return new Exception(StringUtil.truncLast(formatted,LEN-detail.length()) + detail);
    }
    
    
    /**
     Debug-level
     */
    public static void debug(Object o){
        logger.debug(o.toString(),stackTrace(new Exception()));
    }

    public static void debug(Iterable<Object> arr){
        for (Object val : arr)
            logger.debug(val.toString(),stackTrace(new Exception()));
    }

    public static void debug(Object... oo){
        for (Object o : oo)
            logger.debug(o.toString(),stackTrace(new Exception()));
    }

    public static void debugFormat(String str, Object... o){
        logger.debug(String.format(str,o),stackTrace(new Exception()));
    }


    /**
     Trace-level
     */
    public static void trace(Object o){
        logger.trace(o.toString(),stackTrace(new Exception()));
    }

    public static void trace(Iterable<Object> arr){
        for (Object val : arr)
            logger.trace(val.toString(),stackTrace(new Exception()));
    }

    public static void trace(Object... oo){
        for (Object o : oo)
            logger.trace(o.toString(),stackTrace(new Exception()));
    }

    public static void traceFormat(String str, Object... o){
        logger.trace(String.format(str,o),stackTrace(new Exception()));
    }


    /**
     Warn-level
     */
    public static void warn(Object o){
        logger.warn(o.toString(),stackTrace(new Exception()));
    }

    public static void warn(Iterable<Object> arr){
        for (Object val : arr)
            logger.warn(val.toString(),stackTrace(new Exception()));
    }

    public static void warn(Object... oo){
        for (Object o : oo)
            logger.warn(o.toString(),stackTrace(new Exception()));
    }

    public static void warnFormat(String str, Object... o){
        logger.warn(String.format(str,o),stackTrace(new Exception()));
    }


    /**
     Error-level
     */
    public static void error(Object o){
        logger.error(o.toString(),stackTrace(new Exception()));
    }

    public static void error(Iterable<Object> arr){
        for (Object val : arr)
            logger.error(val.toString(),stackTrace(new Exception()));
    }

    public static void error(Object... oo){
        for (Object o : oo)
            logger.error(o.toString(),stackTrace(new Exception()));
    }

    public static void errorFormat(String str, Object... o){
        logger.error(String.format(str,o),stackTrace(new Exception()));
    }

}
