package lyr.testbot.util;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import discord4j.core.object.reaction.ReactionEmoji;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class StringUtil {

    public static String trunc(String str, int maxWidth){
        return StringUtils.abbreviateMiddle(str,"...",maxWidth);
    }

    public static String truncLast(String str, int maxWidth){
        return StringUtils.abbreviate(str, maxWidth);
    }

    public static String getUnicodeEmoji(String name){
        return Optional.ofNullable(EmojiManager.getForAlias(name)).map(Emoji::getUnicode).orElse(name);
    }

    public static ReactionEmoji getReactionEmoji(String name){
        if (name.matches("<:\\w+:\\d+>")){                      //non animated
            long id = Long.parseLong(name.replaceFirst("<:\\w+:(\\d+)>","$1"));  // This shouldn't error
            String emoji = name.replaceFirst("<:(\\w+):\\d+>","$1");
            return ReactionEmoji.of(id,emoji,false);
        } else if (name.matches("<a:\\w+:\\d+>")){              //animated
            long id = Long.parseLong(name.replaceFirst("<a:\\w+:(\\d+)>","$1"));  // This shouldn't error
            String emoji = name.replaceFirst("<a:(\\w+):\\d+>","$1");
            return ReactionEmoji.of(id,emoji,true);
        } else {
            return ReactionEmoji.unicode(getUnicodeEmoji(name));
        }
    }
}
