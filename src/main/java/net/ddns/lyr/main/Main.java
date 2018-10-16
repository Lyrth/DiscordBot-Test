package net.ddns.lyr.main;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.store.jdk.JdkStoreService;
import net.ddns.lyr.handlers.EventHandler;
import net.ddns.lyr.objects.ClientObject;
import net.ddns.lyr.utils.Log;

public class Main {

    public static volatile ClientObject client;
    private static volatile BotConfig config;
    private static volatile EventHandler eventHandler;

    public static void main(String args[]) {
        System.setProperty("log4j.skipJansi","false");  // Color support for logging.
        new Main();
    }

    private Main(){
        Log.log("> Starting...");
        config = BotConfig.readConfig();
        if (config.getToken().isEmpty()) return;

        client = new ClientObject(
            new DiscordClientBuilder(config.getToken())
                .setStoreService(new JdkStoreService())
                .setInitialPresence(Presence.doNotDisturb(Activity.listening("aaaa")))
                .build(),
            config);
        client.init();

        int retries = 3;
        while (retries>0)
        try {
            client.getClient().login().block();
        } catch (Exception e) {
            if (e.getMessage().matches(".*java\\.net\\..*?Exception.*")) {
                retries--;
                Log.logWarn("> Trying to log in again. Internet dropped? Retries: " + retries);
            } else retries = 0;
        }
        Log.log("> End.");
    }

    public static ClientObject getClient(){
        return client;
    }

    public static DiscordClient getDiscordClient(){
        return client.getClient();
    }

    public static EventDispatcher getEventDispatcher() {
        return client.getEventDispatcher();
    }

    public static EventHandler getEventHandler() {
        return client.getEventHandler();
    }

    public static BotConfig getConfig(){
        return client.getConfig();
    }
}
