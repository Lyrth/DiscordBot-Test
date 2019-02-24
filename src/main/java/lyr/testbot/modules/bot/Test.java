package lyr.testbot.modules.bot;

import discord4j.core.event.domain.lifecycle.*;
import lyr.testbot.templates.BotModule;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

public class Test extends BotModule {

    public Mono<Void> on(ReadyEvent event) {
        Log.log("> Ready Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ConnectEvent event) {
        Log.logDebug("Connect Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ReconnectEvent event) {
        Log.logDebug("REconnect Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ReconnectStartEvent event) {
        Log.logDebug("Reconnect Start Event.");
        return Mono.empty();
    }

    public Mono<Void> on(ReconnectFailEvent event) {
        Log.logDebug("Reconnect FAIL Event.");
        return Mono.empty();
    }


}
