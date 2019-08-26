package lyr.testbot.commands.admin;

import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Mono;

@CommandInfo(
    type = CommandType.ADMIN,
    desc = "Show some usage."
)
public class Usage extends Command {

    public Mono<Reply> execute(CommandObject command){
        return Mono.just(
            Reply.format(
                "`CPU (System ):` %.2f\n" +
                "`CPU (Process):` %.2f\n" +
                "`Available Mem:` %d\n" +
                "`Free Totl Mem:` Physical - %d, Swap - %d\n" +
                "`  Runtime Mem:` %d/%d, max %d",
                lyr.testbot.util.Usage.getSystemCpuUsage()*100,
                lyr.testbot.util.Usage.getProcessCpuUsage()*100,
                lyr.testbot.util.Usage.getProcessAvailableMemory(),
                lyr.testbot.util.Usage.getTotalMemory(), lyr.testbot.util.Usage.getTotalSwapSpace(),
                lyr.testbot.util.Usage.getRuntimeTotalMemory()-lyr.testbot.util.Usage.getRuntimeFreeMemory(),
                lyr.testbot.util.Usage.getRuntimeTotalMemory(),lyr.testbot.util.Usage.getRuntimeMaxMemory()
            )
        );
    }
}
