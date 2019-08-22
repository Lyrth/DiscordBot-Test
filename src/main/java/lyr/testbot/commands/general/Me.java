package lyr.testbot.commands.general;

import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    name = "me",
    aliases = {"profile"},
    type = CommandType.GENERAL,
    desc = "Show some info.",
    usage = "me"
)
public class Me extends Command {

    public Mono<Reply> execute(CommandObject command){
        return command.member.map(member ->
            Reply.format("ID: %s\nUsername: %s\nNickname: %s\nDiscrim: %s\n"+
                    "Mention: %s\nGuild ID: %s\nJoin Time: %s",
                member.getId(),
                member.getUsername(),
                member.getDisplayName(),
                member.getDiscriminator(),
                member.getMention(),
                member.getGuildId(),
                member.getJoinTime())
        );
    }
}
