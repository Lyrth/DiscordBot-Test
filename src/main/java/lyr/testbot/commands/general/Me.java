package lyr.testbot.commands.general;

import lyr.testbot.enums.CommandType;
import lyr.testbot.templates.Command;
import lyr.testbot.objects.CommandObject;
import reactor.core.publisher.Mono;

public class Me extends Command {

    public Mono<String> execute(CommandObject command){
        return command.member.map(member ->
            String.format("ID: %s\nUsername: %s\nNickname: %s\nDiscrim: %s\n"+
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

    public String getName(){
        return "me";
    }
    public CommandType getType(){
        return CommandType.GENERAL;
    }
    public String getDesc(){
        return "Show some info.";
    }
    public String getUsage(){
        return "me";
    }
    public int getNumArgs(){
        return 0;
    }

}
