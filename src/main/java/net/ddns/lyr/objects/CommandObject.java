package net.ddns.lyr.objects;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.*;
import reactor.core.publisher.Mono;

public class CommandObject extends MessageObject {

    public Mono<Message> message;

    public Mono<MessageChannel> channel;
    public Mono<Guild> guild;

    public CommandObject(MessageCreateEvent event){
        M = Mono.just(event.getMessage()).cache();

        message = M;

        id = M.map(Message::getId);
        contents = M.map(m -> m.getContent().orElse(null));
        //embed = M.map(m -> m.getEmbeds().get(0));

        guildId = Mono.just(event.getGuildId()).map(e -> e.orElse(null));
        channelId = Mono.just(event.getMessage().getChannelId());

        author = member = Mono.just(event.getMember()).map(e -> e.orElse(null));
        user = member.map(m->m);
        userId = M.map(e->e.getAuthorId().orElse(null));
    }

    public Mono<Guild> getGuild() {
        return guild = M.flatMap(Message::getGuild);
    }

    public Mono<MessageChannel> getChannel() {
        return channel = M.flatMap(Message::getChannel);
    }
}
