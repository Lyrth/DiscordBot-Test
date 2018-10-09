package net.ddns.lyr.modules;

import net.ddns.lyr.modules.bot.*;
import net.ddns.lyr.modules.guild.*;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.Module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BotModules {

    private Map<String, BotModule> botModules = new HashMap<>();

    public BotModules(){
        add(new Test());
    }

    private void add(BotModule module){
        botModules.put(module.getName(),module);
    }

    public Map<String, BotModule> get(){
        return Collections.unmodifiableMap(botModules);
    }

}
