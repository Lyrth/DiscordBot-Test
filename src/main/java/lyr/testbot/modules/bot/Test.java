package lyr.testbot.modules.bot;

import discord4j.core.event.domain.lifecycle.*;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.templates.BotModule;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

@ModuleInfo(
    desc = "Test module."
)
public class Test extends BotModule {

    public Mono<Void> on(ReadyEvent event) {
        return Mono.fromRunnable(() -> Log.info("> Ready Event."));
    }

    public Mono<Void> on(ConnectEvent event) {
        return Mono.fromRunnable(() -> Log.debug("Connect Event."));
    }

    public Mono<Void> on(ReconnectEvent event) {
        return Mono.fromRunnable(() -> Log.debug("REconnect Event."));
    }

    public Mono<Void> on(ReconnectStartEvent event) {
        return Mono.fromRunnable(() -> Log.debug("Reconnect Start Event."));
    }

    public Mono<Void> on(ReconnectFailEvent event) {
        return Mono.fromRunnable(() -> Log.debug("Reconnect FAIL Event."));
    }
}
