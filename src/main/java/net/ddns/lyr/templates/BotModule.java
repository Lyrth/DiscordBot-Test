package net.ddns.lyr.templates;

import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import discord4j.core.event.domain.channel.ChannelEvent;
import discord4j.core.event.domain.channel.TypingStartEvent;
import net.ddns.lyr.annotations.Excluded;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public abstract class BotModule implements Module {

    /** Other Events **/
    @Excluded public void on(PresenceUpdateEvent e){}
    @Excluded public void on(UserUpdateEvent e){}
    @Excluded public void on(VoiceServerUpdateEvent e){}
    @Excluded public void on(VoiceStateUpdateEvent e){}
    @Excluded public void on(WebhooksUpdateEvent e){}


    /** LifecycleEvents **/
    @Excluded public void on(GatewayLifecycleEvent e){}

    @Excluded public void on(ReadyEvent e){}
    @Excluded public void on(ResumeEvent e){}
    @Excluded public void on(ConnectEvent e){}
    @Excluded public void on(DisconnectEvent e){}
    @Excluded public void on(ReconnectEvent e){}
    @Excluded public void on(ReconnectStartEvent e){}
    @Excluded public void on(ReconnectFailEvent e){}


    /** GuildEvents **/
    @Excluded public void on(GuildEvent e){}

    // Other
    @Excluded public void on(EmojisUpdateEvent e){}
    @Excluded public void on(IntegrationsUpdateEvent e){}

    // Guild updates
    @Excluded public void on(GuildCreateEvent e){}
    @Excluded public void on(GuildDeleteEvent e){}
    @Excluded public void on(GuildUpdateEvent e){}

    // Member updates
    @Excluded public void on(MemberJoinEvent e){}
    @Excluded public void on(MemberLeaveEvent e){}
    @Excluded public void on(MemberUpdateEvent e){}
    @Excluded public void on(MemberChunkEvent e){}

    // Bans/Unbans
    @Excluded public void on(BanEvent e){}
    @Excluded public void on(UnbanEvent e){}


    /** RoleEvents **/
    @Excluded public void on(RoleEvent e){}

    // Role updates
    @Excluded public void on(RoleCreateEvent e){}
    @Excluded public void on(RoleDeleteEvent e){}
    @Excluded public void on(RoleUpdateEvent e){}


    /** ChannelEvents **/
    @Excluded public void on(ChannelEvent e){}

    // Other
    @Excluded public void on(PinsUpdateEvent e){}
    @Excluded public void on(TypingStartEvent e){}

    // Private Channels / DMs
    @Excluded public void on(PrivateChannelCreateEvent e){}
    @Excluded public void on(PrivateChannelDeleteEvent e){}

    // Guild Categories
    @Excluded public void on(CategoryCreateEvent e){}
    @Excluded public void on(CategoryDeleteEvent e){}
    @Excluded public void on(CategoryUpdateEvent e){}

    // Text Channels
    @Excluded public void on(TextChannelCreateEvent e){}
    @Excluded public void on(TextChannelDeleteEvent e){}
    @Excluded public void on(TextChannelUpdateEvent e){}

    // Voice Channels
    @Excluded public void on(VoiceChannelCreateEvent e){}
    @Excluded public void on(VoiceChannelDeleteEvent e){}
    @Excluded public void on(VoiceChannelUpdateEvent e){}


    /** MessageEvents **/
    @Excluded public void on(MessageEvent e){}

    // Message updates
    @Excluded public void on(MessageCreateEvent e){}
    @Excluded public void on(MessageDeleteEvent e){}
    @Excluded public void on(MessageUpdateEvent e){}

    @Excluded public void on(ReactionAddEvent e){}
    @Excluded public void on(ReactionRemoveEvent e){}
    @Excluded public void on(ReactionRemoveAllEvent e){}

    @Excluded public void on(MessageBulkDeleteEvent e){}

}
