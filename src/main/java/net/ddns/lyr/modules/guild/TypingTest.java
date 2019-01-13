package net.ddns.lyr.modules.guild;

import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.AnnotationUtil;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class TypingTest extends GuildModule {

    @ModuleEvent
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

    public String getName() {
        return this.getClass().getSimpleName();
    }
    public TypingTest(){}
    public TypingTest(Mono<Guild> guild, GuildSetting guildSettings){ super(guild,guildSettings); }
    public TypingTest newInstance(Mono<Guild> guild, GuildSetting guildSettings){
        return new TypingTest(guild,guildSettings);
    }
}
