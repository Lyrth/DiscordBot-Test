package lyr.testbot.main;

import discord4j.common.GitProperties;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.store.api.service.StoreService;
import discord4j.store.jdk.JdkStoreService;
import discord4j.store.redis.RedisStoreService;
import io.lettuce.core.RedisException;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.BotConfig;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Main {

    public static ClientObject client;

    public static void main(String[] args) {
        new Main();
    }

    private Main(){
        System.setProperty("log4j.skipJansi","false");  // Color support for logging.

        // TODO: do IO on boundedElastic schedulers within reactive publishers

        Log.info("> Starting...");
        Log.debugFormat("Running on Discord4j version %s",
            GitProperties.getProperties().getProperty(GitProperties.GIT_COMMIT_ID_DESCRIBE));
        BotConfig config = BotConfig.readConfig();    // TODO : 1 fix this
        if (config == null || config.getToken().isEmpty()) return;

        client = new ClientObject(
            new DiscordClientBuilder(config.getToken())
                .setStoreService(getStoreService())
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("aaaa")))
                .setEventScheduler(Schedulers.immediate())
                .build(),
            config);
        Log.info("> | Config done.");

        final int maxRetries = 64;
        final double waitFactor = 2.0;
        final double maxWaitSeconds = 1800.0;  // 30 mins

        Mono.whenDelayError(
            client.getDiscordClient().login()
                .retryWhen(f -> f.zipWith(Flux.range(1,maxRetries), (t,i) -> {
                        if (i < maxRetries && t.getClass().getName().contains("java.net."))
                            return i;
                        else throw Exceptions.propagate(new IllegalStateException("Network retries exhausted.",t));
                    })
                    .map(i -> Math.pow(waitFactor+((Math.random()-0.5d)/2),i))
                    .map(i -> i < maxWaitSeconds ? i : maxWaitSeconds+((Math.random()-0.5d)*4))
                    .doOnNext(d -> Log.warnFormat(">> Caught network error. Retrying in %.2fs...\n",d))
                    .map(d -> (int)(d*1000))
                    .flatMap(d -> Mono.delay(Duration.ofMillis(d)))
                ),
            Mono.fromRunnable(client::init)
        ).doOnError(t -> {
            Log.error(">>> Fatal error: " + t.getMessage());
            t.printStackTrace();
        }).doOnNext(v -> Log.info("> Shutting down.")).block();
    }

    private StoreService getStoreService(){
        try {
            return new RedisStoreService();
        } catch (RedisException e) {
            Log.warn(">> Redis store service error! Using JdkStoreService instead.");
            Log.debug(e.toString());
            return new JdkStoreService();
        }
    }
}
