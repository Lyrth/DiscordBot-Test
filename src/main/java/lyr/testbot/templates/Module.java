package lyr.testbot.templates;


import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.event.*;
import lyr.testbot.main.Main;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.objects.annotstore.ModuleInfoObj;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "__Module_Base__")
public abstract class Module {

    protected final ModuleInfoObj moduleInfo = new ModuleInfoObj(this.getClass());

    public String getName() {
        return moduleInfo.name();
    }

    public List<String> getAliases(){
        return Arrays.asList(moduleInfo.aliases());
    }

    public String getDesc(){
        return moduleInfo.desc();
    }

    public List<Class<Command>> getCommands(){
        return Arrays.asList(moduleInfo.commands());
    }

    protected ClientObject getClient(){
        return Main.client;
    }

    public Disposable subscribeTo(EventDispatcher dispatcher, String eventName){
        switch (eventName) {
            case "PresenceUpdateEvent":
                return dispatcher.on(PresenceUpdateEvent.class).flatMap(this::on).subscribe();
            case "UserUpdateEvent":
                return dispatcher.on(UserUpdateEvent.class).flatMap(this::on).subscribe();
            case "VoiceServerUpdateEvent":
                return dispatcher.on(VoiceServerUpdateEvent.class).flatMap(this::on).subscribe();
            case "VoiceStateUpdateEvent":
                return dispatcher.on(VoiceStateUpdateEvent.class).flatMap(this::on).subscribe();
            case "WebhooksUpdateEvent":
                return dispatcher.on(WebhooksUpdateEvent.class).flatMap(this::on).subscribe();

            case "ReadyEvent":
                return dispatcher.on(ReadyEvent.class).flatMap(this::on).subscribe();
            case "ResumeEvent":
                return dispatcher.on(ResumeEvent.class).flatMap(this::on).subscribe();
            case "ConnectEvent":
                return dispatcher.on(ConnectEvent.class).flatMap(this::on).subscribe();
            case "DisconnectEvent":
                return dispatcher.on(DisconnectEvent.class).flatMap(this::on).subscribe();
            case "ReconnectEvent":
                return dispatcher.on(ReconnectEvent.class).flatMap(this::on).subscribe();
            case "ReconnectStartEvent":
                return dispatcher.on(ReconnectStartEvent.class).flatMap(this::on).subscribe();
            case "ReconnectFailEvent":
                return dispatcher.on(ReconnectFailEvent.class).flatMap(this::on).subscribe();

            case "EmojisUpdateEvent":
                return dispatcher.on(EmojisUpdateEvent.class).flatMap(this::on).subscribe();
            case "IntegrationsUpdateEvent":
                return dispatcher.on(IntegrationsUpdateEvent.class).flatMap(this::on).subscribe();
            case "GuildCreateEvent":
                return dispatcher.on(GuildCreateEvent.class).flatMap(this::on).subscribe();
            case "GuildDeleteEvent":
                return dispatcher.on(GuildDeleteEvent.class).flatMap(this::on).subscribe();
            case "GuildUpdateEvent":
                return dispatcher.on(GuildUpdateEvent.class).flatMap(this::on).subscribe();
            case "MemberJoinEvent":
                return dispatcher.on(MemberJoinEvent.class).flatMap(this::on).subscribe();
            case "MemberLeaveEvent":
                return dispatcher.on(MemberLeaveEvent.class).flatMap(this::on).subscribe();
            case "MemberUpdateEvent":
                return dispatcher.on(MemberUpdateEvent.class).flatMap(this::on).subscribe();
            case "MemberChunkEvent":
                return dispatcher.on(MemberChunkEvent.class).flatMap(this::on).subscribe();
            case "BanEvent":
                return dispatcher.on(BanEvent.class).flatMap(this::on).subscribe();
            case "UnbanEvent":
                return dispatcher.on(UnbanEvent.class).flatMap(this::on).subscribe();

            case "RoleCreateEvent":
                return dispatcher.on(RoleCreateEvent.class).flatMap(this::on).subscribe();
            case "RoleDeleteEvent":
                return dispatcher.on(RoleDeleteEvent.class).flatMap(this::on).subscribe();
            case "RoleUpdateEvent":
                return dispatcher.on(RoleUpdateEvent.class).flatMap(this::on).subscribe();

            case "PinsUpdateEvent":
                return dispatcher.on(PinsUpdateEvent.class).flatMap(this::on).subscribe();
            case "TypingStartEvent":
                return dispatcher.on(TypingStartEvent.class).flatMap(this::on).subscribe();
            case "PrivateChannelCreateEvent":
                return dispatcher.on(PrivateChannelCreateEvent.class).flatMap(this::on).subscribe();
            case "PrivateChannelDeleteEvent":
                return dispatcher.on(PrivateChannelDeleteEvent.class).flatMap(this::on).subscribe();
            case "CategoryCreateEvent":
                return dispatcher.on(CategoryCreateEvent.class).flatMap(this::on).subscribe();
            case "CategoryDeleteEvent":
                return dispatcher.on(CategoryDeleteEvent.class).flatMap(this::on).subscribe();
            case "CategoryUpdateEvent":
                return dispatcher.on(CategoryUpdateEvent.class).flatMap(this::on).subscribe();
            case "TextChannelCreateEvent":
                return dispatcher.on(TextChannelCreateEvent.class).flatMap(this::on).subscribe();
            case "TextChannelDeleteEvent":
                return dispatcher.on(TextChannelDeleteEvent.class).flatMap(this::on).subscribe();
            case "TextChannelUpdateEvent":
                return dispatcher.on(TextChannelUpdateEvent.class).flatMap(this::on).subscribe();
            case "VoiceChannelCreateEvent":
                return dispatcher.on(VoiceChannelCreateEvent.class).flatMap(this::on).subscribe();
            case "VoiceChannelDeleteEvent":
                return dispatcher.on(VoiceChannelDeleteEvent.class).flatMap(this::on).subscribe();
            case "VoiceChannelUpdateEvent":
                return dispatcher.on(VoiceChannelUpdateEvent.class).flatMap(this::on).subscribe();

            case "MessageCreateEvent":
                return dispatcher.on(MessageCreateEvent.class).flatMap(this::on).subscribe();
            case "MessageDeleteEvent":
                return dispatcher.on(MessageDeleteEvent.class).flatMap(this::on).subscribe();
            case "MessageUpdateEvent":
                return dispatcher.on(MessageUpdateEvent.class).flatMap(this::on).subscribe();
            case "ReactionAddEvent":
                return dispatcher.on(ReactionAddEvent.class).flatMap(this::on).subscribe();
            case "ReactionRemoveEvent":
                return dispatcher.on(ReactionRemoveEvent.class).flatMap(this::on).subscribe();
            case "ReactionRemoveAllEvent":
                return dispatcher.on(ReactionRemoveAllEvent.class).flatMap(this::on).subscribe();
            case "MessageBulkDeleteEvent":
                return dispatcher.on(MessageBulkDeleteEvent.class).flatMap(this::on).subscribe();

            case "TenSecondEvent":
                return TenSecondEvent.onThis().flatMap(this::on).subscribe();
            case "OneHourEvent":
                return OneHourEvent.onThis().flatMap(this::on).subscribe();
            case "DailyEvent":
                return DailyEvent.onThis().flatMap(this::on).subscribe();

            default:
                throw new IllegalArgumentException("Event " + eventName + " does not exist.");
        }
    }

