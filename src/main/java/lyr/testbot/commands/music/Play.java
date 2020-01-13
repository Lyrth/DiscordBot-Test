package lyr.testbot.commands.music;

import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    aliases = {"play"},
    type = CommandType.ADMIN,
    desc = "Plays.",
    usage = "play [link]",
    minArgs = 1
)
public class Play extends Command {  // TODO: ARRANGE THESE!!!

    public Mono<Reply> execute(CommandObject command){
        return command.args.flatMap(e->execute(e));
    }

    private Mono<Reply> execute(CommandArgs args){
        return Mono.just(args)
            .filter(CommandArgs::isNotEmpty)
            .doOnNext(a ->
                getClient().playerManager.loadItem(args.get(0),
                    new ClientObject.MyAudioLoadResultHandler(getClient().player)))
            .map(a -> Reply.format("Playing `%s`", a.get(0)))
            .onErrorReturn(Reply.with("ewwow!!!"))
            .defaultIfEmpty(Reply.with("Usage: " + getFormattedUsage()));

    }
}
