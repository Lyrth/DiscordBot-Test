package net.ddns.lyr.handlers;

import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.commands.Commands;
import net.ddns.lyr.enums.EventType;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.AnnotationUtil;
import net.ddns.lyr.utils.EventUtil;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class EventHandler {
    private EventDispatcher eventDispatcher;
    private CommandHandler commandHandler;

    //private List<BotModule> activeBotModules;
    private HashMap<String, // K: EventName, V:
        HashMap<String,     //  K: ModuleName
            BotModule>>     //  V: Module
        activeBotModules = new HashMap<>();
    private HashMap<Snowflake,List<GuildModule>> activeGuildModules;

    public EventHandler(EventDispatcher eventDispatcher){
        this.eventDispatcher = eventDispatcher;
        commandHandler = new CommandHandler(new Commands().getCommands());
        eventDispatcher.on(ReadyEvent.class).subscribe(this::on);
        eventDispatcher.on(MessageCreateEvent.class).subscribe(this::on);
    }

    private void on(ReadyEvent ready){
        Log.log("> Logged in as " + ready.getSelf().getUsername());
        System.gc();
    }

    private void on(MessageEvent event){
        //aaaa
        System.out.println(event);
    }

    private void on(MessageCreateEvent event){
        commandHandler.handle(event).subscribe();
        activeBotModules.get("MessageCreateEvent")
            .forEach( (moduleName,module) -> module.on(event) );
    }

    public void registerBotEvent(BotModule module){
        Flux.fromIterable(Arrays.asList(module.getClass().getDeclaredMethods()))  // Convert to stream(-like)
            .filter(method -> method.isAnnotationPresent(ModuleEvent.class))      // Filter methods by annotation
            .map(method -> {                                                      // forEach method
                final String eventName = method.getParameterTypes()[0].getName();
                final String moduleName = module.getClass().getName();
                try {
                    EventType.valueOf(eventName);              // Is event name valid?
                } catch (IllegalArgumentException ignored) {
                    return 0;                                  // ^ No. [0: Invalid Event name.]
                }
                if (activeBotModules.get(eventName) != null){  // containsKey not applicable
                    if (activeBotModules.get(eventName).get(moduleName) != null)
                        return 2;                              // [2: Module already registered.]
                    activeBotModules.get(eventName).put(moduleName,module);
                } else {
                    HashMap<String,BotModule> map = new HashMap<>();
                    map.put(moduleName,module);
                    activeBotModules.put(eventName,map);
                }
                return 1;        // [1: All OK]
            }).subscribe();
    }

    public void unregisterBotEvent(BotModule module){
        activeBotModules.forEach( (eventName,eventMap) ->
            eventMap.remove( module.getClass().getName() )
        );
    }

}
