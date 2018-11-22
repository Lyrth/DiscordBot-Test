package net.ddns.lyr.objects;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.handlers.EventHandler;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.BotConfig;
import net.ddns.lyr.modules.BotModules;
import net.ddns.lyr.utils.config.GuildConfig;
import net.ddns.lyr.utils.config.GuildSetting;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientObject {
    private volatile DiscordClient client;
    private volatile BotConfig config;
    private EventDispatcher eventDispatcher;
    private volatile EventHandler eventHandler;
    private BotModules modules;

    private User botUser;
    private ApplicationInfo applicationInfo;

    public HashMap<Snowflake, GuildSetting> guildSettings;


    public ClientObject(DiscordClient client, BotConfig config){
        this.client = client;
        this.config = config;
        this.eventDispatcher = client.getEventDispatcher();
    }

    public void init(){
        this.eventHandler = new EventHandler(eventDispatcher);
        modules = new BotModules();
        Log.logDebug("thonk");
        HashMap<Snowflake,GuildSetting> configs = GuildConfig.readAllConfig();
        guildSettings = (HashMap<Snowflake, GuildSetting>)
            client.getGuilds()
            .map(Guild::getId)
            .collectMap(
                guildId -> guildId,
                guildId -> configs.getOrDefault(guildId, new GuildSetting())
            ).block();
        //botUser = client.getSelf().block();
        //applicationInfo = client.getApplicationInfo().block();
        Log.logDebug("thonkang");
    }

    public DiscordClient getDiscordClient() {
        return client;
    }

    public BotConfig getBotConfig() {
        return config;
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public User getBotUser() {
        return botUser;
    }
}
