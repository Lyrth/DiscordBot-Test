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

    public static final int MAX_RETRIES = 64;
    public static final double WAIT_FACTOR = 2.0;
    public static final double MAX_WAIT_SECONDS = 1800.0;  // 30 mins

    public static ClientObject client;

    public static void main(String[] args) {
        new Main();
    }

    private Mono<Void> setup(){
        System.setProperty("log4j.skipJansi", "false");  // Color support for logging.
        return Mono.fromRunnable(() -> {
            Log.info("> Starting...");
            Log.debugFormat("Running on Discord4j version %s",
                GitProperties.getProperties().getProperty(GitProperties.GIT_COMMIT_ID_DESCRIBE));
        });
    }

    private Main(){
        setup().then(BotConfig.readConfig())
            .filter(cfg -> !cfg.getToken().isEmpty())
            .map(cfg -> new ClientObject(
                new DiscordClientBuilder(cfg.getToken())
                    .setStoreService(getStoreService())
                    .setInitialPresence(Presence.doNotDisturb(Activity.listening("aaaa")))
                    .setEventScheduler(Schedulers.immediate())
                    .build(),
                cfg))
            .doOnNext(cli -> client = cli)
            .doOnNext($ -> Log.info("> | Config done."))
            .flatMap($ -> Mono.whenDelayError(        // .flatMap and not .then so these won't run when empty.
                client.getDiscordClient().login().retryWhen(this::handleRetry),
                Mono.fromRunnable(client::init)
            ))
            .doOnError(t -> {
                Log.error(">>> Fatal error: " + t.getMessage());
                t.printStackTrace();
            })
            .doOnNext($ -> Log.info("> Shutting down."))
            .block();
    }

    private Flux<Long> handleRetry(Flux<Throwable> f){
        return f.zipWith(Flux.range(1, MAX_RETRIES), (t, i) -> {
                if (i < MAX_RETRIES && t.getClass().getName().contains("java.net."))
                    return i;
                else throw Exceptions.propagate(new IllegalStateException("Network retries exhausted.",t));
            })
            .map(i -> Math.pow(WAIT_FACTOR + ((Math.random()-0.5d)/2),i))
            .map(i -> i < MAX_WAIT_SECONDS ? i : MAX_WAIT_SECONDS +((Math.random()-0.5d)*4))
            .doOnNext(d -> Log.warnFormat(">> Caught network error. Retrying in %.2fs...\n",d))
            .map(d -> (int)(d*1000))
            .flatMap(d -> Mono.delay(Duration.ofMillis(d)));
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
