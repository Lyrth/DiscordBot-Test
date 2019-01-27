package net.ddns.lyr.modules.guild;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;

import java.util.Optional;

public class SettingTest extends GuildModule {

    public void on(MessageCreateEvent event){
        if (!event.getMessage().getContent().isPresent()) return;
        String msg = event.getMessage().getContent().get();

        String[] split = msg.split(" ");
        if (split.length < 1) return;
        if (!split[0].toLowerCase().equals(";settingtest")) return;
        if (split.length == 1){
            event.getMessage().getChannel()
                .flatMap(ch ->
                    ch.createMessage(
                        Optional.of(guildSettings.getModuleSetting(this.getName(), "value"))
                            .filter(s -> !s.isEmpty())
                            .orElse("Empty.")
                    )
                )
                .subscribe();
            Log.logDebug("A!");
        } else {
            guildSettings.setModuleSetting(this.getName(), "value", split[1]);
            Log.logDebug("BBB!");
        }
    }

    public SettingTest(){}
    public SettingTest(GuildSetting guildSettings){ super(guildSettings); }
    public SettingTest newInstance(GuildSetting guildSettings){
        return new SettingTest(guildSettings);
    }
}
