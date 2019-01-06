package net.ddns.lyr.templates;

import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Mono;

public abstract class GuildModule extends Module {

    protected Mono<Guild> guild;
    protected Snowflake guildId;
    protected GuildSetting guildSettings;

    public abstract GuildModule newInstance(Mono<Guild> guild, GuildSetting guildSettings);
    public GuildModule(Mono<Guild> guild, GuildSetting guildSettings){
        this.guild = guild;
        this.guildSettings = guildSettings;
        this.guildId = Snowflake.of(guildSettings.guildId);
    }
    public GuildModule(){}

    /** Other Events **/
    void on(PresenceUpdateEvent e){}
    void on(UserUpdateEvent e){}
    void on(VoiceServerUpdateEvent e){}
    void on(VoiceStateUpdateEvent e){}
    void on(WebhooksUpdateEvent e){}


    /** LifecycleEvents **/
    void on(ReadyEvent e){}
    void on(ResumeEvent e){}
    void on(ConnectEvent e){}
    void on(DisconnectEvent e){}
    void on(ReconnectEvent e){}
    void on(ReconnectStartEvent e){}
    void on(ReconnectFailEvent e){}


    /** GuildEvents **/
    // Other
    void on(EmojisUpdateEvent e){}
    void on(IntegrationsUpdateEvent e){}

    // Guild updates
    void on(GuildCreateEvent e){}
    void on(GuildDeleteEvent e){}
    void on(GuildUpdateEvent e){}

    // Member updates
    void on(MemberJoinEvent e){}
    void on(MemberLeaveEvent e){}
    void on(MemberUpdateEvent e){}
    void on(MemberChunkEvent e){}

    // Bans/Unbans
    void on(BanEvent e){}
    void on(UnbanEvent e){}


    /** RoleEvents **/
    // Role updates
    void on(RoleCreateEvent e){}
    void on(RoleDeleteEvent e){}
    void on(RoleUpdateEvent e){}


    /** ChannelEvents **/
    // Other
    void on(PinsUpdateEvent e){}
    void on(TypingStartEvent e){}

    // Private Channels / DMs
    void on(PrivateChannelCreateEvent e){}
    void on(PrivateChannelDeleteEvent e){}

    // Guild Categories
    void on(CategoryCreateEvent e){}
    void on(CategoryDeleteEvent e){}
    void on(CategoryUpdateEvent e){}

    // Text Channels
    void on(TextChannelCreateEvent e){}
    void on(TextChannelDeleteEvent e){}
    void on(TextChannelUpdateEvent e){}

    // Voice Channels
    void on(VoiceChannelCreateEvent e){}
    void on(VoiceChannelDeleteEvent e){}
    void on(VoiceChannelUpdateEvent e){}


    /** MessageEvents **/
    // Message updates
    void on(MessageCreateEvent e){}
    void on(MessageDeleteEvent e){}
    void on(MessageUpdateEvent e){}

    void on(ReactionAddEvent e){}
    void on(ReactionRemoveEvent e){}
    void on(ReactionRemoveAllEvent e){}

    void on(MessageBulkDeleteEvent e){}

}
