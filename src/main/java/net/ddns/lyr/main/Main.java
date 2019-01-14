package net.ddns.lyr.main;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.store.redis.RedisStoreService;
import net.ddns.lyr.utils.config.BotConfig;
import net.ddns.lyr.objects.ClientObject;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static ClientObject client;

    public static void main(String args[]) {
        System.setProperty("log4j.skipJansi","false");  // Color support for logging.
        new Main();
    }

    private Main(){
        Log.log("> Starting...");
        BotConfig config = BotConfig.readConfig();
        if (config == null || config.getToken().isEmpty()) return;

        client = new ClientObject(
            new DiscordClientBuilder(config.getToken())
                .setStoreService(new RedisStoreService())
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("aaaa")))
                .build(),
            config);
        Log.log("> | Config done.");

        int retries = 0;
        final int maxRetries = 3;
        while (retries < maxRetries) {
            try {
                Mono.when(
                    client.getDiscordClient().login(),
                    Mono.fromRunnable(client::init))
                    .block();
            } catch (Exception e) {
                if (e.getMessage().matches(".*java\\.net\\..*?Exception.*")) {
                    retries++;
                    Log.logWarn("> Trying to log in again. Internet dropped? Retries: " + (maxRetries - retries));
                    for (AtomicInteger i = new AtomicInteger(3 ^ (retries)); i.get() < 1; i.set(i.get() - 1))
                        Mono.delay(Duration.ofSeconds(1))
                            .doOnNext(n -> Log.logfDebug("> Retrying in %s", i.get()))
                            .block();
                } else {
                    retries = 0;
                }
            }
        }
        Log.log("> Exceeded maximum retries.");
    }

}
