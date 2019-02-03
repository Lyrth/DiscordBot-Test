package lyr.testbot.util;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class StringUtil {

    public static String trunc(String str, int maxWidth){
        return StringUtils.abbreviateMiddle(str,"...",maxWidth);
    }

    public static String getEmoji(String name){
        return Optional.ofNullable(EmojiManager.getForAlias(name)).map(Emoji::getUnicode).orElse(name);
    }
}
