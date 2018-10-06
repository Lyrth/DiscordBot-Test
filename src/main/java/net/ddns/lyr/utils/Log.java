package net.ddns.lyr.utils;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    public static void print(String str) {
        logger.info(str);
    }

    public static void printf(String str, Object... o) {
        logger.info(String.format(str,o));
    }

    /**
     Info-level
     */
    public static void log(Object o){
        logger.info(o.toString());
    }

    public static void log(Iterable<Object> arr){
        for (Object val : arr)
            logger.info(val.toString());
    }

    public static void log(Object... oo){
        for (Object o : oo)
            logger.info(o.toString());
    }

    public static void logf(String str, Object... o){
        logger.info(String.format(str,o));
    }


    /**
     Debug-level
     */
    public static void logDebug(Object o){
        logger.debug(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logDebug(Iterable<Object> arr){
        for (Object val : arr)
            logger.debug(val.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logDebug(Object... oo){
        for (Object o : oo)
            logger.debug(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logfDebug(String str, Object... o){
        logger.debug(String.format(str,o),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }


    /**
     Trace-level
     */
    public static void logTrace(Object o){
        logger.trace(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logTrace(Iterable<Object> arr){
        for (Object val : arr)
            logger.trace(val.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logTrace(Object... oo){
        for (Object o : oo)
            logger.trace(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logfTrace(String str, Object... o){
        logger.trace(String.format(str,o),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }


    /**
     Warn-level
     */
    public static void logWarn(Object o){
        logger.warn(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logWarn(Iterable<Object> arr){
        for (Object val : arr)
            logger.warn(val.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logWarn(Object... oo){
        for (Object o : oo)
            logger.warn(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logfWarn(String str, Object... o){
        logger.warn(String.format(str,o),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }


    /**
     Error-level
     */
    public static void logError(Object o){
        logger.error(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logError(Iterable<Object> arr){
        for (Object val : arr)
            logger.error(val.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logError(Object... oo){
        for (Object o : oo)
            logger.error(o.toString(),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

    public static void logfError(String str, Object... o){
        logger.error(String.format(str,o),new Exception(new Exception().getStackTrace()[1].getClassName().trim()));
    }

}
