package net.ddns.lyr.modules;

import net.ddns.lyr.main.Main;
import net.ddns.lyr.modules.bot.*;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BotModules {

    private Map<String, BotModule> botModules = new HashMap<>();

    public BotModules(){
        // Core module.
        add(new Core());

        // Helper for GuildModules
        add(new GuildModuleCore());

        add(new Test());
    }

    private void add(BotModule m){
        Mono.just(m).flatMap(module -> {
            if (!botModules.containsKey(module.getName())) {  // TODO: read config en/disable
                botModules.put(module.getName(), module);
                Log.logfDebug("> Adding module %s...", module.getName());
                return Main.client.getEventHandler().registerBotEvent(module);
            }
            return Mono.empty();
        }).subscribe();
    }

    public Map<String, BotModule> get(){
        return Collections.unmodifiableMap(botModules);
    }

}
