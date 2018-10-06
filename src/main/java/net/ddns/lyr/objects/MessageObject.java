package net.ddns.lyr.objects;

import discord4j.core.object.Embed;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

public abstract class MessageObject {
/**
    Mono<Message> M;

    public Snowflake id;
    public String contents;

    public Snowflake channelId;
    public Snowflake guildId;
    public GuildMemberObject member;
    public UserObject user;
    public Snowflake userId;

    MessageObject(){}

    MessageObject(Mono<Message> message){
        M = message;
        message.subscribe(m -> {
            id = m.getId();
            contents = m.getContent().orElse("");
            channelId = m.getChannelId();
            // guildId =
            userId = m.getAuthorId().orElse(m.getWebhookId().orElse(null));
            user = new UserObject(m.getAuthor());
        });
    }

    public GuildMemberObject getAuthor() {
        M.subscribe(m ->
            member = new GuildMemberObject(m.getAuthorAsMember())
        );
        return member;
    }
    */
    Mono<Message> M;

    public Mono<Snowflake> id;
    public Mono<String> contents;
    public Mono<Embed> embed;

    public Mono<Snowflake> channelId;
    public Mono<Snowflake> guildId;
    public Mono<Member> member,author;
    public Mono<User> user;
    public Mono<Snowflake> userId;

    MessageObject(){}

    MessageObject(Mono<Message> message) {
        M = message;

        id = message.map(Message::getId);
        contents = message.map(m -> m.getContent().orElse(null));
        embed = message.map(m -> m.getEmbeds().get(0));

        channelId = message.map(Message::getChannelId);
        // guildId
        member = author = message.flatMap(Message::getAuthorAsMember);
        user = message.flatMap(Message::getAuthor);
        userId = message.map(m -> m.getAuthorId().orElse(null));
    }

}
