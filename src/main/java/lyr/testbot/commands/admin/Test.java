package lyr.testbot.commands.admin;

import discord4j.core.object.entity.User;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    name = "test",
    type = CommandType.ADMIN,
    desc = "Show some test.",
    usage = "test"
)
public class Test extends Command {

    public Mono<Reply> execute(CommandObject command){
        return Mono.zip(
            command.user.map(ou -> ou.map(User::getMention).orElse("")),
            command.contents,
            command.args
        ).map(T ->
            Reply.format("%s, you said `%s`,\n with args `%s`",T.getT1(),T.getT2(),T.getT3().getRaw())
        );

        /*
        AtomicInteger iA = new AtomicInteger(0);
        return Flux.just(1,2,3,4,5)
            .delayElements(Duration.ofMillis(100))
            .map(i -> {
                iA.set(i);
                return i;
            })
            .flatMap(i -> command.getChannel())
            .flatMap(c ->
                c.createMessage(""+iA.get()+" TEST!")
            )
            .collectList()
            .then(Mono.empty());
            */
    }
}
