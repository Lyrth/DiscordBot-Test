package net.ddns.lyr.objects;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.handlers.EventHandler;
import net.ddns.lyr.modules.GuildModules;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.BotConfig;
import net.ddns.lyr.modules.BotModules;
import net.ddns.lyr.utils.config.GuildConfig;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Flux;

import java.util.HashMap;

public class ClientObject {
    private DiscordClient client;

    public BotConfig config;
    public BotModules botModules;
    public GuildModules availableGuildModules;
    private EventDispatcher eventDispatcher;
    private EventHandler eventHandler;

    private User botUser;
    private ApplicationInfo applicationInfo;

    public Flux<Guild> guilds;

    private HashMap<Snowflake, GuildSetting> guildSettings;


    public ClientObject(DiscordClient client, BotConfig config){
        this.client = client;
        this.config = config;
        this.eventDispatcher = client.getEventDispatcher();
        availableGuildModules = new GuildModules();
    }

    public void init(){
        this.eventHandler = new EventHandler(eventDispatcher);
        Log.logDebug("thonk");
        guilds = client.getGuilds();
        HashMap<Snowflake,GuildSetting> configs = GuildConfig.readAllConfig();
        guildSettings = (HashMap<Snowflake, GuildSetting>)
            guilds.map(Guild::getId)
            .collectMap(
                guildId -> /* Key */ guildId,
                guildId -> /*Value*/ configs.getOrDefault(guildId, new GuildSetting(guildId))
            ).block();
        botModules = new BotModules();
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

    public Flux<Guild> getGuilds() {
        return guilds;
    }

    public HashMap<Snowflake, GuildSetting> getGuildSettings() {
        return guildSettings;
    }
}
