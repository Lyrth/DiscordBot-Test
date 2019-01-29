package lyr.testbot.modules.bot;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.commands.Commands;
import lyr.testbot.handlers.CommandHandler;
import lyr.testbot.main.Main;
import lyr.testbot.templates.BotModule;
import lyr.testbot.util.Log;

import java.util.HashSet;
import java.util.stream.Collectors;

public class Core extends BotModule {

    private CommandHandler commandHandler;

    public Core(){
        commandHandler = new CommandHandler(new Commands().getCommands());
    }

    public void on(MessageCreateEvent event){
        commandHandler.handle(event)
            .doOnError(err -> {
                Log.logError(err.getMessage());
                err.printStackTrace();
            })
            .subscribe();
    }

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
