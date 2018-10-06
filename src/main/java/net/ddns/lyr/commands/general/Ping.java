package net.ddns.lyr.commands.general;

import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.enums.CommandType;
import net.ddns.lyr.objects.CommandObject;
import net.ddns.lyr.templates.Command;
import reactor.core.publisher.Mono;

public class Ping extends Command {

    public Mono<String> execute(CommandObject c){
        return Mono.just(c).flatMap(this::run);
    }

    private Mono<String> run(CommandObject command){
        return command.getChannel().zipWith(command.message,(ch,ms)->
            ch.createMessage("Pinging...")
                .subscribe(m ->
                    m.edit(new MessageEditSpec().setContent(
                        "Pong! Took " + (m.getTimestamp().toEpochMilli()-ms.getTimestamp().toEpochMilli()) + "ms."
                    )).subscribe()
                )
        ).then(Mono.empty());
    }

    public String getName(){
        return "ping";
    }
    public CommandType getType(){
        return CommandType.GENERAL;
    }
    public String getDesc(){
        return "Pings.";
    }
    public String getUsage(){
        return "ping";
    }
    public int getNumArgs(){
        return 0;
    }

}
