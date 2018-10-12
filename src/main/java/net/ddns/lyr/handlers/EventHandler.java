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
    private HashMap<String, // K: EventName, V:
        HashMap<String,     //  K: ModuleName
            BotModule>>     //  V: Module
        activeBotModules = new HashMap<>();
    private HashMap<Snowflake,List<GuildModule>> activeGuildModules;

    public EventHandler(EventDispatcher eventDispatcher){
        this.eventDispatcher = eventDispatcher;
        commandHandler = new CommandHandler(new Commands().getCommands());

        // region [eventDispatcher subscribers]
        eventDispatcher.on(ReadyEvent.class).subscribe(this::on);
        eventDispatcher.on(MessageEvent.class).subscribe(this::on);
        eventDispatcher.on(MessageCreateEvent.class).subscribe(this::on);

        eventDispatcher.on(PresenceUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(UserUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(VoiceServerUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(VoiceStateUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(WebhooksUpdateEvent.class).subscribe(this::on);

        /* LifecycleEvents */
        eventDispatcher.on(GatewayLifecycleEvent.class).subscribe(this::on);
        eventDispatcher.on(ResumeEvent.class).subscribe(this::on);
        eventDispatcher.on(ConnectEvent.class).subscribe(this::on);
        eventDispatcher.on(DisconnectEvent.class).subscribe(this::on);
        eventDispatcher.on(ReconnectEvent.class).subscribe(this::on);
        eventDispatcher.on(ReconnectStartEvent.class).subscribe(this::on);
        eventDispatcher.on(ReconnectFailEvent.class).subscribe(this::on);

        /* GuildEvents */
        eventDispatcher.on(GuildEvent.class).subscribe(this::on);
        // Other
        eventDispatcher.on(EmojisUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(IntegrationsUpdateEvent.class).subscribe(this::on);
        // Guild updates
        eventDispatcher.on(GuildCreateEvent.class).subscribe(this::on);
        eventDispatcher.on(GuildDeleteEvent.class).subscribe(this::on);
        eventDispatcher.on(GuildUpdateEvent.class).subscribe(this::on);
        // Member updates
        eventDispatcher.on(MemberJoinEvent.class).subscribe(this::on);
        eventDispatcher.on(MemberLeaveEvent.class).subscribe(this::on);
        eventDispatcher.on(MemberUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(MemberChunkEvent.class).subscribe(this::on);
        // Bans/Unbans
        eventDispatcher.on(BanEvent.class).subscribe(this::on);
        eventDispatcher.on(UnbanEvent.class).subscribe(this::on);

        /* RoleEvents */
        eventDispatcher.on(RoleEvent.class).subscribe(this::on);
        // Role updates
        eventDispatcher.on(RoleCreateEvent.class).subscribe(this::on);
        eventDispatcher.on(RoleDeleteEvent.class).subscribe(this::on);
        eventDispatcher.on(RoleUpdateEvent.class).subscribe(this::on);

        /* ChannelEvents */
        eventDispatcher.on(ChannelEvent.class).subscribe(this::on);
        // Other
        eventDispatcher.on(PinsUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(TypingStartEvent.class).subscribe(this::on);
        // Private Channels / DMs
        eventDispatcher.on(PrivateChannelCreateEvent.class).subscribe(this::on);
        eventDispatcher.on(PrivateChannelDeleteEvent.class).subscribe(this::on);
        // Guild Categories
        eventDispatcher.on(CategoryCreateEvent.class).subscribe(this::on);
        eventDispatcher.on(CategoryDeleteEvent.class).subscribe(this::on);
        eventDispatcher.on(CategoryUpdateEvent.class).subscribe(this::on);
        // Text Channels
        eventDispatcher.on(TextChannelCreateEvent.class).subscribe(this::on);
        eventDispatcher.on(TextChannelDeleteEvent.class).subscribe(this::on);
        eventDispatcher.on(TextChannelUpdateEvent.class).subscribe(this::on);
        // Voice Channels
        eventDispatcher.on(VoiceChannelCreateEvent.class).subscribe(this::on);
        eventDispatcher.on(VoiceChannelDeleteEvent.class).subscribe(this::on);
        eventDispatcher.on(VoiceChannelUpdateEvent.class).subscribe(this::on);

        /* MessageEvents */
        eventDispatcher.on(MessageDeleteEvent.class).subscribe(this::on);
        eventDispatcher.on(MessageUpdateEvent.class).subscribe(this::on);
        eventDispatcher.on(ReactionAddEvent.class).subscribe(this::on);
        eventDispatcher.on(ReactionRemoveEvent.class).subscribe(this::on);
        eventDispatcher.on(ReactionRemoveAllEvent.class).subscribe(this::on);
        eventDispatcher.on(MessageBulkDeleteEvent.class).subscribe(this::on);
        // endregion
    }

    ////
    // region [onEvent handlers]
    ////

    private void on(ReadyEvent event){
        Log.log("> Logged in as " + event.getSelf().getUsername());
        System.gc();
        if(activeBotModules.get("ReadyEvent") != null)
            activeBotModules.get("ReadyEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(MessageCreateEvent event){
        commandHandler.handle(event).subscribe();
        if(activeBotModules.get("MessageCreateEvent") != null)
            activeBotModules.get("MessageCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }


    /** Other Events **/
    private void on(PresenceUpdateEvent event){
        if(activeBotModules.get("PresenceUpdateEvent") != null)
            activeBotModules.get("PresenceUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(UserUpdateEvent event){
        if(activeBotModules.get("UserUpdateEvent") != null)
            activeBotModules.get("UserUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(VoiceServerUpdateEvent event){
        if(activeBotModules.get("VoiceServerUpdateEvent") != null)
            activeBotModules.get("VoiceServerUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(VoiceStateUpdateEvent event){
        if(activeBotModules.get("VoiceStateUpdateEvent") != null)
            activeBotModules.get("VoiceStateUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(WebhooksUpdateEvent event){
        if(activeBotModules.get("WebhooksUpdateEvent") != null)
            activeBotModules.get("WebhooksUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }


    /** LifecycleEvents **/
    private void on(GatewayLifecycleEvent event){
        if(activeBotModules.get("GatewayLifecycleEvent") != null)
            activeBotModules.get("GatewayLifecycleEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    private void on(ResumeEvent event){
        if(activeBotModules.get("ResumeEvent") != null)
            activeBotModules.get("ResumeEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(ConnectEvent event){
        if(activeBotModules.get("ConnectEvent") != null)
            activeBotModules.get("ConnectEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(DisconnectEvent event){
        if(activeBotModules.get("DisconnectEvent") != null)
            activeBotModules.get("DisconnectEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(ReconnectEvent event){
        if(activeBotModules.get("ReconnectEvent") != null)
            activeBotModules.get("ReconnectEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(ReconnectStartEvent event){
        if(activeBotModules.get("ReconnectStartEvent") != null)
            activeBotModules.get("ReconnectStartEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(ReconnectFailEvent event){
        if(activeBotModules.get("ReconnectFailEvent") != null)
            activeBotModules.get("ReconnectFailEvent").forEach( (moduleName,module) -> module.on(event) );
    }


    /** GuildEvents **/
    private void on(GuildEvent event){
        if(activeBotModules.get("GuildEvent") != null)
            activeBotModules.get("GuildEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Other
    private void on(EmojisUpdateEvent event){
        if(activeBotModules.get("EmojisUpdateEvent") != null)
            activeBotModules.get("EmojisUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(IntegrationsUpdateEvent event){
        if(activeBotModules.get("IntegrationsUpdateEvent") != null)
            activeBotModules.get("IntegrationsUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Guild updates
    private void on(GuildCreateEvent event){
        if(activeBotModules.get("GuildCreateEvent") != null)
            activeBotModules.get("GuildCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(GuildDeleteEvent event){
        if(activeBotModules.get("GuildDeleteEvent") != null)
            activeBotModules.get("GuildDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(GuildUpdateEvent event){
        if(activeBotModules.get("GuildUpdateEvent") != null)
            activeBotModules.get("GuildUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Member updates
    private void on(MemberJoinEvent event){
        if(activeBotModules.get("MemberJoinEvent") != null)
            activeBotModules.get("MemberJoinEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(MemberLeaveEvent event){
        if(activeBotModules.get("MemberLeaveEvent") != null)
            activeBotModules.get("MemberLeaveEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(MemberUpdateEvent event){
        if(activeBotModules.get("MemberUpdateEvent") != null)
            activeBotModules.get("MemberUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(MemberChunkEvent event){
        if(activeBotModules.get("MemberChunkEvent") != null)
            activeBotModules.get("MemberChunkEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Bans/Unbans
    private void on(BanEvent event){
        if(activeBotModules.get("BanEvent") != null)
            activeBotModules.get("BanEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(UnbanEvent event){
        if(activeBotModules.get("UnbanEvent") != null)
            activeBotModules.get("UnbanEvent").forEach( (moduleName,module) -> module.on(event) );
    }


    /** RoleEvents **/
    private void on(RoleEvent event){
        if(activeBotModules.get("RoleEvent") != null)
            activeBotModules.get("RoleEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Role updates
    private void on(RoleCreateEvent event){
        if(activeBotModules.get("RoleCreateEvent") != null)
            activeBotModules.get("RoleCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(RoleDeleteEvent event){
        if(activeBotModules.get("RoleDeleteEvent") != null)
            activeBotModules.get("RoleDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(RoleUpdateEvent event){
        if(activeBotModules.get("RoleUpdateEvent") != null)
            activeBotModules.get("RoleUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }


    /** ChannelEvents **/
    private void on(ChannelEvent event){
        if(activeBotModules.get("ChannelEvent") != null)
            activeBotModules.get("ChannelEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Other
    private void on(PinsUpdateEvent event){
        if(activeBotModules.get("PinsUpdateEvent") != null)
            activeBotModules.get("PinsUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(TypingStartEvent event){
        if(activeBotModules.get("TypingStartEvent") != null)
            activeBotModules.get("TypingStartEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Private Channels / DMs
    private void on(PrivateChannelCreateEvent event){
        if(activeBotModules.get("PrivateChannelCreateEvent") != null)
            activeBotModules.get("PrivateChannelCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(PrivateChannelDeleteEvent event){
        if(activeBotModules.get("PrivateChannelDeleteEvent") != null)
            activeBotModules.get("PrivateChannelDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Guild Categories
    private void on(CategoryCreateEvent event){
        if(activeBotModules.get("CategoryCreateEvent") != null)
            activeBotModules.get("CategoryCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(CategoryDeleteEvent event){
        if(activeBotModules.get("CategoryDeleteEvent") != null)
            activeBotModules.get("CategoryDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(CategoryUpdateEvent event){
        if(activeBotModules.get("CategoryUpdateEvent") != null)
            activeBotModules.get("CategoryUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Text Channels
    private void on(TextChannelCreateEvent event){
        if(activeBotModules.get("TextChannelCreateEvent") != null)
            activeBotModules.get("TextChannelCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(TextChannelDeleteEvent event){
        if(activeBotModules.get("TextChannelDeleteEvent") != null)
            activeBotModules.get("TextChannelDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(TextChannelUpdateEvent event){
        if(activeBotModules.get("TextChannelUpdateEvent") != null)
            activeBotModules.get("TextChannelUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    // Voice Channels
    private void on(VoiceChannelCreateEvent event){
        if(activeBotModules.get("VoiceChannelCreateEvent") != null)
            activeBotModules.get("VoiceChannelCreateEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(VoiceChannelDeleteEvent event){
        if(activeBotModules.get("VoiceChannelDeleteEvent") != null)
            activeBotModules.get("VoiceChannelDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(VoiceChannelUpdateEvent event){
        if(activeBotModules.get("VoiceChannelUpdateEvent") != null)
            activeBotModules.get("VoiceChannelUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }


    /** MessageEvents **/
    private void on(MessageEvent event){
        if(activeBotModules.get("MessageEvent") != null)
            activeBotModules.get("MessageEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    private void on(MessageDeleteEvent event){
        if(activeBotModules.get("MessageDeleteEvent") != null)
            activeBotModules.get("MessageDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(MessageUpdateEvent event){
        if(activeBotModules.get("MessageUpdateEvent") != null)
            activeBotModules.get("MessageUpdateEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    private void on(ReactionAddEvent event){
        if(activeBotModules.get("ReactionAddEvent") != null)
            activeBotModules.get("ReactionAddEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(ReactionRemoveEvent event){
        if(activeBotModules.get("ReactionRemoveEvent") != null)
            activeBotModules.get("ReactionRemoveEvent").forEach( (moduleName,module) -> module.on(event) );
    }
    private void on(ReactionRemoveAllEvent event){
        if(activeBotModules.get("ReactionRemoveAllEvent") != null)
            activeBotModules.get("ReactionRemoveAllEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    private void on(MessageBulkDeleteEvent event){
        if(activeBotModules.get("MessageBulkDeleteEvent") != null)
            activeBotModules.get("MessageBulkDeleteEvent").forEach( (moduleName,module) -> module.on(event) );
    }

    ////
    // endregion
    ////

    public void registerBotEvent(BotModule module){
        Flux.fromIterable(Arrays.asList(module.getClass().getDeclaredMethods()))  // Convert to stream(-like)
            .filter(method -> method.isAnnotationPresent(ModuleEvent.class))      // Filter methods by annotation
            .map(method -> {                                                      // forEach method
                final String eventName = method.getParameterTypes()[0].getSimpleName();
                final String eventNameTrunc = eventName.replace("Event","");
                final String moduleName = module.getClass().getName();
                int i=0;
                while (true)
                    try {
                        i++;
                        if (i==1) {EventType.valueOf(eventNameTrunc); break;} else
                        if (i==2) {EventType.Channel.valueOf(eventNameTrunc); break;} else
                        if (i==3) {EventType.Message.valueOf(eventNameTrunc); break;} else
                        if (i==4) {EventType.Guild.valueOf(eventNameTrunc); break;} else
                        if (i==5) {EventType.Lifecycle.valueOf(eventNameTrunc); break;} else
                        if (i==6) {EventType.Role.valueOf(eventNameTrunc); break;} else {
                            Log.logf("> Event name %s invalid.",eventNameTrunc);
                            return 0;                                  // ^ No. [0: Invalid Event name.]
                        }
                    } catch (IllegalArgumentException ignored) {}
                if (activeBotModules.get(eventName) != null){  // containsKey not applicable
                    Log.logf("> Registering %s...",eventName);
                    if (activeBotModules.get(eventName).get(moduleName) != null)
                        return 2;                              // [2: Module already registered.]
                    activeBotModules.get(eventName).put(moduleName,module);
                } else {
                    Log.logf("> Applying and registering %s...",eventName);
                    HashMap<String,BotModule> map = new HashMap<>();
                    map.put(moduleName,module);
                    activeBotModules.put(eventName,map);
                }
                Log.log("> Registered.");
                return 1;        // [1: All OK]
            }).subscribe();
    }

    public void unregisterBotEvent(BotModule module){
        activeBotModules.forEach( (eventName,eventMap) ->
            eventMap.remove( module.getClass().getName() )
        );
    }

}
