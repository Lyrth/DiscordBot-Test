package lyr.testbot.commands.music;

import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    aliases = {"stop"},
    type = CommandType.ADMIN,
    desc = "sotp.",
    usage = "stop",
    minArgs = 0
)
public class Stop extends Command {  // TODO: ARRANGE THESE!!!

    public Mono<Reply> execute(CommandObject command){
        return command.args.flatMap(e->execute(e));
    }

    private Mono<Reply> execute(CommandArgs args){
        return Mono.just(args)
            .doOnNext(a ->
                getClient().player.stopTrack())
            .map(a -> Reply.format("Stopped..", a.get(0)))
            .onErrorReturn(Reply.with("ewwow!!!"));
    }
}
