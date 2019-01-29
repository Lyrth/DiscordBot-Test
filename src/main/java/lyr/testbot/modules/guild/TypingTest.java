package lyr.testbot.modules.guild;

import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.GuildSetting;

import java.time.Duration;

public class TypingTest extends GuildModule {

    public void on(TypingStartEvent event){
        event.getUser().flatMap(User::getPrivateChannel)
            .flatMap( ch ->
                ch.createMessage("I saw you typing! On <#" + event.getChannelId().asString() + ">").flatMap( m ->
                    m.edit(new MessageEditSpec().setContent("[REDACTED]"))
                        .delaySubscription(Duration.ofSeconds(15))
                )
            ).doOnError(err ->
                Log.logfError("REEEE CANNOT SEND MESSAGE TO %s ;-;", event.getUserId().asString())
            ).subscribe();
    }

    public TypingTest(){}
    public TypingTest(GuildSetting guildSettings){ super(guildSettings); }
    public TypingTest newInstance(GuildSetting guildSettings){
        return new TypingTest(guildSettings);
    }
}
