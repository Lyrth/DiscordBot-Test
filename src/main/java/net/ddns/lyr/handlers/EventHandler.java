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
import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.commands.Commands;
import net.ddns.lyr.enums.EventType;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.*;

public class EventHandler {
    private EventDispatcher eventDispatcher;
    private CommandHandler commandHandler;

    // <moduleName, module>
    private HashMap<String, BotModule>
        activeBotModules = new HashMap<>();

    // <guildId, <moduleName, module>>
    public HashMap<Snowflake, HashMap<String, GuildModule>>
        activeGuildModules = new HashMap<>();

    public EventHandler(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        Mono.fromRunnable(() -> {
            subscribe();
            Log.logDebug("> Finished subscribing bot events.");
        }).subscribe();
    }

    public Mono<Void> registerBotEvent(BotModule m){
        return Mono.just(m).doOnNext(module -> {
            activeBotModules.putIfAbsent(module.getName(), module);
            Log.logDebug("> | Registered.");
        }).then();
    }

    public Mono<Void> unregisterBotEvent(BotModule module){
        return unregisterBotEvent(module.getName());
    }

    public Mono<Void> unregisterBotEvent(String moduleName){
        return Mono.fromRunnable(() -> {
            Log.logfDebug("> Unregistering %s...", moduleName);
            activeBotModules.remove(moduleName);
        }).then();
    }


    public void updateGuildModules(GuildSetting setting){
        Map<String,GuildModule> availableGuildModules = Main.client.availableGuildModules;
        Snowflake guildId = setting.guildId;
        activeGuildModules.computeIfAbsent(guildId, id -> new HashMap<>());
        availableGuildModules.forEach((moduleName,module) -> {
            if (setting.enabledModules.contains(moduleName)){  // It should be enabled
                if (activeGuildModules.get(guildId).containsKey(moduleName))  // Already enabled
                    return;
                Log.logfDebug("> | Enabling module %s...", moduleName);
                GuildModule guildModule =
                    module.newInstance(
                        Main.client.guilds
                            .filter(guild -> guildId.equals(guild.getId())).single(),  // or .last()
                        setting
                    );
                activeGuildModules.get(guildId).put(moduleName,guildModule);
            } else { // Should be disabled.
                if (!activeGuildModules.get(guildId).containsKey(moduleName))  // Already disabled
                    return;
                Log.logfDebug("> | Disabling module %s...", moduleName);
                activeGuildModules.get(guildId).remove(moduleName);
            }

            /*
            if (availableGuildModules.containsKey(moduleName)) {  // is valid guildModule?
                if () {
                    Log.logfDebug("> | Module %s...", moduleName);
                    GuildModule module1 = availableGuildModules.get(moduleName)
                        .newInstance(
                            guilds.filter(guild -> guildId.equals(guild.getId())).single(),  // or .last()
                            setting
                        );
                    if (activeGuildModules.get(guildId) != null) {
                        activeGuildModules.get(guildId).put(module1.getName(), module1);
                    } else {
                        HashMap<String, GuildModule> map = new HashMap<>();
                        map.put(module1.getName(), module1);
                        activeGuildModules.put(guildId, map);
                    }
                }
            }*/
        });
    }


    private void subscribe(){   // Please collapse this method for your sanity, maybe.
        eventDispatcher.on(PresenceUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(UserUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(VoiceServerUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(VoiceStateUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(WebhooksUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));

        /* LifecycleEvents */
        eventDispatcher.on(ReadyEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ResumeEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ConnectEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(DisconnectEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ReconnectEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ReconnectStartEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ReconnectFailEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));

        /* GuildEvents */
        // Other
        eventDispatcher.on(EmojisUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(IntegrationsUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Guild updates
        eventDispatcher.on(GuildCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(GuildDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(GuildUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Member updates
        eventDispatcher.on(MemberJoinEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(MemberLeaveEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(MemberUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(MemberChunkEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Bans/Unbans
        eventDispatcher.on(BanEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(UnbanEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));

        /* RoleEvents */
        // Role updates
        eventDispatcher.on(RoleCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(RoleDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(RoleUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));

        /* ChannelEvents */
        // Other
        eventDispatcher.on(PinsUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(TypingStartEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Private Channels / DMs
        eventDispatcher.on(PrivateChannelCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(PrivateChannelDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Guild Categories
        eventDispatcher.on(CategoryCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(CategoryDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(CategoryUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Text Channels
        eventDispatcher.on(TextChannelCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(TextChannelDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(TextChannelUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        // Voice Channels
        eventDispatcher.on(VoiceChannelCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(VoiceChannelDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(VoiceChannelUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));

        /* MessageEvents */
        eventDispatcher.on(MessageCreateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(MessageDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(MessageUpdateEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ReactionAddEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ReactionRemoveEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(ReactionRemoveAllEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));
        eventDispatcher.on(MessageBulkDeleteEvent.class)
            .subscribe(event -> activeBotModules.forEach((moduleName,module) -> module.on(event)));

    }
}
