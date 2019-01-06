package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;
import net.ddns.lyr.utils.config.GuildSetting;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class GuildModuleCore extends BotModule {


    public HashMap<Snowflake, GuildSetting> guildSettings;
    private Map<String, GuildModule> availableGuildModules;
    private HashMap<Snowflake, HashMap<String, GuildModule>> activeGuildModules = new HashMap<>();;
    private Flux<Guild> guilds;

    public GuildModuleCore() {
        guildSettings = Main.client.getGuildSettings();  // to get the list of guilds w/ config = possible activated module
        availableGuildModules = Main.client.availableGuildModules.get();
        guilds = Main.client.guilds;
    }

    public void on(ReadyEvent e){
        int n = e.getGuilds().size();
        HashSet<Snowflake> ids = (HashSet<Snowflake>)
            e.getGuilds().stream().map(ReadyEvent.Guild::getId).collect(Collectors.toSet());
        if (ids.size() != n) Log.logWarn(">> Warning: Set of IDs not the same size as Set of Guilds in ReadyEvent.");
        // wait for all guilds to be received
        Main.client.getEventDispatcher().on(GuildCreateEvent.class).take(n).last().subscribe(gce ->
            // instantiate each module for each guild
            guildSettings.forEach((guildId,setting) -> {
                if (!ids.contains(guildId)) return;
                Log.logfDebug("> Setting up modules for guild %s", guildId.asString());
                setting.enabledModules.forEach(module -> {
                    if (availableGuildModules.containsKey(module)){
                        Log.logfDebug("> | Module %s", module);
                        availableGuildModules.get(module)
                            .newInstance(
                                guilds.filter(guild->guild.getId().equals(guildId)).single(),  // or .last()
                                setting
                            );
                    }
                });
            })
        );
    }


    public String getName() {
        return this.getClass().getSimpleName();
    }

}
