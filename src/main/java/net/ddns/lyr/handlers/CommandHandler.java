package net.ddns.lyr.handlers;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.objects.CommandObject;
import net.ddns.lyr.templates.Command;
import net.ddns.lyr.utils.Log;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CommandHandler {

    private final Map<String, Command> commands;
    private String prefix;
    private int prefixLength;

    long st, en;

    public CommandHandler(Map<String, Command> commands) {
        this.commands = commands;
        prefix = Main.getClient().getConfig().getPrefix();
        prefixLength = prefix.length();
    }

    Mono<Void> handle(MessageCreateEvent mEvent){
        return Mono.just(mEvent)
            .map(e->{st = System.nanoTime(); return e;})  //  <timeLogging>
            .filter(this::shouldHandle)
            .flatMap(event ->
                Mono.justOrEmpty(
                    event.getMessage().getContent()
                        .map(this::getCommandName)
                        .flatMap(this::getCommand)    // Optional<Command>
                )
                .flatMap(cmd ->
                    event.getMessage().getChannel()
                        .flatMap(ch ->
                            cmd.execute(new CommandObject(event))
                                .filter(s -> (s!=null && !s.isEmpty()))
                                .flatMap(ch::createMessage)
                        )
                )
            )
            .map(e->{    //  </timeLogging>
                en = System.nanoTime();
                Log.logfDebug("> Command taken %.3fms",(en-st)/1000_000f);
                return e;
            }).then();
    }

    private boolean shouldHandle(MessageCreateEvent mEvent) {
        return mEvent.getMessage().getContent()
            .map(c -> c.startsWith(prefix)||c.matches("^<@!?"+0+"> .*"))
            .orElse(false);
    }

    private String getCommandName(String content) {
        int end = content.contains(" ") ? content.indexOf(" ", prefixLength) : content.length();
        return content.substring(prefixLength, end);
    }

    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
