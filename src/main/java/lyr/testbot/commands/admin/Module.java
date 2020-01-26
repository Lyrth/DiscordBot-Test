package lyr.testbot.commands.admin;

import discord4j.core.object.entity.Guild;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    aliases = {"modules", "mod"},
    type = CommandType.ADMIN,
    desc = "Enable or disable a module.",
    usage = "module [enable/disable] (moduleName)",
    minArgs = 1
)
public class Module extends Command {

    public Mono<Reply> execute(CommandObject command){
        return Mono.zip(command.guild, command.args, this::execute);
    }

    private Reply execute(Guild guild, CommandArgs args){
        if ( args.isEmpty() ||
            (args.getCount() == 1 && args.matchesAt("(enable|disable)",0))){
            return Reply.with("Usage: " + getFormattedUsage());
        }
        String name;
        if (!(name = getClient().availableGuildModules.getProperName(args.get(0))).isEmpty()) {    // TODO: fix this
            return getGuildSettingsFor(guild.getId())
                .toggleModule(name) ?
                    Reply.format("Module **%s** enabled.", name) :
                    Reply.format("Module **%s** disabled.", name);
        } else if (args.getCount() > 1 && !(name = getClient().availableGuildModules.getProperName(args.get(1))).isEmpty()) {
            if (args.equalsAt("enable",0)) {
                return getGuildSettingsFor(guild.getId())
                    .enableModule(name) ?
                        Reply.format("Module **%s** enabled.", name) :
                        Reply.format("Module **%s** was already enabled.", name);
            } else if (args.equalsAt("disable",0)) {
                return getGuildSettingsFor(guild.getId())
                    .disableModule(name) ?
                        Reply.format("Module **%s** disabled.", name) :
                        Reply.format("Module **%s** was already disabled.", name);
            }
        }
        return Reply.format("Module **%s** not found.",
            args.getRaw().replaceFirst("^((enable|disable)\\s+)?(\\S+).*", "$3"));
    }
}
