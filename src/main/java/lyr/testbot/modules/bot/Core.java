package lyr.testbot.modules.bot;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.handlers.CommandHandler;
import lyr.testbot.templates.BotModule;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.GuildConfig;
import lyr.testbot.util.config.GuildSetting;
import lyr.testbot.util.pagination.Paginator;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@ModuleInfo(
    desc = "The bot's core module. Will break if this was disabled. Handles commands and config set-up.",
    essential = true
)
public class Core extends BotModule {

    private CommandHandler commandHandler;

    public Core(){
        commandHandler = new CommandHandler(Collections.emptyMap());  // Don't handle cmmands yet until ready.
    }

    public Mono<Void> on(MessageCreateEvent event){
        return commandHandler.handle(event);
    }

    public Mono<Void> on(ReactionAddEvent e) {
        return getClient().getId()
            .filter(id -> !e.getUserId().equals(id))
            .map(id -> e)
            .flatMap(Paginator::onReact);
    }

    public Mono<Void> on(ReactionRemoveEvent e) {
        return getClient().getId()
            .filter(id -> !e.getUserId().equals(id))
            .map(id -> e)
            .flatMap(Paginator::onReactRemove);
    }


    // TODO: Sometimes GuildCreateEvent fires first before ReadyEvent, causing this to halt
    public Mono<Void> on(ReadyEvent event){
        Log.infoFormat("> Logged in as %s#%s.", event.getSelf().getUsername(), event.getSelf().getDiscriminator());
        commandHandler = new CommandHandler(getClient().commands.getCommands());

        int n = event.getGuilds().size();
        HashSet<Snowflake> ids = (HashSet<Snowflake>)
            event.getGuilds().stream().map(ReadyEvent.Guild::getId).collect(Collectors.toSet());
        if (ids.size() != n)
            Log.warn(">> Warning: Set of IDs not the same size as Set of Guilds in ReadyEvent.");
        // wait for all guilds to be received
        return getClient().getEventDispatcher()
            .on(GuildCreateEvent.class)
            .take(n).last()
            .doOnNext(gce -> {
                // instantiate each module for each guild
                getClient().getGuildSettings().forEach((guildId, setting) -> {
                    if (!ids.contains(guildId)) return;
                    Log.debugFormat("Setting up modules for guild %s", guildId.asString());
                    getClient().eventHandler.updateGuildModules(setting);
                });
            })
            .then(Mono.fromRunnable(System::gc));
    }

    public Mono<Void> on(GuildCreateEvent e) {
        Log.info("> New guild: ID " + e.getGuild().getId().asString() + ", Name: " + e.getGuild().getName());
        getClient().getGuildSettings().computeIfAbsent(e.getGuild().getId(), id -> {
            GuildSetting setting = new GuildSetting(id);
            GuildConfig.updateGuildSettings(setting);
            return setting;
        });
        return Mono.empty();
    }
}
