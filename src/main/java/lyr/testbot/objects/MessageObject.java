package lyr.testbot.objects;

import discord4j.core.object.Embed;
import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public abstract class MessageObject {

    Mono<Message> M;

    public Mono<Snowflake> id;
    public Mono<String> contents;
    public Mono<List<Embed>> embeds;

    public Mono<Snowflake> channelId;
    public Mono<Snowflake> guildId;
    public Mono<Member> member,author;
    public Mono<Optional<User>> user;
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
        user = message.map(Message::getAuthor);
        userId = message.map(m -> m.getAuthor().map(User::getId).orElse(null));
    }

}
