package lyr.testbot.modules.guild;

import lyr.testbot.util.Log;
import lyr.testbot.event.TenSecondEvent;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.config.GuildSetting;


public class IntervalTest extends GuildModule {

    public void on(TenSecondEvent event){
        Log.log("> Ten second event.");
    }

    public IntervalTest(){}
    public IntervalTest(GuildSetting guildSettings){ super(guildSettings); }
    public IntervalTest newInstance(GuildSetting guildSettings){
        return new IntervalTest(guildSettings);
    }
}
