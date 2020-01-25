package lyr.testbot.modules.guild;

import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.object.entity.User;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

import java.time.Duration;

@ModuleInfo(
    desc = "Test typing. DMs users so this bad lol."
)
public class TypingTest extends GuildModule {

    public Mono<Void> on(TypingStartEvent event){
        return event.getUser().flatMap(User::getPrivateChannel)
            .flatMap( ch ->
                ch.createMessage("I saw you typing! On <#" + event.getChannelId().asString() + ">").flatMap( m ->
                    m.edit(msg -> msg.setContent("[REDACTED]"))
                        .delaySubscription(Duration.ofSeconds(15))
                )
            ).doOnError(err ->
                Log.errorFormat("REEEE CANNOT SEND MESSAGE TO %s ;-;", event.getUserId().asString())
            ).then();
    }
}
