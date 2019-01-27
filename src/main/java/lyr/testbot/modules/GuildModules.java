package lyr.testbot.modules;

import lyr.testbot.modules.guild.*;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.Log;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GuildModules {

    private Map<String, GuildModule> guildModules = new HashMap<>();

    public GuildModules(){
        add(
            new TypingTest(),
            new SettingTest(),
            new IntervalTest()
        );
    }

    private void add(GuildModule... m){
        Flux.just(m).doOnNext(module -> {
            if (!guildModules.containsKey(module.getName())) {
                guildModules.put(module.getName(), module);
                Log.logfDebug("> Adding module %s...", module.getName());
            }
        }).subscribe();
    }

    public Map<String, GuildModule> get(){
        return Collections.unmodifiableMap(guildModules);
    }

}
