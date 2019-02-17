package lyr.testbot.modules.guild;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.GuildSetting;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class SettingTest extends GuildModule {

    public Mono<Void> on(MessageCreateEvent event){
        if (!event.getMessage().getContent().isPresent()) return Mono.empty();
        String msg = event.getMessage().getContent().get();

        String[] split = msg.split(" ");
        if (split.length < 1 ||
            !split[0].toLowerCase().equals(";settingtest")) return Mono.empty();
        if (split.length == 1){
            return event.getMessage().getChannel()
                .flatMap(ch ->
                    ch.createMessage(
                        Optional.ofNullable(guildSettings.getModuleSetting(this.getName(), "value"))
                            .filter(s -> !s.isEmpty())
                            .orElse("Empty.")
                    )
                ).then(Mono.fromRunnable(()-> Log.logDebug("A!")));
        } else {
            guildSettings.setModuleSetting(this.getName(), "value", split[1]);
            Log.logDebug("BBB!");
            return Mono.empty();
        }
    }

    public SettingTest(){}
    public SettingTest(GuildSetting guildSettings){ super(guildSettings); }
    public SettingTest newInstance(GuildSetting guildSettings){
        return new SettingTest(guildSettings);
    }
}
