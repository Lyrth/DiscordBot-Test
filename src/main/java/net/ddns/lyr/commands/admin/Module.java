package net.ddns.lyr.commands.admin;

import discord4j.core.object.entity.Guild;
import net.ddns.lyr.enums.CommandType;
import net.ddns.lyr.objects.CommandObject;
import net.ddns.lyr.templates.Command;
import reactor.core.publisher.Mono;

public class Module extends Command {

    public Mono<String> execute(CommandObject command){
        return Mono.zip(command.guild,command.args)
            .map(tup -> execute(tup.getT1(),tup.getT2()));
    }

    private String execute(Guild guild, String arg){
        String[] args = arg.split(" ");
        if (args[0].isEmpty()){
            return "Usage: " + getUsage();
        }
        if (args[0].equals("enable") && args.length > 1) {
            if (getClient().getGuildSettings().get(guild.getId())
                .enableModule(args[1])){
                return String.format("Module **%s** enabled.", args[0]);
            } else {
                return String.format("Module **%s** was already enabled.", args[0]);
            }
        } else if (args[0].equals("disable") && args.length > 1) {
            if (getClient().getGuildSettings().get(guild.getId())
                .disableModule(args[1])){
                return String.format("Module **%s** disabled.", args[0]);
            } else {
                return String.format("Module **%s** was already disabled.", args[0]);
            }
        } else if (!getClient().availableGuildModules.containsKey(args[0])) {  // TODO: case insensitive search
            return String.format("Module **%s** not found.", args[0]);
        } else {
            return (getClient().getGuildSettings().get(guild.getId())
                .toggleModule(args[0])) ?
                String.format("Module **%s** enabled.", args[0]) :
                String.format("Module **%s** disabled.", args[0]);
        }
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
