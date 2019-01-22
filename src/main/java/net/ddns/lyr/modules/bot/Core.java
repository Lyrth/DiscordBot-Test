package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.commands.Commands;
import net.ddns.lyr.handlers.CommandHandler;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;

import java.util.HashSet;
import java.util.stream.Collectors;

public class Core extends BotModule {

    private CommandHandler commandHandler;

    public Core(){
        commandHandler = new CommandHandler(new Commands().getCommands());
    }

    @ModuleEvent
    public void on(MessageCreateEvent event){
        commandHandler.handle(event)
            .doOnError(err -> {
                Log.logError(err.getMessage());
                err.printStackTrace();
            })
            .subscribe();
    }

    @ModuleEvent
    public void on(ReadyEvent event){
        Log.logf("> Logged in as %s#%s.", event.getSelf().getUsername(), event.getSelf().getDiscriminator());

        int n = event.getGuilds().size();
        HashSet<Snowflake> ids = (HashSet<Snowflake>)
            event.getGuilds().stream().map(ReadyEvent.Guild::getId).collect(Collectors.toSet());
        if (ids.size() != n)
            Log.logWarn(">> Warning: Set of IDs not the same size as Set of Guilds in ReadyEvent.");
        // wait for all guilds to be received
        Main.client.getEventDispatcher().on(GuildCreateEvent.class).take(n).last().subscribe(gce -> {
            // instantiate each module for each guild
            Main.client.getGuildSettings().forEach((guildId, setting) -> {
                if (!ids.contains(guildId)) return;
                Log.logfDebug("> Setting up modules for guild %s", guildId.asString());
                Main.client.eventHandler.updateGuildModules(setting);
            });
        });

        System.gc();
    }

}
