package net.ddns.lyr.modules.guild;

import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.event.TenSecondEvent;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;


public class IntervalTest extends GuildModule {

    @ModuleEvent
    public void on(TenSecondEvent event){
        Log.log("> Ten second event.");
    }

    public IntervalTest(){}
    public IntervalTest(GuildSetting guildSettings){ super(guildSettings); }
    public IntervalTest newInstance(GuildSetting guildSettings){
        return new IntervalTest(guildSettings);
    }
}
