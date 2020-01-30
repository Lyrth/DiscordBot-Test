package lyr.testbot.commands.admin;

import discord4j.core.object.entity.Guild;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import lyr.testbot.util.FuncUtil;
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
        return Mono.zip(command.guild, command.args, this::execute).flatMap(FuncUtil::it);
    }

    private Mono<Reply> execute(Guild guild, CommandArgs args){
        if ( args.isEmpty() ||
            (args.getCount() == 1 && args.matchesAt("(enable|disable)",0))){
            return Mono.just(Reply.with("Usage: " + getFormattedUsage()));
        }

        return Mono.just(getClient().availableGuildModules.getProperName(args.get(0)))
            .filter(name -> !name.isEmpty())
            .flatMap(name ->
                getGuildSettingsFor(guild.getId())
                    .toggleModule(name)
                    .filter(FuncUtil::it)
                    .map($ -> "enabled")
                    .defaultIfEmpty("disabled")
                    .map(state -> Reply.format("Module **%s** %s.", name, state))
            )
            .switchIfEmpty(
                Mono.just(getClient().availableGuildModules.getProperName(args.get(1)))
                    .filter(name -> args.getCount() > 1 && !name.isEmpty())
                    .flatMap(name -> {
                        if (args.equalsAt("enable",0)) {
                            return getGuildSettingsFor(guild.getId())
                                .enableModule(name)
                                .filter(FuncUtil::it)
                                .map($ -> "enabled")
                                .defaultIfEmpty("was already enabled")
                                .map(state -> Reply.format("Module **%s** %s.", name, state));
                        } else if (args.equalsAt("disable",0)) {
                            return getGuildSettingsFor(guild.getId())
                                .disableModule(name)
                                .filter(FuncUtil::it)
                                .map($ -> "disabled")
                                .defaultIfEmpty("was already disabled")
                                .map(state -> Reply.format("Module **%s** %s.", name, state));
                        } else return Mono.empty();
                    })
            )
            .defaultIfEmpty(Reply.format("Module **%s** not found.",
                args.getRaw().replaceFirst("^((enable|disable)\\s+)?(\\S+).*", "$3")));
    }
}
