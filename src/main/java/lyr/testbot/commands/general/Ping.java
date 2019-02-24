package lyr.testbot.commands.general;

import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Embed;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Ping extends Command {

    public Mono<Reply> execute(CommandObject command){
        AtomicLong start = new AtomicLong(0L);
        List<Embed> pages = new ArrayList<>();
        pages.add(Embed.withDesc("aaa"));
        pages.add(Embed.withDesc("bbb"));
        pages.add(Embed.withDesc("ccc"));
        return command.getChannel()
            .zipWith(command.message,(ch,ms)-> {
                start.set(ms.getTimestamp().toEpochMilli());
                return ch.createMessage("Pinging...");
            })
            .flatMap(m -> m)
            .flatMap(m ->
                m.edit(s -> s.setContent(
                    "Pong! Took " + (m.getTimestamp().toEpochMilli()-start.get()) + "ms."
                ))
            )
            .thenReturn(Reply.empty());
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
