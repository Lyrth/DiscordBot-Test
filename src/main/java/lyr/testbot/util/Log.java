package lyr.testbot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);
    private static final int LEN = 27; // Class name length in Debug logs.

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


    /**
     Debug-level
     */
    public static void debug(Object o){
        logger.debug(o.toString(),
            new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void debug(Iterable<Object> arr){
        for (Object val : arr)
            logger.debug(val.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void debug(Object... oo){
        for (Object o : oo)
            logger.debug(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void debugFormat(String str, Object... o){
        logger.debug(String.format(str,o),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }


    /**
     Trace-level
     */
    public static void trace(Object o){
        logger.trace(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void trace(Iterable<Object> arr){
        for (Object val : arr)
            logger.trace(val.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void trace(Object... oo){
        for (Object o : oo)
            logger.trace(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void traceFormat(String str, Object... o){
        logger.trace(String.format(str,o),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }


    /**
     Warn-level
     */
    public static void warn(Object o){
        logger.warn(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void warn(Iterable<Object> arr){
        for (Object val : arr)
            logger.warn(val.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void warn(Object... oo){
        for (Object o : oo)
            logger.warn(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void warnFormat(String str, Object... o){
        logger.warn(String.format(str,o),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }


    /**
     Error-level
     */
    public static void error(Object o){
        logger.error(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void error(Iterable<Object> arr){
        for (Object val : arr)
            logger.error(val.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void error(Object... oo){
        for (Object o : oo)
            logger.error(o.toString(),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

    public static void errorFormat(String str, Object... o){
        logger.error(String.format(str,o),new Exception(
                StringUtil.trunc(new Exception().getStackTrace()[1].getClassName().trim(),LEN)));
    }

}
