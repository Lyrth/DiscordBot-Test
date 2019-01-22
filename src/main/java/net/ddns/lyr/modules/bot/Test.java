package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;

import java.time.Duration;

public class Test extends BotModule {

    @ModuleEvent
    public void on(MessageCreateEvent event) {

    }

    @ModuleEvent
    public void on(MessageDeleteEvent event) {

    }

    @ModuleEvent
    public void on(TextChannelCreateEvent event) {

    }


    @ModuleEvent
    public void on(ReadyEvent event) {
        Log.log("> Ready Event.");
    }


    public void on(ConnectEvent event) {
        Log.logDebug("> Connect Event.");
    }

    public void on(ReconnectEvent event) {
        Log.logDebug("> REconnect Event.");
    }

    public void on(ReconnectStartEvent event) {
        Log.logDebug("> Reconnect Start Event.");
    }

    public void on(ReconnectFailEvent event) {
        Log.logDebug("> Reconnect FAIL Event.");
    }


}
