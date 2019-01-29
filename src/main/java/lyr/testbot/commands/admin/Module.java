package lyr.testbot.commands.admin;

import discord4j.core.object.entity.Guild;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

public class Module extends Command {

    public Mono<String> execute(CommandObject command){
        return Mono.zip(command.guild,command.args)
            .map(tup -> execute(tup.getT1(),tup.getT2()));
    }

    private String execute(Guild guild, String arg){
        String[] args = arg.split(" ");
        if ( args[0].isEmpty() ||
            (args.length == 1 && args[0].matches("(enable|disable)"))){
            return "Usage: " + getUsage();
        }
        String name;
        if (!(name = getClient().availableGuildModules.getProperName(args[0])).isEmpty()) {
            return (getClient().getGuildSettings().get(guild.getId())
                .toggleModule(name) ?
                String.format("Module **%s** enabled.", name) :
                String.format("Module **%s** disabled.", name));
        } else if (args.length > 1 && !(name = getClient().availableGuildModules.getProperName(args[1])).isEmpty()) {
            if (args[0].equals("enable")) {
                if (getClient().getGuildSettings().get(guild.getId())
                    .enableModule(name)) {
                    return String.format("Module **%s** enabled.", name);
                } else {
                    return String.format("Module **%s** was already enabled.", name);
                }
            } else if (args[0].equals("disable")) {
                if (getClient().getGuildSettings().get(guild.getId())
                    .disableModule(name)) {
                    return String.format("Module **%s** disabled.", name);
                } else {
                    return String.format("Module **%s** was already disabled.", name);
                }
            }
        }
        return String.format("Module **%s** not found.",
            arg.replaceFirst("^((enable|disable)\\s+)?(\\S+).*", "$3"));
    }

    public String getName(){
        return "module";
    }
    public CommandType getType(){
        return CommandType.ADMIN;
    }
    public String getDesc(){
        return "Enable or disable a module.";
    }
    public String getUsage(){
        return "module [enable/disable] (moduleName)";
    }
    public int getNumArgs(){
        return 0;
    }
}
