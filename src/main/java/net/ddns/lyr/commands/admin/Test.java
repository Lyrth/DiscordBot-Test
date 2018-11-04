package net.ddns.lyr.commands.admin;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import net.ddns.lyr.enums.CommandType;
import net.ddns.lyr.objects.CommandObject;
import net.ddns.lyr.templates.Command;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Test extends Command {

    public Mono<String> execute(CommandObject command){

        return Mono.zip(
            command.user.map(User::getMention),
            command.contents,
            command.args
        ).map(T ->
            String.format("%s, you said `%s`,\n with args `%s`",T.getT1(),T.getT2(),T.getT3())
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

    public String getName(){
        return "test";
    }
    public CommandType getType(){
        return CommandType.ADMIN;
    }
    public String getDesc(){
        return "Show some test.";
    }
    public String getUsage(){
        return "test";
    }
    public int getNumArgs(){
        return 0;
    }
}
