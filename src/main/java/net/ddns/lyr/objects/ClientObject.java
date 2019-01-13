package net.ddns.lyr.objects;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.handlers.EventHandler;
import net.ddns.lyr.modules.GuildModules;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.BotConfig;
import net.ddns.lyr.modules.BotModules;
import net.ddns.lyr.utils.config.GuildConfig;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class ClientObject {
    private DiscordClient client;

    public BotConfig config;
    public BotModules botModules;
    public Map<String, GuildModule> availableGuildModules;
    public EventHandler eventHandler;
    private EventDispatcher eventDispatcher;

    public Snowflake selfId;

    private Mono<User> botUser;
    private Mono<ApplicationInfo> applicationInfo;

    public Flux<Guild> guilds;

    public HashMap<Snowflake,Snowflake> channelToGuildMapping = new HashMap<>();

    private HashMap<Snowflake, GuildSetting> guildSettings;


    public ClientObject(DiscordClient client, BotConfig config){
        this.client = client;
        this.config = config;
        this.eventDispatcher = client.getEventDispatcher();
        availableGuildModules = new GuildModules().get();
    }

    public void init(){
        Log.log("> Doing client init things.");
        this.eventHandler = new EventHandler(eventDispatcher);
        guilds = client.getGuilds();
        botUser = client.getSelf();
        botUser.doOnNext(user -> selfId = user.getId()).block();
        HashMap<Snowflake,GuildSetting> configs = GuildConfig.readAllConfig();
        guildSettings = (HashMap<Snowflake, GuildSetting>)
            guilds
                .map(Guild::getId)
                .collectMap(
                    guildId -> /* Key */ guildId,
                    guildId -> /*Value*/ configs.getOrDefault(guildId, new GuildSetting(guildId))
                ).block();
        botModules = new BotModules();
        guilds.flatMap(Guild::getChannels)
            .collectMap(GuildChannel::getId, GuildChannel::getGuildId,() -> channelToGuildMapping)
            .block();
        //applicationInfo = client.getApplicationInfo().block();
        Log.log("> Done client init.");
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

    public Mono<User> getBotUser() {
        return botUser;
    }

    public Flux<Guild> getGuilds() {
        return guilds;
    }

    public HashMap<Snowflake, GuildSetting> getGuildSettings() {
        return guildSettings;
    }
}
