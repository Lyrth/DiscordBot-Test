package net.ddns.lyr.handlers;

import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import net.ddns.lyr.commands.Commands;
import net.ddns.lyr.utils.Log;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;

public class EventHandler {
    private EventDispatcher eventDispatcher;
    private CommandHandler commandHandler = new CommandHandler(new Commands().getCommands());

    public Map<String,         // BotModule name
        Map<String,Method>>    // Event name
        list;

    public EventHandler(EventDispatcher eventDispatcher){
        this.eventDispatcher = eventDispatcher;
        eventDispatcher.on(ReadyEvent.class).subscribe(this::onReady);
        eventDispatcher.on(MessageCreateEvent.class).subscribe(this::onMessageCreate);
    }

    private void onReady(ReadyEvent ready){
        Log.log("> Logged in as " + ready.getSelf().getUsername());
        System.gc();
    }

    private void onMessageCreate(MessageCreateEvent event){
        commandHandler.handle(event).subscribe();
    }

    public void registerEvent(String moduleName, String eventName, Method method){

    }

}
