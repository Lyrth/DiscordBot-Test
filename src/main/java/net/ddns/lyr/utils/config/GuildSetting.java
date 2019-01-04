package net.ddns.lyr.utils.config;

import net.ddns.lyr.templates.ModuleSettings;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class GuildSetting {

    public Collection<String> enabledModules;

    public HashMap<String, HashMap<String,String>> modulesSettings;  // ModuleName, SettingKey, SettingValue

    public GuildSetting(){
        this.enabledModules = Collections.emptyList();
        this.modulesSettings = new HashMap<>();
    }
}
