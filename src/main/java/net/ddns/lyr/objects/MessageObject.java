package net.ddns.lyr.objects;

import discord4j.core.object.Embed;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class MessageObject {

    Mono<Message> M;

    public Mono<Snowflake> id;
    public Mono<String> contents;
    public Mono<List<Embed>> embeds;

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
        embeds = message.map(Message::getEmbeds);

        channelId = message.map(Message::getChannelId);
        // guildId
        member = author = message.flatMap(Message::getAuthorAsMember);
        user = message.flatMap(Message::getAuthor);
        userId = message.map(m -> m.getAuthorId().orElse(null));
    }

}
