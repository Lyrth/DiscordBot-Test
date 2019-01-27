package lyr.testbot.templates;

import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;

public abstract class BotModule extends Module {

    /** Other Events **/
    public void on(PresenceUpdateEvent e){}
    public void on(UserUpdateEvent e){}
    public void on(VoiceServerUpdateEvent e){}
    public void on(VoiceStateUpdateEvent e){}
    public void on(WebhooksUpdateEvent e){}


    /** LifecycleEvents **/
    public void on(ReadyEvent e){}
    public void on(ResumeEvent e){}
    public void on(ConnectEvent e){}
    public void on(DisconnectEvent e){}
    public void on(ReconnectEvent e){}
    public void on(ReconnectStartEvent e){}
    public void on(ReconnectFailEvent e){}


    /** GuildEvents **/
    // Other
    public void on(EmojisUpdateEvent e){}
    public void on(IntegrationsUpdateEvent e){}

    // Guild updates
    public void on(GuildCreateEvent e){}
    public void on(GuildDeleteEvent e){}
    public void on(GuildUpdateEvent e){}

    // Member updates
    public void on(MemberJoinEvent e){}
    public void on(MemberLeaveEvent e){}
    public void on(MemberUpdateEvent e){}
    public void on(MemberChunkEvent e){}

    // Bans/Unbans
    public void on(BanEvent e){}
    public void on(UnbanEvent e){}


    /** RoleEvents **/
    // Role updates
    public void on(RoleCreateEvent e){}
    public void on(RoleDeleteEvent e){}
    public void on(RoleUpdateEvent e){}


    /** ChannelEvents **/
    // Other
    public void on(PinsUpdateEvent e){}
    public void on(TypingStartEvent e){}

    // Private Channels / DMs
    public void on(PrivateChannelCreateEvent e){}
    public void on(PrivateChannelDeleteEvent e){}

    // Guild Categories
    public void on(CategoryCreateEvent e){}
    public void on(CategoryDeleteEvent e){}
    public void on(CategoryUpdateEvent e){}

    // Text Channels
    public void on(TextChannelCreateEvent e){}
    public void on(TextChannelDeleteEvent e){}
    public void on(TextChannelUpdateEvent e){}

    // Voice Channels
    public void on(VoiceChannelCreateEvent e){}
    public void on(VoiceChannelDeleteEvent e){}
    public void on(VoiceChannelUpdateEvent e){}


    /** MessageEvents **/
    // Message updates
    public void on(MessageCreateEvent e){}
    public void on(MessageDeleteEvent e){}
    public void on(MessageUpdateEvent e){}

    public void on(ReactionAddEvent e){}
    public void on(ReactionRemoveEvent e){}
    public void on(ReactionRemoveAllEvent e){}

    public void on(MessageBulkDeleteEvent e){}

}
