package lyr.testbot.commands.module.settingtest;

import discord4j.core.object.util.Snowflake;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandArgs;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    type = CommandType.ADMIN,
    desc = "Do some test."
)
public class SettingTest extends Command {

    public Mono<Reply> execute(CommandObject command){
        return Mono.zip(command.args, command.guildId, this::execute);  // would not run when no guildId
    }

    private Reply execute(CommandArgs args, Snowflake guildId){
        if (args.isEmpty()){
            return Reply.with(getSettingOrDefault(guildId,"SettingTest", "value", "Empty."));  // TODO: MONO TOO
        } else {
            setSetting(guildId, "SettingTest", "value", args.get(0));  // TODO, new as Mono now
            return Reply.with("Set!");
        }
    }
}
