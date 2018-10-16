package net.ddns.lyr.handlers;

import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.commands.Commands;
import net.ddns.lyr.enums.EventType;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Flux;

import java.util.*;

public class EventHandler {
    private EventDispatcher eventDispatcher;
    private CommandHandler commandHandler;

    //private List<BotModule> activeBotModules;
    //volatile?
    private HashMap<String, // K: EventName, V:
        HashMap<String,     //  K: ModuleName
            BotModule>>     //  V: Module
        activeBotModules = new HashMap<>();
    private HashMap<Snowflake,List<GuildModule>> activeGuildModules;

    public EventHandler(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        commandHandler = new CommandHandler(new Commands().getCommands());
        subscribe();
    }


    private void onReady(ReadyEvent event){
        Log.log("> Logged in as " + event.getSelf().getUsername());
        System.gc();
        if(activeBotModules.get("ReadyEvent") != null)
            activeBotModules.get("ReadyEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void onMessageCreate(MessageCreateEvent event){
        commandHandler.handle(event).subscribe();
        if(activeBotModules.get("MessageCreateEvent") != null)
            activeBotModules.get("MessageCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    private void subscribe(){   // Please collapse this method

        eventDispatcher.on(ReadyEvent.class).subscribe(event -> {
            onReady(event);
            if(activeBotModules.get("ReadyEvent") != null)
                activeBotModules.get("ReadyEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MessageCreateEvent.class).subscribe(event -> {
            onMessageCreate(event);
            if(activeBotModules.get("MessageCreateEvent") != null)
                activeBotModules.get("MessageCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });

        eventDispatcher.on(PresenceUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("PresenceUpdateEvent") != null)
                activeBotModules.get("PresenceUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(UserUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("UserUpdateEvent") != null)
                activeBotModules.get("UserUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(VoiceServerUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("VoiceServerUpdateEvent") != null)
                activeBotModules.get("VoiceServerUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(VoiceStateUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("VoiceStateUpdateEvent") != null)
                activeBotModules.get("VoiceStateUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(WebhooksUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("WebhooksUpdateEvent") != null)
                activeBotModules.get("WebhooksUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });

        /* LifecycleEvents */
        eventDispatcher.on(GatewayLifecycleEvent.class).subscribe(event -> {
            if(activeBotModules.get("GatewayLifecycleEvent") != null)
                activeBotModules.get("GatewayLifecycleEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ResumeEvent.class).subscribe(event -> {
            if(activeBotModules.get("ResumeEvent") != null)
                activeBotModules.get("ResumeEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ConnectEvent.class).subscribe(event -> {
            if(activeBotModules.get("ConnectEvent") != null)
                activeBotModules.get("ConnectEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(DisconnectEvent.class).subscribe(event -> {
            if(activeBotModules.get("DisconnectEvent") != null)
                activeBotModules.get("DisconnectEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ReconnectEvent.class).subscribe(event -> {
            if(activeBotModules.get("ReconnectEvent") != null)
                activeBotModules.get("ReconnectEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ReconnectStartEvent.class).subscribe(event -> {
            if(activeBotModules.get("ReconnectStartEvent") != null)
                activeBotModules.get("ReconnectStartEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ReconnectFailEvent.class).subscribe(event -> {
            if(activeBotModules.get("ReconnectFailEvent") != null)
                activeBotModules.get("ReconnectFailEvent").forEach( (moduleName,module) -> module.on(event) );
        });

        /* GuildEvents */
        eventDispatcher.on(GuildEvent.class).subscribe(event -> {
            if(activeBotModules.get("GuildEvent") != null)
                activeBotModules.get("GuildEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Other
        eventDispatcher.on(EmojisUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("EmojisUpdateEvent") != null)
                activeBotModules.get("EmojisUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(IntegrationsUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("IntegrationsUpdateEvent") != null)
                activeBotModules.get("IntegrationsUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Guild updates
        eventDispatcher.on(GuildCreateEvent.class).subscribe(event -> {
            if(activeBotModules.get("GuildCreateEvent") != null)
                activeBotModules.get("GuildCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(GuildDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("GuildDeleteEvent") != null)
                activeBotModules.get("GuildDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(GuildUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("GuildUpdateEvent") != null)
                activeBotModules.get("GuildUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Member updates
        eventDispatcher.on(MemberJoinEvent.class).subscribe(event -> {
            if(activeBotModules.get("MemberJoinEvent") != null)
                activeBotModules.get("MemberJoinEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MemberLeaveEvent.class).subscribe(event -> {
            if(activeBotModules.get("MemberLeaveEvent") != null)
                activeBotModules.get("MemberLeaveEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MemberUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("MemberUpdateEvent") != null)
                activeBotModules.get("MemberUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MemberChunkEvent.class).subscribe(event -> {
            if(activeBotModules.get("MemberChunkEvent") != null)
                activeBotModules.get("MemberChunkEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Bans/Unbans
        eventDispatcher.on(BanEvent.class).subscribe(event -> {
            if(activeBotModules.get("BanEvent") != null)
                activeBotModules.get("BanEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(UnbanEvent.class).subscribe(event -> {
            if(activeBotModules.get("UnbanEvent") != null)
                activeBotModules.get("UnbanEvent").forEach( (moduleName,module) -> module.on(event) );
        });

        /* RoleEvents */
        eventDispatcher.on(RoleEvent.class).subscribe(event -> {
            if(activeBotModules.get("RoleEvent") != null)
                activeBotModules.get("RoleEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Role updates
        eventDispatcher.on(RoleCreateEvent.class).subscribe(event -> {
            if(activeBotModules.get("RoleCreateEvent") != null)
                activeBotModules.get("RoleCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(RoleDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("RoleDeleteEvent") != null)
                activeBotModules.get("RoleDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(RoleUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("RoleUpdateEvent") != null)
                activeBotModules.get("RoleUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });

        /* ChannelEvents */
        eventDispatcher.on(ChannelEvent.class).subscribe(event -> {
            if(activeBotModules.get("ChannelEvent") != null)
                activeBotModules.get("ChannelEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Other
        eventDispatcher.on(PinsUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("PinsUpdateEvent") != null)
                activeBotModules.get("PinsUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(TypingStartEvent.class).subscribe(event -> {
            if(activeBotModules.get("TypingStartEvent") != null)
                activeBotModules.get("TypingStartEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Private Channels / DMs
        eventDispatcher.on(PrivateChannelCreateEvent.class).subscribe(event -> {
            if(activeBotModules.get("PrivateChannelCreateEvent") != null)
                activeBotModules.get("PrivateChannelCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(PrivateChannelDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("PrivateChannelDeleteEvent") != null)
                activeBotModules.get("PrivateChannelDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Guild Categories
        eventDispatcher.on(CategoryCreateEvent.class).subscribe(event -> {
            if(activeBotModules.get("CategoryCreateEvent") != null)
                activeBotModules.get("CategoryCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(CategoryDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("CategoryDeleteEvent") != null)
                activeBotModules.get("CategoryDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(CategoryUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("CategoryUpdateEvent") != null)
                activeBotModules.get("CategoryUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Text Channels
        eventDispatcher.on(TextChannelCreateEvent.class).subscribe(event -> {
            if(activeBotModules.get("TextChannelCreateEvent") != null)
                activeBotModules.get("TextChannelCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(TextChannelDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("TextChannelDeleteEvent") != null)
                activeBotModules.get("TextChannelDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(TextChannelUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("TextChannelUpdateEvent") != null)
                activeBotModules.get("TextChannelUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        // Voice Channels
        eventDispatcher.on(VoiceChannelCreateEvent.class).subscribe(event -> {
            if(activeBotModules.get("VoiceChannelCreateEvent") != null)
                activeBotModules.get("VoiceChannelCreateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(VoiceChannelDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("VoiceChannelDeleteEvent") != null)
                activeBotModules.get("VoiceChannelDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(VoiceChannelUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("VoiceChannelUpdateEvent") != null)
                activeBotModules.get("VoiceChannelUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });

        /* MessageEvents */
        eventDispatcher.on(MessageEvent.class).subscribe(event -> {
            if(activeBotModules.get("MessageEvent") != null)
                activeBotModules.get("MessageEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MessageDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("MessageDeleteEvent") != null)
                activeBotModules.get("MessageDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MessageUpdateEvent.class).subscribe(event -> {
            if(activeBotModules.get("MessageUpdateEvent") != null)
                activeBotModules.get("MessageUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ReactionAddEvent.class).subscribe(event -> {
            if(activeBotModules.get("ReactionAddEvent") != null)
                activeBotModules.get("ReactionAddEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ReactionRemoveEvent.class).subscribe(event -> {
            if(activeBotModules.get("ReactionRemoveEvent") != null)
                activeBotModules.get("ReactionRemoveEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(ReactionRemoveAllEvent.class).subscribe(event -> {
            if(activeBotModules.get("ReactionRemoveAllEvent") != null)
                activeBotModules.get("ReactionRemoveAllEvent").forEach( (moduleName,module) -> module.on(event) );
        });
        eventDispatcher.on(MessageBulkDeleteEvent.class).subscribe(event -> {
            if(activeBotModules.get("MessageBulkDeleteEvent") != null)
                activeBotModules.get("MessageBulkDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
        });

    }


    public void registerBotEvent(BotModule module){
        Flux.fromIterable(Arrays.asList(module.getClass().getDeclaredMethods()))  // Convert to stream(-like)
            .filter(method -> method.isAnnotationPresent(ModuleEvent.class))      // Filter methods by annotation
            .map(method -> {                                                      // forEach method
                final String eventName = method.getParameterTypes()[0].getSimpleName();
                final String eventNameTrunc = eventName.replace("Event","");
                final String moduleName = module.getClass().getName();
                try { EventType.All.valueOf(eventNameTrunc); }
                catch (IllegalArgumentException ignored) {
                    Log.logfDebug("> | Event name %s invalid.",eventNameTrunc);
                    return 0;                                  // ^ No. [0: Invalid Event name.]
                }
                if (activeBotModules.get(eventName) != null){  // containsKey not applicable
                    Log.logfDebug("> | Registering %s...",eventName);
                    if (activeBotModules.get(eventName).get(moduleName) != null)
                        return 2;                              // [2: Module already registered.]
                    activeBotModules.get(eventName).put(moduleName,module);
                } else {
                    Log.logfDebug("> | Applying and registering %s...",eventName);
                    HashMap<String,BotModule> map = new HashMap<>();
                    map.put(moduleName,module);
                    activeBotModules.put(eventName,map);
                }
                Log.logDebug("> | Registered.");
                return 1;        // [1: All OK]
            }).subscribe();
    }

    public void unregisterBotEvent(BotModule module){
        Log.logfDebug("> Unregistering %s...",module.getName());
        activeBotModules.forEach( (eventName,eventMap) ->
            eventMap.remove( module.getClass().getName() )
        );
    }

}
