package net.ddns.lyr.utils.config;

import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.templates.ModuleSettings;

import java.util.Collection;
import java.util.Collections;
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

    public void setModulesSettings(HashMap<String, HashMap<String, String>> modulesSettings) {
        this.modulesSettings = modulesSettings;
    }

    public boolean isModuleEnabled(String moduleName){
        return enabledModules.contains(moduleName);
    }

    public boolean enableModule(String moduleName){
        return enabledModules.add(moduleName);
    }

    public boolean disableModule(String moduleName){
        return enabledModules.remove(moduleName);
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

    public boolean hasSettings(String moduleName){
        return modulesSettings.get(moduleName) != null; //&& !modulesSettings.get(moduleName).isEmpty(); // possible that a module has no settings
    }

    public String getSetting(String moduleName, String key){
        if (!hasSettings(moduleName)) return null;
        return modulesSettings.get(moduleName).get(key);
    }

    //public String setSetting(){ } // TODO  update file accordingly. (have interval between writes?)
    public void setSetting(String moduleName, String key, String value){
        if (hasSettings(moduleName)) {
            modulesSettings.get(moduleName).put(key,value);
        } else {
            HashMap<String,String> map = new HashMap<>();
            map.put(key,value);
            modulesSettings.put(moduleName,map);
        }
    }
}
