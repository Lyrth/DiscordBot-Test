package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class GuildModuleCore extends BotModule {

    private HashMap<Snowflake, GuildSetting> guildSettings;
    private Map<String, GuildModule> availableGuildModules;
    // <guildID, <moduleName, module>>
    private HashMap<Snowflake, HashMap<String, GuildModule>> activeGuildModules;
    private Flux<Guild> guilds;
    private HashMap<Snowflake,Snowflake> chToGuildMap;

    public GuildModuleCore() {
        guildSettings = Main.client.getGuildSettings();  // to get the list of guilds w/ config = possible activated module
        availableGuildModules = Main.client.availableGuildModules;
        activeGuildModules = Main.client.eventHandler.activeGuildModules;
        guilds = Main.client.guilds;
        chToGuildMap = Main.client.channelToGuildMapping;
    }
    
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    public void on(ReadyEvent e){
        int n = e.getGuilds().size();
        HashSet<Snowflake> ids = (HashSet<Snowflake>)
            e.getGuilds().stream().map(ReadyEvent.Guild::getId).collect(Collectors.toSet());
        if (ids.size() != n) Log.logWarn(">> Warning: Set of IDs not the same size as Set of Guilds in ReadyEvent.");
        // wait for all guilds to be received
        Main.client.getEventDispatcher().on(GuildCreateEvent.class).take(n).last().subscribe(gce -> {
            // instantiate each module for each guild
            guildSettings.forEach((guildId, setting) -> {
                if (!ids.contains(guildId)) return;
                Log.logfDebug("> Setting up modules for guild %s", guildId.asString());
                Main.client.eventHandler.updateGuildModules(setting);
            });
            onReadyEvent(e);
        });
    }

    /*
    public void on(PresenceUpdateEvent event){
        Optional.ofNullable(activeGuildModules.get(e.getGuildId()))
            .ifPresent(map ->
                map.forEach((name,module) -> module.on(event))
            );
    }*/
    // A shorthand method for the above function (plus guildId null check for channelId to guildId cases.
    private void exec(Snowflake guildId, BiConsumer<String, GuildModule> func){
        Optional.ofNullable(guildId)
            .map(id -> activeGuildModules.get(id))
            .ifPresent(map -> map.forEach(func));
    }

    /*
    public void on(UserUpdateEvent event){
        activeGuildModules.forEach((guildId,map) ->
            if (map != null) {
                map.forEach((name,module) -> module.on(event))
            }
        );
    }
    */
    // Another shorthand method, for the above function
    private void exec(BiConsumer<String, GuildModule> func){
        activeGuildModules.forEach((id,map) -> {
            if (map != null) map.forEach(func);
        });
    }
    
    // Events
    /** Non-specific events **/
    public void on(PresenceUpdateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(UserUpdateEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(VoiceServerUpdateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(VoiceStateUpdateEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(WebhooksUpdateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }

    /** Lifecycle events **/
    private void onReadyEvent(ReadyEvent e){  // this gets called from the above `on(ReadyEvent e)`, after all guilds are loaded.
        exec((name,module) -> module.on(e));
    }
    public void on(ResumeEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(ConnectEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(DisconnectEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(ReconnectEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(ReconnectStartEvent e){
        exec((name,module) -> module.on(e));
    }
    public void on(ReconnectFailEvent e){
        exec((name,module) -> module.on(e));
    }


    /** GuildEvents **/
    // Other
    public void on(EmojisUpdateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(IntegrationsUpdateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }

    // Guild updates
    public void on(GuildCreateEvent e){
        exec(e.getGuild().getId(), (name,module) -> module.on(e));
    }
    public void on(GuildDeleteEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(GuildUpdateEvent e){
        exec(e.getCurrent().getId(), (name,module) -> module.on(e));
    }

    // Member updates
    public void on(MemberJoinEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(MemberLeaveEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(MemberUpdateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(MemberChunkEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }

    // Bans/Unbans
    public void on(BanEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(UnbanEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }


    /** RoleEvents **/
    // Role updates
    public void on(RoleCreateEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(RoleDeleteEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }
    public void on(RoleUpdateEvent e){
        exec(e.getCurrent().getGuildId(), (name,module) -> module.on(e));
    }


    /** ChannelEvents **/
    // Other
    public void on(PinsUpdateEvent e){
        exec(chToGuildMap.get(e.getChannelId()), (name,module) -> module.on(e));
    }
    public void on(TypingStartEvent e){
        exec(chToGuildMap.get(e.getChannelId()), (name,module) -> module.on(e));
    }

    // Guild Categories
    public void on(CategoryCreateEvent e){
        exec(e.getCategory().getGuildId(), (name,module) -> module.on(e));
    }
    public void on(CategoryDeleteEvent e){
        exec(e.getCategory().getGuildId(), (name,module) -> module.on(e));
    }
    public void on(CategoryUpdateEvent e){
        exec(e.getCurrent().getGuildId(), (name,module) -> module.on(e));
    }

    // Text Channels
    public void on(TextChannelCreateEvent e){
        exec(e.getChannel().getGuildId(), (name,module) -> module.on(e));
    }
    public void on(TextChannelDeleteEvent e){
        exec(e.getChannel().getGuildId(), (name,module) -> module.on(e));
    }
    public void on(TextChannelUpdateEvent e){
        exec(e.getCurrent().getGuildId(), (name,module) -> module.on(e));
    }

    // Voice Channels
    public void on(VoiceChannelCreateEvent e){
        exec(e.getChannel().getGuildId(), (name,module) -> module.on(e));
    }
    public void on(VoiceChannelDeleteEvent e){
        exec(e.getChannel().getGuildId(), (name,module) -> module.on(e));
    }
    public void on(VoiceChannelUpdateEvent e){
        exec(e.getCurrent().getGuildId(), (name,module) -> module.on(e));
    }


    /** MessageEvents **/
    // Message updates
    public void on(MessageCreateEvent e){
        exec(e.getGuildId().orElse(null), (name,module) -> module.on(e));
    }
    public void on(MessageDeleteEvent e){
        exec(chToGuildMap.get(e.getChannelId()), (name,module) -> module.on(e));
    }
    public void on(MessageUpdateEvent e){
        exec(chToGuildMap.get(e.getChannelId()), (name,module) -> module.on(e));
    }
    public void on(MessageBulkDeleteEvent e){
        exec(e.getGuildId(), (name,module) -> module.on(e));
    }

    // Reactions
    public void on(ReactionAddEvent e){
        exec(e.getGuildId().orElse(null), (name,module) -> module.on(e));
    }
    public void on(ReactionRemoveEvent e){
        exec(e.getGuildId().orElse(null), (name,module) -> module.on(e));
    }
    public void on(ReactionRemoveAllEvent e){
        exec(e.getGuildId().orElse(null), (name,module) -> module.on(e));
    }

}
