package net.ddns.lyr.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static String trunc(String str, int maxWidth){
        return StringUtils.abbreviateMiddle(str,"...",maxWidth);
    }

}
