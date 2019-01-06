package net.ddns.lyr.utils.config;

import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.templates.ModuleSettings;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class GuildSetting {

    public final String guildId;

    public Collection<String> enabledModules = Collections.emptyList();

    // Each module has its own config file, hence transient.
    //   ModuleName, <SettingKey, SettingValue>
    public transient HashMap<String, HashMap<String,String>> modulesSettings = new HashMap<>();

    public GuildSetting(Snowflake guildId){
        this.guildId = guildId.asString();
    }

    public void setModulesSettings(HashMap<String, HashMap<String, String>> modulesSettings) {
        this.modulesSettings = modulesSettings;
    }

    public boolean isModuleEnabled(String moduleName){
        return enabledModules.contains(moduleName);
    }

    public boolean hasSettings(String moduleName){
        return modulesSettings.get(moduleName) != null; //&& !modulesSettings.get(moduleName).isEmpty(); // possible that a module has no settings
    }

    public String getSetting(String moduleName, String key){
        if (!hasSettings(moduleName)) return null;
        return modulesSettings.get(moduleName).get(key);
    }

    //public String setSetting(){ } // TODO add setSetting and update file accordingly. (have interval between writes?) Also, MULTIPLE MODULE CONFIIIIIGS

}
