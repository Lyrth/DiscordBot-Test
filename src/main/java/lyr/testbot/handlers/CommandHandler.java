package lyr.testbot.handlers;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import discord4j.rest.http.client.ClientException;
import lyr.testbot.enums.CommandType;
import lyr.testbot.main.Main;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.templates.Command;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CommandHandler {

    private final Map<String, Command> commands;
    private String prefix;
    private Mono<Snowflake> selfId;

    public CommandHandler(Map<String, Command> commands) {
        this.commands = commands;
        prefix = Main.client.getBotConfig().getPrefix();
        selfId = Main.client.getId();
    }

    public Mono<Void> handle(MessageCreateEvent mEvent){
        return Mono.just(mEvent)
            .filterWhen(this::shouldHandle)
            .flatMap(event ->
                Mono.justOrEmpty(event.getMessage().getContent())
                    .flatMap(this::getCommandName)
                    .flatMap(this::getCommand)
                    .flatMap(cmd ->
                        event.getMessage().getChannel()
                            .flatMap(ch ->
                                cmd.execute(new CommandObject(event))
                                    .filter(Reply::isNotEmpty)
                                    .flatMap(r -> ch.createMessage(r).retry(3,t -> !(t instanceof ClientException)))
                            )
                    )
            )
            .onErrorResume(e -> mEvent.getMessage().getChannel()
                .flatMap(ch -> ch.createMessage(e.getMessage()).retry(3,t -> !(t instanceof ClientException))))
            .doOnError(err -> {
                Log.error(">>> CommandHandler Error: " + err.getMessage());
                err.printStackTrace();
            })//.log("reactorLog", Level.FINEST, true)
            .then();
    }

    private Mono<Boolean> shouldHandle(MessageCreateEvent e) {
        return Mono.justOrEmpty(e.getMessage().getContent())
            .zipWith(selfId)
            .map(tup ->
                tup.getT1().matches(String.format("(\\Q%s\\E|<@!?%s>\\s+?)(.+)",prefix,tup.getT2().asString()))
            );
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Mono<Boolean> allowed(Optional<Member> member, Optional<User> user, Command cmd){
        return Mono.just(cmd.getType())
            .filter(CommandType.OWNER::equals)   // if for owner only
            .flatMap(t -> Main.client.getOwnerId())
            .map(id ->
                user.map(u -> u.getId().equals(id))
                    .orElse(member.map(m -> m.getId().equals(id))
                        .orElse(false))
            )
            .switchIfEmpty(
                Mono.just(false)    // TODO: check perms
            );
    }

    private Mono<String> getCommandName(String content) {
        return selfId
            .map(Snowflake::asString)
            .map(id -> content.replaceFirst("(^\\Q"+prefix+"\\E|^<@!?"+id+">\\s+)(\\S+).*$","$2"));
    }

    public Mono<Command> getCommand(String name) {
        return Mono.justOrEmpty(commands.get(name.toLowerCase()));
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
