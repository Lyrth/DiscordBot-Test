package net.ddns.lyr.modules.guild;

import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class SettingTest extends GuildModule {

    @ModuleEvent
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
                        Optional.of(guildSettings.getModuleSetting(this.getName(),"value"))
                            .orElse("Empty.")
                    )
                )
                .subscribe();
            Log.logDebug("A!");
        } else {
            guildSettings.setModuleSetting(this.getName(),"value",split[1]);
            Log.logDebug("BBB!");
        }

        /*
        event.getUser().flatMap(User::getPrivateChannel)
            .flatMap( ch ->
                ch.createMessage("I saw you typing! On <#" + event.getChannelId().asString() + ">").flatMap( m ->
                    m.edit(new MessageEditSpec().setContent("[REDACTED]"))
                        .delaySubscription(Duration.ofSeconds(15))
                )
            ).doOnError(err ->
                Log.logfError("REEEE CANNOT SEND MESSAGE TO %s ;-;", event.getUserId().asString())
            ).subscribe();*/
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
    public SettingTest(){}
    public SettingTest(Mono<Guild> guild, GuildSetting guildSettings){ super(guild,guildSettings); }
    public SettingTest newInstance(Mono<Guild> guild, GuildSetting guildSettings){
        return new SettingTest(guild,guildSettings);
    }
}
