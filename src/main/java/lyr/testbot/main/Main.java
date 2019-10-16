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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class Main {

    public static ClientObject client;

    public static void main(String[] args) {
        new Main();
    }

    private Main(){
        System.setProperty("log4j.skipJansi","false");  // Color support for logging.
        Log.log("> Starting...");
        Log.logfDebug("Running on Discord4j version %s",
            GitProperties.getProperties().getProperty(GitProperties.GIT_COMMIT_ID_DESCRIBE));
        BotConfig config = BotConfig.readConfig();
        if (config == null || config.getToken().isEmpty()) return;

        client = new ClientObject(
            new DiscordClientBuilder(config.getToken())
                .setStoreService(getStoreService())
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("aaaa")))
                .setEventScheduler(Schedulers.immediate())
                .build(),
            config);
        Log.log("> | Config done.");

        final long maxRetries = 64;
        final double waitFactor = 2.0;
        final double maxWaitSeconds = 1800.0;  // 30 mins

        Mono.when(
            client.getDiscordClient().login()
            .retryWhen(t ->  // Exponential backoff with class filter. (Because Mono doesn't have one.)
                t.index()
                    .flatMap(i ->
                        i.getT2().getClass().getName().contains("java.net.") ? Mono.just(i) : Mono.error(i.getT2()))
                    .flatMap(i ->
                        i.getT1() < maxRetries ? Mono.just(i) : Mono.error(new IllegalStateException("Network retries exhausted.")))
                    .map(Tuple2::getT1)
                    .map(i -> Math.pow(waitFactor+((Math.random()-0.5d)/2),i))
                    .map(i -> i < maxWaitSeconds ? i : maxWaitSeconds)
                    .doOnNext(d -> System.out.printf(">> Caught network error. Retrying in %.2fs...\n",d))
                    .map(d -> (int)(d*1000))
                    .map(Duration::ofMillis)
                    .flatMap(Mono::delay)
            ),
            Mono.fromRunnable(client::init)
        ).doOnError(t -> {
            System.out.println(">>> Fatal error: " + t.getMessage());
            t.printStackTrace();
        }).doOnNext(v -> Log.log("> Shutting down.")).block();
    }

    private StoreService getStoreService(){
        try {
            return new RedisStoreService();
        } catch (RedisException e) {
            Log.logWarn(">> Redis store service error! Using JdkStoreService instead.");
            e.printStackTrace();
            return new JdkStoreService();
        }
    }
}
