package lyr.testbot.commands.general;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Embed;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import lyr.testbot.util.pagination.Paginator;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@CommandInfo(
    aliases = {"commandlist", "commandslist", "listcommands"},
    type = CommandType.GENERAL,
    desc = "Show all commands."
)
public class Commands extends Command {

    public Mono<Reply> execute(CommandObject command){  // TODO: refactor, mono
        return command.args.flatMap(args -> execute(args, command.getChannel())).thenReturn(Reply.empty());
    }

    public Mono<Message> execute(CommandArgs args, Mono<MessageChannel> channel){
        ConcurrentHashMap<CommandType, List<String>> sortedCommands = new ConcurrentHashMap<>();
        Stream<Command> commands = getClient().commands.getCommands().values().parallelStream();
        commands.forEach(command -> {
            List<String> list = sortedCommands.get(command.getType());
            if (list == null){
                list = new ArrayList<>();
                list.add(command.getClass().getSimpleName());
                sortedCommands.put(command.getType(),list);
            } else {
                list.add(command.getClass().getSimpleName());
            }
        });
        commands.close();
        if (args.isEmpty()){
            List<Embed> pages = new ArrayList<>();
            sortedCommands.forEach((type, commandList) -> {
                Collections.sort(commandList);
                pages.add(
                    Embed.withTitle("Type **" + type.name() + "**:")
                    .setDescription("```\n" + String.join("\n",commandList) + "```")
                );
            });
            return Paginator.paginate(channel,pages,"List of all available commands:");
        } else {
            try {
                CommandType type = CommandType.valueOf(args.get(0).toUpperCase());
                if (!sortedCommands.containsKey(type)) throw new IllegalArgumentException();
                Reply reply = Reply.empty()
                    .setEmbedTitle("Type **" + type.name() + "** commands:")
                    .setEmbedDescription("```\n" + String.join("\n",sortedCommands.get(type)) + "```");
                return channel.flatMap(c -> c.createMessage(reply));
            } catch (IllegalArgumentException e){
                return channel.flatMap(c -> c.createMessage("No commands found for type **" + args.get(0) + "**."));
            }
        }
    }

    public String getName(){
        return "commands";
    }
    public CommandType getType(){
        return CommandType.GENERAL;
    }
    public String getDesc(){
        return "Show all commands.";
    }
    public String getUsage(){
        return "commands";
    }
    public int getMinArgs(){
        return 0;
    }

}
