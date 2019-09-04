package lyr.testbot.modules.guild;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

@ModuleInfo(
    desc = "Test for settings."
)
public class SettingTest extends GuildModule {

    public Mono<Void> on(MessageCreateEvent event){
        if (!event.getMessage().getContent().isPresent()) return Mono.empty();  // TODO: dedicate command
        String msg = event.getMessage().getContent().get();

        String[] split = msg.split(" ");
        if (split.length < 1 ||
            !split[0].toLowerCase().equals(";settingtest")) return Mono.empty();
        if (split.length == 1){
            return event.getMessage().getChannel()
                .flatMap(ch -> ch.createMessage(getSettingOrDefault( "value", "Empty.")))
                .then(Mono.fromRunnable(()-> Log.logDebug("A!")));
        } else {
            setSetting("value", split[1]);
            Log.logDebug("BBB!");
            return Mono.empty();
        }
    }
}
