package lyr.testbot.handlers;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.rest.http.client.ClientException;
import lyr.testbot.enums.CommandType;
import lyr.testbot.main.Main;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.templates.Command;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CommandHandler {

    private final Map<String, Command> commands;
    private String prefix;
    private String selfId;

    public CommandHandler(Map<String, Command> commands) {
        this.commands = commands;
        prefix = Main.client.getBotConfig().getPrefix();
        selfId = Main.client.getId().asString();
    }

    public Mono<Void> handle(MessageCreateEvent mEvent){
        return Mono.just(mEvent)
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
                                .filter(r -> !r.isEmpty())
                                .flatMap(r -> ch.createMessage(r).retry(3,t -> !(t instanceof ClientException)))
                        )
                )
            )
            .onErrorResume(e -> mEvent.getMessage().getChannel()
                    .flatMap(ch -> ch.createMessage(e.getMessage()).retry(3,t -> !(t instanceof ClientException))))
            .doOnError(err -> {
                Log.logError(">>> CommandHandler Error: " + err.getMessage());
                err.printStackTrace();
            })
            .then();
    }

    private boolean shouldHandle(MessageCreateEvent mEvent) {
        return mEvent.getMessage().getContent()
            .map(c -> c.startsWith(prefix)||c.startsWith("<@!"+selfId+"> ")||c.startsWith("<@"+selfId+"> "))
            .orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean allowed(Optional<Member> member, Command cmd){
        if (!member.isPresent()){
            return true;                       // TODO
        }
        if (cmd.getType() == CommandType.OWNER){
            return member.get().getId().equals(Main.client.ownerId);
        }
    }

    private String getCommandName(String content) {
        return content.replaceFirst("(^<@!?"+selfId+">\\s+?|^\\Q"+prefix+"\\E)(\\S+).*$","$2");
            //.substring(content.matches("^<@!?"+selfId+">\\s+?.*") ? 0 : prefixLength);
    }

    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(commands.get(name.toLowerCase()));
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
