package lyr.testbot.objects;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import com.sedmelluq.discord.lavaplayer.track.playback.*;
import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import discord4j.voice.AudioProvider;
import lyr.testbot.commands.Commands;
import lyr.testbot.handlers.EventHandler;
import lyr.testbot.modules.BotModules;
import lyr.testbot.modules.GuildModules;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.HashMap;

import discord4j.core.object.entity.ApplicationInfo;
//import discord4j.core.object.entity.GuildChannel;

public class ClientObject {
    private DiscordClient client;

    public BotConfig config;
    public Commands commands;
    public BotModules botModules;
    public GuildModules availableGuildModules;
    public EventHandler eventHandler;
    private EventDispatcher eventDispatcher;

    public Mono<Snowflake> selfId;
    public Mono<Snowflake> ownerId;

    private Mono<User> botUser;
    private Mono<User> owner;
    private Mono<ApplicationInfo> applicationInfo;

    public Flux<Guild> guilds;

    public AudioPlayerManager playerManager;  // TODO: unclutter audio player things
    public AudioPlayer player;
    public AudioProvider provider;

    //public HashMap<Snowflake,Snowflake> channelToGuildMapping = new HashMap<>();

    private HashMap<Snowflake, GuildSetting> guildSettings;

    public ClientObject(DiscordClient client, BotConfig config){
        this.client = client;
        this.config = config;
        this.eventDispatcher = client.getEventDispatcher();
        commands = new Commands();
    }

    public void init(){
        Log.info("> Doing client init things.");

        botUser = client.getSelf();
        selfId = botUser.map(User::getId).cache();

        applicationInfo = client.getApplicationInfo();
        owner = applicationInfo.flatMap(ApplicationInfo::getOwner);
        ownerId = applicationInfo.map(ApplicationInfo::getOwnerId).cache();

        this.eventHandler = new EventHandler(eventDispatcher);
        guilds = client.getGuilds();

        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
        provider = new LavaplayerAudioProvider(player);

        availableGuildModules = new GuildModules();
        HashMap<Snowflake,GuildSetting> configs = GuildConfig.readAllConfig();   // TODO bloccing io
        botModules = new BotModules();

        guildSettings = (HashMap<Snowflake, GuildSetting>)  // TODO fix this shiz
            guilds
                .map(Guild::getId)
                .collectMap(
                    guildId -> /* Key */ guildId,
                    guildId -> /*Value*/ configs.getOrDefault(guildId, new GuildSetting(guildId))
                ).block();
        //guilds.flatMap(Guild::getChannels)
        //    .collectMap(GuildChannel::getId, GuildChannel::getGuildId,() -> channelToGuildMapping)
        //   .block();
        Log.info("> Done client init.");
        // TODO: Shutdown Hook
    }

    public void shutdown(){
        System.exit(0);
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

    public Mono<User> getOwner() {
        return owner;
    }

    public Mono<Snowflake> getId() {
        return selfId;
    }

    public Mono<Snowflake> getOwnerId() {
        return ownerId;
    }

    public Mono<ApplicationInfo> getApplicationInfo() {
        return applicationInfo;
    }

    public Flux<Guild> getGuilds() {
        return guilds;
    }

    public HashMap<Snowflake, GuildSetting> getGuildSettings() {
        return guildSettings;
    }



    public static class LavaplayerAudioProvider extends AudioProvider {

        private final AudioPlayer player;
        private final MutableAudioFrame frame = new MutableAudioFrame();

        public LavaplayerAudioProvider(AudioPlayer player) {
            super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
            this.player = player;
            this.frame.setBuffer(getBuffer());
        }

        @Override
        public boolean provide() {
            boolean didProvide = player.provide(frame);
            if (didProvide) {
                getBuffer().flip();
            }
            return didProvide;
        }
    }

    public static class MyAudioLoadResultHandler implements AudioLoadResultHandler {

        private final AudioPlayer player;

        public MyAudioLoadResultHandler(AudioPlayer player) {
            this.player = player;
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            player.playTrack(track);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {

        }

        @Override
        public void noMatches() {

        }

        @Override
        public void loadFailed(FriendlyException exception) {

        }
    }
}
