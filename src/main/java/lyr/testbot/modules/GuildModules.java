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

    // for case insensitive searching. <lowercaseName, properName>
    private Map<String, String> guildModuleNameMap = new HashMap<>();

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
                guildModuleNameMap.put(module.getName().toLowerCase(),module.getName());
                Log.logfDebug("> Adding module %s...", module.getName());
            }
        }).subscribe();
    }

    public Map<String, GuildModule> get(){
        return Collections.unmodifiableMap(guildModules);
    }

    public GuildModule getModule(String moduleName){
        return guildModules.getOrDefault(moduleName,
            guildModules.get(guildModuleNameMap.get(moduleName.toLowerCase()))
        );
    }

    public String getProperName(String moduleName){
        if (guildModules.containsKey(moduleName)) return moduleName;
        else if (guildModules.containsKey(guildModuleNameMap.get(moduleName.toLowerCase())))
            return guildModuleNameMap.get(moduleName.toLowerCase());
        else return "";
    }

    private boolean isGuildModule(String moduleName){
        return guildModules.containsKey(moduleName) ||
            guildModules.containsKey(guildModuleNameMap.get(moduleName.toLowerCase()));
    }

}
