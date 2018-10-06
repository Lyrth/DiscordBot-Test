package net.ddns.lyr.modules;

import net.ddns.lyr.modules.bot.*;
import net.ddns.lyr.modules.guild.*;
import net.ddns.lyr.templates.Module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BotModules {

    private Map<String, Module> botModules = new HashMap<>();

    public BotModules(){
        add(new Test());
    }

    private void add(Module module){
        botModules.put(module.getName(),module);
    }

    public Map<String, Module> get(){
        return Collections.unmodifiableMap(botModules);
    }

}
