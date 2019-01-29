package lyr.testbot.enums;

import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.*;

public enum EventType {

    PresenceUpdate(PresenceUpdateEvent.class),
    UserUpdate(UserUpdateEvent.class),
    VoiceServerUpdate(VoiceServerUpdateEvent.class),
    VoiceStateUpdate(VoiceStateUpdateEvent.class),
    WebhooksUpdate(WebhooksUpdateEvent.class),
    ;

    public Class<? extends Event> e;
    <T extends Event> EventType(Class<T> c){
        this.e = c;
    }

    public enum All {

        /** Other **/
        PresenceUpdate(PresenceUpdateEvent.class),
        UserUpdate(UserUpdateEvent.class),
        VoiceServerUpdate(VoiceServerUpdateEvent.class),
        VoiceStateUpdate(VoiceStateUpdateEvent.class),
        WebhooksUpdate(WebhooksUpdateEvent.class),


        /** Lifecycle **/
        GatewayLifecycle(discord4j.core.event.domain.lifecycle.GatewayLifecycleEvent.class),

        Ready(ReadyEvent.class),
        Resume(ResumeEvent.class),
        Connect(ConnectEvent.class),
        Disconnect(DisconnectEvent.class),
        Reconnect(ReconnectEvent.class),
        ReconnectStart(ReconnectStartEvent.class),
        ReconnectFail(ReconnectFailEvent.class),


        /** Guild **/
        Guild(discord4j.core.event.domain.guild.GuildEvent.class),

        // Other
        EmojisUpdate(EmojisUpdateEvent.class),
        IntegrationsUpdate(IntegrationsUpdateEvent.class),

        // Guild updates
        GuildCreate(GuildCreateEvent.class),
        GuildDelete(GuildDeleteEvent.class),
        GuildUpdate(GuildUpdateEvent.class),

        // Member updates
        MemberJoin(MemberJoinEvent.class),
        MemberLeave(MemberLeaveEvent.class),
        MemberUpdate(MemberUpdateEvent.class),
        MemberChunk(MemberChunkEvent.class),

        // Bans/Unbans
        Ban(BanEvent.class),
        Unban(UnbanEvent.class),


        /** Role **/
        Role(discord4j.core.event.domain.role.RoleEvent.class),

        // Role updates
        RoleCreate(RoleCreateEvent.class),
        RoleDelete(RoleDeleteEvent.class),
        RoleUpdate(RoleUpdateEvent.class),


        /** Channel **/
        Channel(discord4j.core.event.domain.channel.ChannelEvent.class),

        // Other
        PinsUpdate(PinsUpdateEvent.class),
        TypingStart(TypingStartEvent.class),

        // Private Channels / DMs
        PrivateChannelCreate(PrivateChannelCreateEvent.class),
        PrivateChannelDelete(PrivateChannelDeleteEvent.class),

        // Guild Categories
        CategoryCreate(CategoryCreateEvent.class),
        CategoryDelete(CategoryDeleteEvent.class),
        CategoryUpdate(CategoryUpdateEvent.class),

        // Text Channels
        TextChannelCreate(TextChannelCreateEvent.class),
        TextChannelDelete(TextChannelDeleteEvent.class),
        TextChannelUpdate(TextChannelUpdateEvent.class),

        // Voice Channels
        VoiceChannelCreate(VoiceChannelCreateEvent.class),
        VoiceChannelDelete(VoiceChannelDeleteEvent.class),
        VoiceChannelUpdate(VoiceChannelUpdateEvent.class),


        /** Message **/
        Message(discord4j.core.event.domain.message.MessageEvent.class),

        // Message updates
        MessageCreate(MessageCreateEvent.class),
        MessageDelete(MessageDeleteEvent.class),
        MessageUpdate(MessageUpdateEvent.class),

        ReactionAdd(ReactionAddEvent.class),
        ReactionRemove(ReactionRemoveEvent.class),
        ReactionRemoveAll(ReactionRemoveAllEvent.class),

        MessageBulkDelete(MessageBulkDeleteEvent.class),
        ;

        public Class<? extends Event> e;
        <T extends Event> All(Class<T> c){
            this.e = c;
        }
    }

