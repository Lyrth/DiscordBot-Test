package lyr.testbot.objects;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.handlers.EventHandler;
import lyr.testbot.modules.GuildModules;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.BotConfig;
import lyr.testbot.modules.BotModules;
import lyr.testbot.util.config.GuildConfig;
import lyr.testbot.util.config.GuildSetting;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class ClientObject {
    private DiscordClient client;

    public BotConfig config;
    public BotModules botModules;
    public GuildModules availableGuildModules;
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
        availableGuildModules = new GuildModules();
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
