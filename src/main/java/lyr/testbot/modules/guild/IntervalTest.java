package lyr.testbot.modules.guild;

import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.event.TenSecondEvent;
import lyr.testbot.templates.GuildModule;
import reactor.core.publisher.Mono;

import java.time.Duration;

@ModuleInfo(
    desc = "Sustains something."
)
public class IntervalTest extends GuildModule {

    public Mono<Void> on(TenSecondEvent event){
        if (event.getTick() % 3 != 0) return Mono.empty();  // do this every 30 seconds
        return getClient().getDiscordClient()
            .getChannelById(Snowflake.of(539743710347395072L))
            .cast(TextChannel.class)
            .flatMap(ch -> ch.createMessage("Sustain " + event.getTick()))
            .flatMap(m -> m.delete().delaySubscription(Duration.ofSeconds(3)));
    }
}
