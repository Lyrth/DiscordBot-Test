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
        Log.info("> Ready Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ConnectEvent event) {
        Log.debug("Connect Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ReconnectEvent event) {
        Log.debug("REconnect Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ReconnectStartEvent event) {
        Log.debug("Reconnect Start Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ReconnectFailEvent event) {
        Log.debug("Reconnect FAIL Event.");
        return Mono.empty();
    }


}
