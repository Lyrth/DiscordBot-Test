package lyr.testbot.objects;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.*;
import reactor.core.publisher.Mono;

public class CommandObject extends MessageObject {

    public Mono<Message> message;

    public Mono<CommandArgs> args;

    public Mono<Guild> guild;

    public CommandObject(MessageCreateEvent event){
        M = Mono.just(event.getMessage()).cache();

        message = M;

        id = M.map(Message::getId);
        contents = M.flatMap(m -> Mono.justOrEmpty(m.getContent()));
        args = M.map(m ->
            m.getContent()
                .map(s -> s.replaceFirst("(^<@!?\\d+>\\s+\\S+|^\\S+)\\s*",""))
                .orElse("")
            ).map(CommandArgs::new);
        embeds = M.map(Message::getEmbeds);

        guildId = Mono.justOrEmpty(event.getGuildId());
        channelId = Mono.just(event.getMessage().getChannelId());

        guild = M.flatMap(Message::getGuild);
        author = member = Mono.justOrEmpty(event.getMember());
        user = M.flatMap(m -> Mono.justOrEmpty(m.getAuthor()));
        userId = M.flatMap(m -> Mono.justOrEmpty(m.getAuthor().map(User::getId)));
    }

    public Mono<Guild> getGuild() {
        return guild = M.flatMap(Message::getGuild);
    }

    public Mono<MessageChannel> getChannel() {
        return M.flatMap(Message::getChannel);
    }
}
