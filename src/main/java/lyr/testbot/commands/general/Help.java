package lyr.testbot.commands.general;

import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.Map;

public class Help extends Command {

    public Mono<Reply> execute(CommandObject command){
        return command.args.map(this::execute);
    }

    private Reply execute(CommandArgs args){
        if (args.isEmpty()){
            return Reply.with("Usage: " + getFormattedUsage());
        }
        Map<String, Command> commands = getClient().commands.getCommands();
        if (commands.containsKey(args.get(0).toLowerCase())){
            Command command = commands.get(args.get(0).toLowerCase());
            return Reply.empty()
                .setEmbedColor(Color.LIGHT_GRAY)
                .setEmbedTitle(command.getClass().getSimpleName())
                .setEmbedDescription(command.getDesc())
                .addEmbedField("Usage", getPrefix() + command.getFormattedUsage(), false)
                .addEmbedField("Type", command.getType().name(), false);
        } else {
            return Reply.with("Command **" + args.get(0) + "** does not exist.");
        }
    }

    public String getName(){
        return "help";
    }
    public CommandType getType(){
        return CommandType.GENERAL;
    }
    public String getDesc(){
        return "Show some help about a command.";
    }
    public String getUsage(){
        return "help (command name)";
    }
    public int getNumArgs(){
        return 1;
    }

}
