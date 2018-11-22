package net.ddns.lyr.utils.config;

import java.util.Collection;
import java.util.Collections;

public class GuildSetting {

    public Collection<String> enabledModules;

    public GuildSetting(){
        this.enabledModules = Collections.emptyList();
    }
}
