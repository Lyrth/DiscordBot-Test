package net.ddns.lyr.modules.bot;

import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.config.GuildSetting;

import java.util.HashMap;

public class GuildModuleCore extends BotModule {


    public HashMap<Snowflake, GuildSetting> guildSettings;

    public GuildModuleCore() {
        guildSettings = Main.client.getGuildSettings();
    }


    public String getName() {
        return this.getClass().getName();
    }

}
