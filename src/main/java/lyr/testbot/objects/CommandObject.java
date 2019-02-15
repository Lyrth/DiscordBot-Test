package lyr.testbot.objects;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.*;
import reactor.core.publisher.Mono;

public class CommandObject extends MessageObject {

    public Mono<Message> message;

    public Mono<String> args;

    public Mono<MessageChannel> channel;
    public Mono<Guild> guild;

    public CommandObject(MessageCreateEvent event){
        M = Mono.just(event.getMessage()).cache();

        message = M;

        id = M.map(Message::getId);
        contents = M.map(m -> m.getContent().orElse(""));
        args = M.map(m ->m.getContent()
            .map(s -> s.replaceFirst("(^<@!?\\d+>\\s+\\S+|^\\S+)\\s*",""))
            .orElse("")
        );
        embeds = M.map(Message::getEmbeds);

        guildId = Mono.just(event.getGuildId()).map(e -> e.orElse(null));
        channelId = Mono.just(event.getMessage().getChannelId());

        guild = M.flatMap(Message::getGuild);
        author = member = Mono.just(event.getMember()).map(e -> e.orElse(null));
        user = M.map(Message::getAuthor);
        userId = M.map(m -> m.getAuthor().map(User::getId).orElse(null));
    }

    public Mono<Guild> getGuild() {
        return guild = M.flatMap(Message::getGuild);
    }

    public Mono<MessageChannel> getChannel() {
        return channel = M.flatMap(Message::getChannel);
    }
}
