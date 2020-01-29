package lyr.testbot.util;

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
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Optional;


public class Fetch {

    private static final Gson gson = new GsonBuilder().create();

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrme/72.0.3626.121 Safari/537.36";
    private static final Duration CACHE_DURATION = Duration.ofSeconds(86398);

    private static final HttpClient client = HttpClient.create().headers(h ->
        h.set("user-agent", USER_AGENT).set("accept-encoding","gzip")).compress(true);;

    public static <U extends User> String fetchAvatar(U u){
        return u.getAvatarUrl(Image.Format.WEB_P).orElse(
            u.getAvatarUrl(Image.Format.GIF).orElse(
                u.getAvatarUrl(Image.Format.PNG).orElse(
                    u.getAvatarUrl(Image.Format.JPEG).orElse(u.getDefaultAvatarUrl())
                )
            )
        );
    }

    public static Mono<String> fetchHttp(String url, boolean noCache){
        return client.baseUrl(url)
            .get()
            .responseSingle((r,b) -> b.asString())
            .cache($ -> noCache ? Duration.ZERO : CACHE_DURATION,
                $ -> Duration.ZERO,
                () -> noCache ? Duration.ZERO : CACHE_DURATION);
    }

    public static Mono<String> fetchHttp(String url){
        return fetchHttp(url, false);
    }

    public static <T> Mono<T> fetchJson(String url, Class<T> clazz){
        return fetchHttp(url).map(s -> gson.fromJson(s, clazz));
    }

    public static <T> Mono<T> fetchJson(String url, Class<T> clazz, boolean noCache){
        return fetchHttp(url, noCache).map(s -> gson.fromJson(s, clazz));
    }

    public static Mono<Boolean> isInvite(String inviteCode, boolean onlyCheckNonVanity){
        String link = "http://www.discordapp.com/api/v6/invite/%s?with_counts=true";
        return fetchJson(String.format(link,inviteCode),InviteObject.class)
            .map( inviteObject ->
                !(
                    (inviteObject.message != null && inviteObject.message.contains("Unknown Invite")) ||
                        (inviteObject.guild == null) ||
                        (onlyCheckNonVanity && inviteObject.guild.features.contains("VANITY_URL"))
                )
            );
    }
}
