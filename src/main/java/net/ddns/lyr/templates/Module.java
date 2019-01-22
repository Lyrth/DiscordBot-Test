package net.ddns.lyr.templates;


import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import reactor.core.Disposable;

public abstract class Module {

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public Disposable subscribeTo(EventDispatcher dispatcher, String eventName){
        switch (eventName) {
            case "PresenceUpdateEvent":
                return dispatcher.on(PresenceUpdateEvent.class).doOnNext(this::on).subscribe();
            case "UserUpdateEvent":
                return dispatcher.on(UserUpdateEvent.class).doOnNext(this::on).subscribe();
            case "VoiceServerUpdateEvent":
                return dispatcher.on(VoiceServerUpdateEvent.class).doOnNext(this::on).subscribe();
            case "VoiceStateUpdateEvent":
                return dispatcher.on(VoiceStateUpdateEvent.class).doOnNext(this::on).subscribe();
            case "WebhooksUpdateEvent":
                return dispatcher.on(WebhooksUpdateEvent.class).doOnNext(this::on).subscribe();

            case "ReadyEvent":
                return dispatcher.on(ReadyEvent.class).doOnNext(this::on).subscribe();
            case "ResumeEvent":
                return dispatcher.on(ResumeEvent.class).doOnNext(this::on).subscribe();
            case "ConnectEvent":
                return dispatcher.on(ConnectEvent.class).doOnNext(this::on).subscribe();
            case "DisconnectEvent":
                return dispatcher.on(DisconnectEvent.class).doOnNext(this::on).subscribe();
            case "ReconnectEvent":
                return dispatcher.on(ReconnectEvent.class).doOnNext(this::on).subscribe();
            case "ReconnectStartEvent":
                return dispatcher.on(ReconnectStartEvent.class).doOnNext(this::on).subscribe();
            case "ReconnectFailEvent":
                return dispatcher.on(ReconnectFailEvent.class).doOnNext(this::on).subscribe();

            case "EmojisUpdateEvent":
                return dispatcher.on(EmojisUpdateEvent.class).doOnNext(this::on).subscribe();
            case "IntegrationsUpdateEvent":
                return dispatcher.on(IntegrationsUpdateEvent.class).doOnNext(this::on).subscribe();
            case "GuildCreateEvent":
                return dispatcher.on(GuildCreateEvent.class).doOnNext(this::on).subscribe();
            case "GuildDeleteEvent":
                return dispatcher.on(GuildDeleteEvent.class).doOnNext(this::on).subscribe();
            case "GuildUpdateEvent":
                return dispatcher.on(GuildUpdateEvent.class).doOnNext(this::on).subscribe();
            case "MemberJoinEvent":
                return dispatcher.on(MemberJoinEvent.class).doOnNext(this::on).subscribe();
            case "MemberLeaveEvent":
                return dispatcher.on(MemberLeaveEvent.class).doOnNext(this::on).subscribe();
            case "MemberUpdateEvent":
                return dispatcher.on(MemberUpdateEvent.class).doOnNext(this::on).subscribe();
            case "MemberChunkEvent":
                return dispatcher.on(MemberChunkEvent.class).doOnNext(this::on).subscribe();
            case "BanEvent":
                return dispatcher.on(BanEvent.class).doOnNext(this::on).subscribe();
            case "UnbanEvent":
                return dispatcher.on(UnbanEvent.class).doOnNext(this::on).subscribe();

            case "RoleCreateEvent":
                return dispatcher.on(RoleCreateEvent.class).doOnNext(this::on).subscribe();
            case "RoleDeleteEvent":
                return dispatcher.on(RoleDeleteEvent.class).doOnNext(this::on).subscribe();
            case "RoleUpdateEvent":
                return dispatcher.on(RoleUpdateEvent.class).doOnNext(this::on).subscribe();

            case "PinsUpdateEvent":
                return dispatcher.on(PinsUpdateEvent.class).doOnNext(this::on).subscribe();
            case "TypingStartEvent":
                return dispatcher.on(TypingStartEvent.class).doOnNext(this::on).subscribe();
            case "PrivateChannelCreateEvent":
                return dispatcher.on(PrivateChannelCreateEvent.class).doOnNext(this::on).subscribe();
            case "PrivateChannelDeleteEvent":
                return dispatcher.on(PrivateChannelDeleteEvent.class).doOnNext(this::on).subscribe();
            case "CategoryCreateEvent":
                return dispatcher.on(CategoryCreateEvent.class).doOnNext(this::on).subscribe();
            case "CategoryDeleteEvent":
                return dispatcher.on(CategoryDeleteEvent.class).doOnNext(this::on).subscribe();
            case "CategoryUpdateEvent":
                return dispatcher.on(CategoryUpdateEvent.class).doOnNext(this::on).subscribe();
            case "TextChannelCreateEvent":
                return dispatcher.on(TextChannelCreateEvent.class).doOnNext(this::on).subscribe();
            case "TextChannelDeleteEvent":
                return dispatcher.on(TextChannelDeleteEvent.class).doOnNext(this::on).subscribe();
            case "TextChannelUpdateEvent":
                return dispatcher.on(TextChannelUpdateEvent.class).doOnNext(this::on).subscribe();
            case "VoiceChannelCreateEvent":
                return dispatcher.on(VoiceChannelCreateEvent.class).doOnNext(this::on).subscribe();
            case "VoiceChannelDeleteEvent":
                return dispatcher.on(VoiceChannelDeleteEvent.class).doOnNext(this::on).subscribe();
            case "VoiceChannelUpdateEvent":
                return dispatcher.on(VoiceChannelUpdateEvent.class).doOnNext(this::on).subscribe();

            case "MessageCreateEvent":
                return dispatcher.on(MessageCreateEvent.class).doOnNext(this::on).subscribe();
            case "MessageDeleteEvent":
                return dispatcher.on(MessageDeleteEvent.class).doOnNext(this::on).subscribe();
            case "MessageUpdateEvent":
                return dispatcher.on(MessageUpdateEvent.class).doOnNext(this::on).subscribe();
            case "ReactionAddEvent":
                return dispatcher.on(ReactionAddEvent.class).doOnNext(this::on).subscribe();
            case "ReactionRemoveEvent":
                return dispatcher.on(ReactionRemoveEvent.class).doOnNext(this::on).subscribe();
            case "ReactionRemoveAllEvent":
                return dispatcher.on(ReactionRemoveAllEvent.class).doOnNext(this::on).subscribe();
            case "MessageBulkDeleteEvent":
                return dispatcher.on(MessageBulkDeleteEvent.class).doOnNext(this::on).subscribe();
            default:
                throw new IllegalArgumentException("Event " + eventName + " does not exist.");
        }
    }

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
