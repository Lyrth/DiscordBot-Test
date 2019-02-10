package lyr.testbot.commands.general;

import discord4j.core.spec.EmbedCreateSpec;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.Reply;
import lyr.testbot.templates.Command;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.util.pagination.Paginator;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Ping extends Command {

    public Mono<Reply> execute(CommandObject command){
        AtomicLong start = new AtomicLong(0L);
        List<EmbedCreateSpec> pages = new ArrayList<>();
        pages.add(new EmbedCreateSpec().setDescription("aaa"));
        pages.add(new EmbedCreateSpec().setDescription("bbb"));
        pages.add(new EmbedCreateSpec().setDescription("ccc"));
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
            .flatMap(m -> Paginator.paginate(m.getChannel(),pages))
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
