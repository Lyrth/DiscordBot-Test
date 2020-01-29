package lyr.testbot.modules;

import lyr.testbot.main.Main;
import lyr.testbot.modules.bot.*;
import lyr.testbot.templates.BotModule;
import lyr.testbot.util.Log;

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
        for(BotModule module : m)
            if (!botModules.containsKey(module.getName())) {
                botModules.put(module.getName(), module);
                Log.debugFormat("Adding module %s...", module.getName());
                Main.client.getEventHandler().registerBotEvent(module);  // TODO: read config en/disable
            }
    }

    public Map<String, BotModule> get(){
        return Collections.unmodifiableMap(botModules);
    }

}
