package lyr.testbot.templates;

import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;
import lyr.testbot.event.*;
import reactor.core.publisher.Mono;

public abstract class BotModule extends Module {

    public static final Mono<Void> VOID = Mono.empty();

    /** Interval Events **/
    public Mono<Void> on(TenSecondEvent e){
        return VOID;
    }
    public Mono<Void> on(OneHourEvent e){
        return VOID;
    }
    public Mono<Void> on(DailyEvent e){
        return VOID;
    }


    /** Other Events **/
    public Mono<Void> on(PresenceUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(UserUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(VoiceServerUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(VoiceStateUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(WebhooksUpdateEvent e){
        return VOID;
    }


    /** LifecycleEvents **/
    public Mono<Void> on(ReadyEvent e){
        return VOID;
    }
    public Mono<Void> on(ResumeEvent e){
        return VOID;
    }
    public Mono<Void> on(ConnectEvent e){
        return VOID;
    }
    public Mono<Void> on(DisconnectEvent e){
        return VOID;
    }
    public Mono<Void> on(ReconnectEvent e){
        return VOID;
    }
    public Mono<Void> on(ReconnectStartEvent e){
        return VOID;
    }
    public Mono<Void> on(ReconnectFailEvent e){
        return VOID;
    }


    /** GuildEvents **/
    // Other
    public Mono<Void> on(EmojisUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(IntegrationsUpdateEvent e){
        return VOID;
    }

    // Guild updates
    public Mono<Void> on(GuildCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(GuildDeleteEvent e){
        return VOID;
    }
    public Mono<Void> on(GuildUpdateEvent e){
        return VOID;
    }

    // Member updates
    public Mono<Void> on(MemberJoinEvent e){
        return VOID;
    }
    public Mono<Void> on(MemberLeaveEvent e){
        return VOID;
    }
    public Mono<Void> on(MemberUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(MemberChunkEvent e){
        return VOID;
    }

    // Bans/Unbans
    public Mono<Void> on(BanEvent e){
        return VOID;
    }
    public Mono<Void> on(UnbanEvent e){
        return VOID;
    }


    /** RoleEvents **/
    // Role updates
    public Mono<Void> on(RoleCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(RoleDeleteEvent e){
        return VOID;
    }
    public Mono<Void> on(RoleUpdateEvent e){
        return VOID;
    }


    /** ChannelEvents **/
    // Other
    public Mono<Void> on(PinsUpdateEvent e){
        return VOID;
    }
    public Mono<Void> on(TypingStartEvent e){
        return VOID;
    }

    // Private Channels / DMs
    public Mono<Void> on(PrivateChannelCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(PrivateChannelDeleteEvent e){
        return VOID;
    }

    // Guild Categories
    public Mono<Void> on(CategoryCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(CategoryDeleteEvent e){
        return VOID;
    }
    public Mono<Void> on(CategoryUpdateEvent e){
        return VOID;
    }

    // Text Channels
    public Mono<Void> on(TextChannelCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(TextChannelDeleteEvent e){
        return VOID;
    }
    public Mono<Void> on(TextChannelUpdateEvent e){
        return VOID;
    }

    // Voice Channels
    public Mono<Void> on(VoiceChannelCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(VoiceChannelDeleteEvent e){
        return VOID;
    }
    public Mono<Void> on(VoiceChannelUpdateEvent e){
        return VOID;
    }


    /** MessageEvents **/
    // Message updates
    public Mono<Void> on(MessageCreateEvent e){
        return VOID;
    }
    public Mono<Void> on(MessageDeleteEvent e){
        return VOID;
    }
    public Mono<Void> on(MessageUpdateEvent e){
        return VOID;
    }

    public Mono<Void> on(ReactionAddEvent e){
        return VOID;
    }
    public Mono<Void> on(ReactionRemoveEvent e){
        return VOID;
    }
    public Mono<Void> on(ReactionRemoveAllEvent e){
        return VOID;
    }

    public Mono<Void> on(MessageBulkDeleteEvent e){
        return VOID;
    }

}
