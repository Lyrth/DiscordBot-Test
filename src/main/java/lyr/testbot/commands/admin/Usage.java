package lyr.testbot.commands.admin;

import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

public class Usage extends Command {

    public Mono<Reply> execute(CommandObject command){
        return Mono.just(
            Reply.format(
                "`CPU (System ):` %d\n" +
                "`CPU (Process):` %d\n" +
                "`Available Mem:` %d\n" +
                "`Free Totl Mem:` Physical - %d, Swap - %d\n" +
                "`  Runtime Mem:` %d/%d, max %d",
                lyr.testbot.util.Usage.getSystemCpuUsage(),
                lyr.testbot.util.Usage.getProcessCpuUsage(),
                lyr.testbot.util.Usage.getProcessAvailableMemory(),
                lyr.testbot.util.Usage.getTotalMemory(), lyr.testbot.util.Usage.getTotalSwapSpace(),
                lyr.testbot.util.Usage.getRuntimeTotalMemory()-lyr.testbot.util.Usage.getRuntimeFreeMemory(),
                lyr.testbot.util.Usage.getRuntimeTotalMemory(),lyr.testbot.util.Usage.getRuntimeMaxMemory()
            )
        );
    }

    public String getName(){
        return "usage";
    }
    public CommandType getType(){
        return CommandType.ADMIN;
    }
    public String getDesc(){
        return "Show some usage.";
    }
    public String getUsage(){
        return "usage";
    }
    public int getNumArgs(){
        return 0;
    }
}
