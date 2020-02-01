package lyr.testbot.util.config;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.main.Main;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;

public class GuildSetting {

    public final Snowflake guildId;

    public HashSet<String> enabledModules = new HashSet<>();

    // Each module has its own config file, hence transient.
    //   ModuleName, <SettingKey, SettingValue>
    public transient HashMap<String, HashMap<String,String>> modulesSettings = new HashMap<>();

    // For Main.client.eventHandler.updateGuildModules use.
    public transient Mono<Guild> guild;

    public GuildSetting(Snowflake guildId){
        this.guildId = guildId;
        this.guild = Main.client.guilds.filter(guild -> guildId.equals(guild.getId())).next();
    }

    private Mono<Boolean> updateEnabledModules(){
        return Main.client.eventHandler.updateGuildModules(this)
            .then(GuildConfig.updateGuildSettings(this))
            .thenReturn(true);  // TODO: schedule update on a fixed interval instead & add shutdown hook
    }

    public Mono<Void> setModulesSettings(HashMap<String, HashMap<String, String>> modulesSettings) {
        return Mono.fromCallable(() -> this.modulesSettings = modulesSettings)
            .flatMap(set -> GuildConfig.updateModulesSettings(set,guildId.asString()));  // TODO: same as above)
    }

    public boolean isModuleEnabled(String moduleName){
        return enabledModules.contains(moduleName);
    }

    public Mono<Boolean> enableModule(String moduleName){
        return Mono.fromCallable(() -> enabledModules.add(moduleName))
            .filter(b -> b)
            .filterWhen($ -> updateEnabledModules())
            .map($ -> true)
            .defaultIfEmpty(false);
    }

    public Mono<Boolean> disableModule(String moduleName){
        return Mono.fromCallable(() -> enabledModules.remove(moduleName))
            .filter(b -> b)
            .filterWhen($ -> updateEnabledModules())
            .map($ -> true)
            .defaultIfEmpty(false);
    }

    // returns: true if module just activated, false if we disabled it.
    public Mono<Boolean> toggleModule(String moduleName){
        return Mono.just(moduleName)
            .filter(this::isModuleEnabled)
            .flatMap(this::disableModule)
            .map($ -> false)
            .switchIfEmpty(enableModule(moduleName).thenReturn(true));
    }

    public Mono<Boolean> hasModuleSettings(String moduleName){
        return Mono.fromCallable(() -> modulesSettings.get(moduleName) != null);
        //&& !modulesSettings.get(moduleName).isEmpty();  // possible that a module has no settings
    }

    public Mono<String> getModuleSetting(String moduleName, String key){
        return Mono.fromCallable(() -> modulesSettings.get(moduleName))
            .doOnNext(Log::screamB)
            .map(m -> m.get(key));
    }

    public Mono<Void> setModuleSetting(String moduleName, String key, String value){
        return Mono.just(moduleName)
            .filterWhen(this::hasModuleSettings)
            .map(modulesSettings::get)
            .doOnNext(map -> map.put(key,value))
            .switchIfEmpty(
                Mono.just(new HashMap<String,String>())
                    .doOnNext(map -> map.put(key,value))
                    .doOnNext(map -> modulesSettings.put(moduleName,map))
            )
            .flatMap(map -> GuildConfig.updateModuleSettings(moduleName, map, guildId.asString()));  // TODO: same as above
    }
}
