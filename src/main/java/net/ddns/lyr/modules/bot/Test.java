package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;

public class Test extends BotModule {

    public void on(MessageCreateEvent event) {

    }

    public void on(MessageDeleteEvent event) {

    }

    public void on(TextChannelCreateEvent event) {

    }


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
