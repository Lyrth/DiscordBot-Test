package net.ddns.lyr.commands.admin;


import net.ddns.lyr.enums.CommandType;
import net.ddns.lyr.objects.CommandObject;
import net.ddns.lyr.templates.Command;
import reactor.core.publisher.Mono;

public class Test extends Command {
    public Mono<String> execute(CommandObject c){
        return Mono.just(c).flatMap(this::run);
    }

    /*
    public Mono<String> execute(CommandObject command){
        return command.member.map(member ->
            String.format("ID: %s\nNickname Direct Access: %s",
                member.getId(),
                member.getDisplayName())
        );
    }
     */

    private Mono<String> run(CommandObject command){
        return command.member.map(member ->
            String.format("ID: %s\nNickname Direct Access: %s",
                member.getId(),
                member.getDisplayName())
        );
    }

    public String getName(){
        return "test";
    }
    public CommandType getType(){
        return CommandType.GENERAL;
    }
    public String getDesc(){
        return "Show some test.";
    }
    public String getUsage(){
        return "test";
    }
    public int getNumArgs(){
        return 0;
    }
}
