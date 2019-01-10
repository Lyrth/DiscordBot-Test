package net.ddns.lyr.modules;

import net.ddns.lyr.main.Main;
import net.ddns.lyr.modules.guild.TypingTest;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuildModules {


    private Map<String, GuildModule> guildModules = new HashMap<>();

    public GuildModules(){

        add(new TypingTest());

    }

    private void add(GuildModule m){
        Mono.just(m).flatMap(module -> {
            if (!guildModules.containsKey(module.getName())) {
                guildModules.put(module.getName(), module);
                Log.logfDebug("> Adding module %s...", module.getName());
            }
            return Mono.empty();
        }).subscribe();
    }

    public Map<String, GuildModule> get(){
        return Collections.unmodifiableMap(guildModules);
    }


}
