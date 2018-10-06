package net.ddns.lyr.utils;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Image;

public class Fetch {
    public static String fetchAvatar(Member u){
        return u.getAvatarUrl(Image.Format.WEB_P).orElse(
            u.getAvatarUrl(Image.Format.GIF).orElse(
                u.getAvatarUrl(Image.Format.PNG).orElse(
                            u.getAvatarUrl(Image.Format.JPEG).orElse(u.getDefaultAvatarUrl())
                )
            )
        );
    }
    public static String fetchAvatar(User u){
        return u.getAvatarUrl(Image.Format.WEB_P).orElse(
                u.getAvatarUrl(Image.Format.GIF).orElse(
                        u.getAvatarUrl(Image.Format.PNG).orElse(
                                u.getAvatarUrl(Image.Format.JPEG).orElse(u.getDefaultAvatarUrl())
                        )
                )
        );
    }
}
