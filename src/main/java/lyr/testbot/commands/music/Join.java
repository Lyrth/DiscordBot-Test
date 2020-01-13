package lyr.testbot.commands.music;

import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    aliases = {"join"},
    type = CommandType.ADMIN,
    desc = "join a channel.",
    usage = "join",
    minArgs = 0
)
public class Join extends Command {  // TODO: ARRANGE THESE!!!

    public Mono<Reply> execute(CommandObject command){
        return command.member.flatMap(e->execute(e));
    }

    private Mono<Reply> execute(Member m){
        return m.getVoiceState()
            .flatMap(VoiceState::getChannel)
            .flatMap(c -> c.join(s -> s.setProvider(getClient().provider)))
            .map(a -> Reply.format("joined"))
            .onErrorReturn(Reply.with("ewwow!!!"))
            .defaultIfEmpty(Reply.with("oh no"));

    }
}
