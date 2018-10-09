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

    public Mono<Void> handle(MessageCreateEvent mEvent){
        return Mono.just(mEvent)
            .map(e->{st = System.nanoTime(); return e;})
            .filter(this::shouldHandle)
            .flatMap(event -> {
                Optional<Command> command = event.getMessage().getContent()
                    .map(this::getCommandName)
                    .flatMap(this::getCommand);

                return Mono.justOrEmpty(command)
                    .flatMap(cmd ->
                        event.getMessage().getChannel()
                            .map(ch ->
                                cmd.execute(new CommandObject(event)).defaultIfEmpty("")
                                    .subscribe(m ->
                                        ch.createMessage(m).subscribe(i->{},err->Log.logError(err.getMessage()))
                                    )
                            )
                    );
            })
            .map(e->{en = System.nanoTime(); Log.log(((en-st)/1000/1000f)+"ms"); /*System.gc();*/ return e;}).then();
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
