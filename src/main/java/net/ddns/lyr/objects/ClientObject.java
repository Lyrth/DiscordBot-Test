package net.ddns.lyr.objects;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.User;
import net.ddns.lyr.handlers.EventHandler;
import net.ddns.lyr.main.BotConfig;
import net.ddns.lyr.modules.BotModules;

public class ClientObject {
    private volatile DiscordClient client;
    private volatile BotConfig config;
    private EventDispatcher eventDispatcher;
    private volatile EventHandler eventHandler;
    private BotModules modules;

    private User botUser;
    private ApplicationInfo applicationInfo;

    // public HashMap<Long,GuildObject> guilds;

    public ClientObject(DiscordClient client, BotConfig config){
        this.client = client;
        this.config = config;
        this.eventDispatcher = client.getEventDispatcher();
    }

    public void init(){
        this.eventHandler = new EventHandler(eventDispatcher);
        modules = new BotModules();
        //botUser = client.getSelf().block();
        //applicationInfo = client.getApplicationInfo().block();
    }

    public DiscordClient getClient() {
        return client;
    }

    public BotConfig getConfig() {
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
