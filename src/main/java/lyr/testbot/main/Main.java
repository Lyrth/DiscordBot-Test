package lyr.testbot.main;

import discord4j.common.GitProperties;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.store.redis.RedisStoreService;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.BotConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Main {

    public static ClientObject client;

    public static void main(String args[]) {
        System.setProperty("log4j.skipJansi","false");  // Color support for logging.
        Log.logfDebug("Running on Discord4j version %s",
            GitProperties.getProperties().getProperty(GitProperties.GIT_COMMIT_ID_DESCRIBE));
        new Main();
    }

    @SuppressWarnings("ConstantConditions")
    private Main(){
        Log.log("> Starting...");
        BotConfig config = BotConfig.readConfig();
        if (config == null || config.getToken().isEmpty()) return;

        client = new ClientObject(
            new DiscordClientBuilder(config.getToken())
                .setStoreService(new RedisStoreService())
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("aaaa")))
                .setEventScheduler(Schedulers.immediate())
                .build(),
            config);
        Log.log("> | Config done.");

        int retries = 0;
        final int maxRetries = 4;  // total logins to be attempted
        while (retries < maxRetries) {
            retries++;
            retries =
                Mono.when(client.getDiscordClient().login(), Mono.fromRunnable(client::init))
                    .map(v -> 0)
                    .onErrorReturn(t -> {
                        if (t.toString().matches(".*java\\.net\\..*?Exception.*")){
                            Log.logWarn(">> Caught network error.");
                            return true;
                        } else {
                            Log.logError(">>> Caught fatal error! ");
                            t.printStackTrace();
                            return false;
                        }}, retries)
                    .filter(retr -> retr < maxRetries)
                    .flatMap(retr ->
                        Flux.generate(() -> (int) Math.pow(3,retr),
                            (Integer s, SynchronousSink<Integer> sink) -> {
                                sink.next(s--);
                                if (s < 0) sink.complete();
                                return s;
                            })
                            .delayElements(Duration.ofSeconds(1))
                            .doOnNext(n -> Log.logfDebug("Retrying in %s", n))
                            .last()
                            .then(Mono.just(retr))
                    )
                    .onErrorReturn(maxRetries)
                    .switchIfEmpty(Mono.just(maxRetries))
                    .block();
        }
        Log.logError(">>> Exceeded maximum retries.");
    }

}
