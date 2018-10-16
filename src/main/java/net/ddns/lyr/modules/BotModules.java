package net.ddns.lyr.modules;

import net.ddns.lyr.main.Main;
import net.ddns.lyr.modules.bot.*;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.Module;
import net.ddns.lyr.utils.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BotModules {

    private Map<String, BotModule> botModules = new HashMap<>();

    public BotModules(){
        add(new Test());
    }

    private void add(BotModule module){
        if (!botModules.containsKey(module.getName())) {
            botModules.put(module.getName(), module);
            Log.logfDebug("> Adding module %s...", module.getName());
            Main.getEventHandler().registerBotEvent(module);
        }
    }

    public Map<String, BotModule> get(){
        return botModules;
    }

}
