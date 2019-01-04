package net.ddns.lyr.templates;


import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;

public abstract class Module {
    public abstract String getName();

    /** Other Events **/
    abstract void on(PresenceUpdateEvent e);
    abstract void on(UserUpdateEvent e);
    abstract void on(VoiceServerUpdateEvent e);
    abstract void on(VoiceStateUpdateEvent e);
    abstract void on(WebhooksUpdateEvent e);


    /** LifecycleEvents **/
    abstract void on(ReadyEvent e);
    abstract void on(ResumeEvent e);
    abstract void on(ConnectEvent e);
    abstract void on(DisconnectEvent e);
    abstract void on(ReconnectEvent e);
    abstract void on(ReconnectStartEvent e);
    abstract void on(ReconnectFailEvent e);


    /** GuildEvents **/
    // Other
    abstract void on(EmojisUpdateEvent e);
    abstract void on(IntegrationsUpdateEvent e);

    // Guild updates
    abstract void on(GuildCreateEvent e);
    abstract void on(GuildDeleteEvent e);
    abstract void on(GuildUpdateEvent e);

    // Member updates
    abstract void on(MemberJoinEvent e);
    abstract void on(MemberLeaveEvent e);
    abstract void on(MemberUpdateEvent e);
    abstract void on(MemberChunkEvent e);

    // Bans/Unbans
    abstract void on(BanEvent e);
    abstract void on(UnbanEvent e);


    /** RoleEvents **/
    // Role updates
    abstract void on(RoleCreateEvent e);
    abstract void on(RoleDeleteEvent e);
    abstract void on(RoleUpdateEvent e);


    /** ChannelEvents **/
    // Other
    abstract void on(PinsUpdateEvent e);
    abstract void on(TypingStartEvent e);

    // Private Channels / DMs
    abstract void on(PrivateChannelCreateEvent e);
    abstract void on(PrivateChannelDeleteEvent e);

    // Guild Categories
    abstract void on(CategoryCreateEvent e);
    abstract void on(CategoryDeleteEvent e);
    abstract void on(CategoryUpdateEvent e);

    // Text Channels
    abstract void on(TextChannelCreateEvent e);
    abstract void on(TextChannelDeleteEvent e);
    abstract void on(TextChannelUpdateEvent e);

    // Voice Channels
    abstract void on(VoiceChannelCreateEvent e);
    abstract void on(VoiceChannelDeleteEvent e);
    abstract void on(VoiceChannelUpdateEvent e);


    /** MessageEvents **/
    // Message updates
    abstract void on(MessageCreateEvent e);
    abstract void on(MessageDeleteEvent e);
    abstract void on(MessageUpdateEvent e);

    abstract void on(ReactionAddEvent e);
    abstract void on(ReactionRemoveEvent e);
    abstract void on(ReactionRemoveAllEvent e);

    abstract void on(MessageBulkDeleteEvent e);
}
