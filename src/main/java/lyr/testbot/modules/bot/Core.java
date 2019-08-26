package lyr.testbot.modules.bot;

import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.handlers.CommandHandler;
import lyr.testbot.main.Main;
import lyr.testbot.templates.BotModule;
import lyr.testbot.util.Log;
import lyr.testbot.util.config.GuildConfig;
import lyr.testbot.util.config.GuildSetting;
import lyr.testbot.util.pagination.Paginator;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.stream.Collectors;

@ModuleInfo(
    desc = "The bot's core module. Will break if this was disabled. Handles commands and config set-up.",
    essential = true
)
public class Core extends BotModule {

    private CommandHandler commandHandler;

    public Core(){
        commandHandler = new CommandHandler(getClient().commands.getCommands());
    }

    public Mono<Void> on(MessageCreateEvent event){
        return commandHandler.handle(event)
            .doOnError(err -> {
                Log.logError(err.getMessage());
                err.printStackTrace();
            });
    }

    public Mono<Void> on(ReactionAddEvent e) {
        if (e.getUserId().equals(getClient().selfId)) return Mono.empty();
        return Paginator.onReact(e);
    }

    public Mono<Void> on(ReactionRemoveEvent e) {
        if (e.getUserId().equals(getClient().selfId)) return Mono.empty();
        return Paginator.onReactRemove(e);
    }

    public Mono<Void> on(ReadyEvent event){
        Log.logf("> Logged in as %s#%s.", event.getSelf().getUsername(), event.getSelf().getDiscriminator());

        int n = event.getGuilds().size();
        HashSet<Snowflake> ids = (HashSet<Snowflake>)
            event.getGuilds().stream().map(ReadyEvent.Guild::getId).collect(Collectors.toSet());
        if (ids.size() != n)
            Log.logWarn(">> Warning: Set of IDs not the same size as Set of Guilds in ReadyEvent.");
        // wait for all guilds to be received
        return getClient().getEventDispatcher()
            .on(GuildCreateEvent.class)
            .take(n).last()
            .doOnNext(gce -> {
                // instantiate each module for each guild
                getClient().getGuildSettings().forEach((guildId, setting) -> {
                    if (!ids.contains(guildId)) return;
                    Log.logfDebug("Setting up modules for guild %s", guildId.asString());
                    getClient().eventHandler.updateGuildModules(setting);
                });
            })
            .then(Mono.fromRunnable(System::gc));
    }

    public Mono<Void> on(GuildCreateEvent e) {
        Log.log("> New guild: ID " + e.getGuild().getId().asString() + ", Name: " + e.getGuild().getName());
        Main.client.getGuildSettings().computeIfAbsent(e.getGuild().getId(), id -> {
            GuildSetting setting = new GuildSetting(id);
            GuildConfig.updateGuildSettings(setting);
            return setting;
        });
        return Mono.empty();
    }
}
