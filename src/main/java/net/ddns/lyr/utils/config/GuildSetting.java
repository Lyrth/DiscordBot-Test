package net.ddns.lyr.utils.config;

import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.main.Main;

import java.util.HashMap;
import java.util.HashSet;

public class GuildSetting {

    public final Snowflake guildId;

    public HashSet<String> enabledModules = new HashSet<>();

    // Each module has its own config file, hence transient.
    //   ModuleName, <SettingKey, SettingValue>
    public transient HashMap<String, HashMap<String,String>> modulesSettings = new HashMap<>();

    public GuildSetting(Snowflake guildId){
        this.guildId = guildId;
    }

    private boolean updateEnabledModules(){
        Main.client.eventHandler.updateGuildModules(this);
        GuildConfig.updateGuildSettings(this);
        return true;
    }

    public void setModulesSettings(HashMap<String, HashMap<String, String>> modulesSettings) {
        this.modulesSettings = modulesSettings;
        GuildConfig.updateModulesSettings(modulesSettings,guildId.asString());
    }

    public boolean isModuleEnabled(String moduleName){
        return enabledModules.contains(moduleName);
    }

    public boolean enableModule(String moduleName){
        return enabledModules.add(moduleName) && updateEnabledModules();
    }

    public boolean disableModule(String moduleName){
        return enabledModules.remove(moduleName) && updateEnabledModules();
    }

    // returns: true if module just activated, false if not.
    public boolean toggleModule(String moduleName){
        if(isModuleEnabled(moduleName)){
            disableModule(moduleName);
            return false;
        } else {
            enableModule(moduleName);
            return true;
        }
    }

    public boolean hasModuleSettings(String moduleName){
        return modulesSettings.get(moduleName) != null; //&& !modulesSettings.get(moduleName).isEmpty(); // possible that a module has no settings
    }

    public String getModuleSetting(String moduleName, String key){
        if (!hasModuleSettings(moduleName)) return null;
        return modulesSettings.get(moduleName).get(key);
    }

    public void setModuleSetting(String moduleName, String key, String value){
        if (hasModuleSettings(moduleName)) {
            modulesSettings.get(moduleName).put(key,value);
        } else {
            HashMap<String,String> map = new HashMap<>();
            map.put(key,value);
            modulesSettings.put(moduleName,map);
        }
        GuildConfig.updateModuleSettings(moduleName, modulesSettings.get(moduleName), guildId.asString());
    }
}
