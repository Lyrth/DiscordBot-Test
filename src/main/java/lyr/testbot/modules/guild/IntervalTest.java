package lyr.testbot.modules.guild;

import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.main.Main;
import lyr.testbot.event.TenSecondEvent;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.config.GuildSetting;

import java.time.Duration;


public class IntervalTest extends GuildModule {

    public void on(TenSecondEvent event){
        Main.client.getDiscordClient()
            .getChannelById(Snowflake.of(539743710347395072L))
            .cast(TextChannel.class)
            .flatMap(ch -> ch.createMessage("Sustain " + event.getTick()))
            .flatMap(m -> m.delete().delaySubscription(Duration.ofSeconds(3)))
            .subscribe();
    }

    public IntervalTest(){}
    public IntervalTest(GuildSetting guildSettings){ super(guildSettings); }
    public IntervalTest newInstance(GuildSetting guildSettings){
        return new IntervalTest(guildSettings);
    }
}
