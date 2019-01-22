package net.ddns.lyr.modules;

import net.ddns.lyr.main.Main;
import net.ddns.lyr.modules.bot.*;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BotModules {

    private Map<String, BotModule> botModules = new HashMap<>();

    public BotModules(){
        add(
            // Core module.
            new Core(),

            new Test()
        );
    }

    private void add(BotModule... m){
        Flux.just(m).doOnNext(module -> {
            if (!botModules.containsKey(module.getName())) {
                botModules.put(module.getName(), module);
                Log.logfDebug("> Adding module %s...", module.getName());
                Main.client.getEventHandler().registerBotEvent(module);  // TODO: read config en/disable
            }
        }).subscribe();
    }

    public Map<String, BotModule> get(){
        return Collections.unmodifiableMap(botModules);
    }

}
