package lyr.testbot.commands.admin;

import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    type = CommandType.ADMIN,
    desc = "Collect garbage."
)
public class Gc extends Command {

    public Mono<Reply> execute(CommandObject command){
        return Mono.fromRunnable(System::gc)
            .thenReturn(Reply.format("Garbage collected."))
            .onErrorReturn(Reply.format("Ewwow!!!"));
    }
}