    public enum Lifecycle {

        // Generic Lifecycle event
        GatewayLifecycle(discord4j.core.event.domain.lifecycle.GatewayLifecycleEvent.class),

        Ready(ReadyEvent.class),
        Resume(ResumeEvent.class),
        Connect(ConnectEvent.class),
        Disconnect(DisconnectEvent.class),
        Reconnect(ReconnectEvent.class),
        ReconnectStart(ReconnectStartEvent.class),
        ReconnectFail(ReconnectFailEvent.class),
        ;

        public Class<? extends GatewayLifecycleEvent> e;
        <T extends GatewayLifecycleEvent> Lifecycle(Class<T> c){
            this.e = c;
        }
    }

    public enum Guild {
        
        // Generic Guild event
        Guild(discord4j.core.event.domain.guild.GuildEvent.class),
        
        // Other
        EmojisUpdate(EmojisUpdateEvent.class),
        IntegrationsUpdate(IntegrationsUpdateEvent.class),
        
        // Guild updates
        GuildCreate(GuildCreateEvent.class),
        GuildDelete(GuildDeleteEvent.class),
        GuildUpdate(GuildUpdateEvent.class),

        // Member updates
        MemberJoin(MemberJoinEvent.class),
        MemberLeave(MemberLeaveEvent.class),
        MemberUpdate(MemberUpdateEvent.class),
        MemberChunk(MemberChunkEvent.class),
        
        // Bans/Unbans
        Ban(BanEvent.class),
        Unban(UnbanEvent.class),
        ;

        public Class<? extends GuildEvent> e;
        <T extends GuildEvent> Guild(Class<T> c){
            this.e = c;
        }
    }
    
    public enum Role {

        // Generic Role event
        Role(discord4j.core.event.domain.role.RoleEvent.class),

        // Role updates
        RoleCreate(RoleCreateEvent.class),
        RoleDelete(RoleDeleteEvent.class),
        RoleUpdate(RoleUpdateEvent.class),
        ;
        
        public Class<? extends RoleEvent> e;
        <T extends RoleEvent> Role(Class<T> c){
            this.e = c;
        }
    }

    public enum Channel {

        // Generic Channel event
        Channel(discord4j.core.event.domain.channel.ChannelEvent.class),

        // Other
        PinsUpdate(PinsUpdateEvent.class),
        TypingStart(TypingStartEvent.class),

        // Private Channels / DMs
        PrivateChannelCreate(PrivateChannelCreateEvent.class),
        PrivateChannelDelete(PrivateChannelDeleteEvent.class),

        // Guild Categories
        CategoryCreate(CategoryCreateEvent.class),
        CategoryDelete(CategoryDeleteEvent.class),
        CategoryUpdate(CategoryUpdateEvent.class),

        // Text Channels
        TextChannelCreate(TextChannelCreateEvent.class),
        TextChannelDelete(TextChannelDeleteEvent.class),
        TextChannelUpdate(TextChannelUpdateEvent.class),

        // Voice Channels
        VoiceChannelCreate(VoiceChannelCreateEvent.class),
        VoiceChannelDelete(VoiceChannelDeleteEvent.class),
        VoiceChannelUpdate(VoiceChannelUpdateEvent.class),
        ;

        public Class<? extends ChannelEvent> e;
        <T extends ChannelEvent> Channel(Class<T> c){
            this.e = c;
        }
    }

    public enum Message {
        
        // Generic Message event
        Message(discord4j.core.event.domain.message.MessageEvent.class),

        // Message updates
        MessageCreate(MessageCreateEvent.class),
        MessageDelete(MessageDeleteEvent.class),
        MessageUpdate(MessageUpdateEvent.class),
        
        ReactionAdd(ReactionAddEvent.class),
        ReactionRemove(ReactionRemoveEvent.class),
        ReactionRemoveAll(ReactionRemoveAllEvent.class),
        
        MessageBulkDelete(MessageBulkDeleteEvent.class),
        ;

        public Class<? extends MessageEvent> e;
        <T extends MessageEvent> Message(Class<T> c){
            this.e = c;
        }
    }


}
