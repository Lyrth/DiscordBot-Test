package net.ddns.lyr.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Image;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;


public class Fetch {

    private static final Gson gson = new GsonBuilder().create();


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


    public static Optional<String> fetchHttp(String url){
        try {
            StringBuilder result = new StringBuilder();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
                HttpEntity entity = response.getEntity();
                String line;
                try(BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()))){
                    while ((line = rd.readLine()) != null) result.append(line);
                }
            }
            return Optional.of(result.toString());
        } catch (Exception e){
            Log.logError(e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> fetchJson(String url, Class<T> clazz){
        return fetchHttp(url)
            .map(s -> gson.fromJson(s, clazz));
    }


    public static boolean isInvite(String inviteCode, boolean onlyCheckNonVanity){
        String link = "http://www.discordapp.com/api/v6/invite/%s?with_counts=true";
        return fetchJson(String.format(link,inviteCode),InviteObject.class).map( inviteObject ->
            !(
                (inviteObject.message != null && inviteObject.message.contains("Unknown Invite")) ||
                (inviteObject.guild == null) ||
                (onlyCheckNonVanity && inviteObject.guild.features.contains("VANITY_URL"))
            )
        ).orElse(false);
    }
}