    /** Interval Events **/
    abstract Mono<Void> on(TenSecondEvent e);
    abstract Mono<Void> on(OneHourEvent e);
    abstract Mono<Void> on(DailyEvent e);


    /** Other Events **/
    abstract Mono<Void> on(PresenceUpdateEvent e);
    abstract Mono<Void> on(UserUpdateEvent e);
    abstract Mono<Void> on(VoiceServerUpdateEvent e);
    abstract Mono<Void> on(VoiceStateUpdateEvent e);
    abstract Mono<Void> on(WebhooksUpdateEvent e);


    /** LifecycleEvents **/
    abstract Mono<Void> on(ReadyEvent e);
    abstract Mono<Void> on(ResumeEvent e);
    abstract Mono<Void> on(ConnectEvent e);
    abstract Mono<Void> on(DisconnectEvent e);
    abstract Mono<Void> on(ReconnectEvent e);
    abstract Mono<Void> on(ReconnectStartEvent e);
    abstract Mono<Void> on(ReconnectFailEvent e);


    /** GuildEvents **/
    // Other
    abstract Mono<Void> on(EmojisUpdateEvent e);
    abstract Mono<Void> on(IntegrationsUpdateEvent e);

    // Guild updates
    abstract Mono<Void> on(GuildCreateEvent e);
    abstract Mono<Void> on(GuildDeleteEvent e);
    abstract Mono<Void> on(GuildUpdateEvent e);

    // Member updates
    abstract Mono<Void> on(MemberJoinEvent e);
    abstract Mono<Void> on(MemberLeaveEvent e);
    abstract Mono<Void> on(MemberUpdateEvent e);
    abstract Mono<Void> on(MemberChunkEvent e);

    // Bans/Unbans
    abstract Mono<Void> on(BanEvent e);
    abstract Mono<Void> on(UnbanEvent e);


    /** RoleEvents **/
    // Role updates
    abstract Mono<Void> on(RoleCreateEvent e);
    abstract Mono<Void> on(RoleDeleteEvent e);
    abstract Mono<Void> on(RoleUpdateEvent e);


    /** ChannelEvents **/
    // Other
    abstract Mono<Void> on(PinsUpdateEvent e);
    abstract Mono<Void> on(TypingStartEvent e);

    // Private Channels / DMs
    abstract Mono<Void> on(PrivateChannelCreateEvent e);
    abstract Mono<Void> on(PrivateChannelDeleteEvent e);

    // Guild Categories
    abstract Mono<Void> on(CategoryCreateEvent e);
    abstract Mono<Void> on(CategoryDeleteEvent e);
    abstract Mono<Void> on(CategoryUpdateEvent e);

    // Text Channels
    abstract Mono<Void> on(TextChannelCreateEvent e);
    abstract Mono<Void> on(TextChannelDeleteEvent e);
    abstract Mono<Void> on(TextChannelUpdateEvent e);

    // Voice Channels
    abstract Mono<Void> on(VoiceChannelCreateEvent e);
    abstract Mono<Void> on(VoiceChannelDeleteEvent e);
    abstract Mono<Void> on(VoiceChannelUpdateEvent e);


    /** MessageEvents **/
    // Message updates
    abstract Mono<Void> on(MessageCreateEvent e);
    abstract Mono<Void> on(MessageDeleteEvent e);
    abstract Mono<Void> on(MessageUpdateEvent e);

    abstract Mono<Void> on(ReactionAddEvent e);
    abstract Mono<Void> on(ReactionRemoveEvent e);
    abstract Mono<Void> on(ReactionRemoveAllEvent e);

    abstract Mono<Void> on(MessageBulkDeleteEvent e);
}
