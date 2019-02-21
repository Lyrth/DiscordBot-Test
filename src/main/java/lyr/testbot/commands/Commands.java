package lyr.testbot.commands;

import lyr.testbot.commands.admin.*;
import lyr.testbot.commands.general.*;
import lyr.testbot.templates.Command;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Commands {

    private Map<String, Command> commands = new HashMap<>();

    public Commands(){
        add(
            // Admin
            new Module(),
            new Test(),

            // General
            new Me(),
            new Ping(),
            new Usage()
        );
    }

    private void add(Command... c){
        Flux.just(c).doOnNext(command -> {
            if (!commands.containsKey(command.getName().toLowerCase())) {
                commands.put(command.getName().toLowerCase(), command);
            }
        }).subscribe();
    }

    public Map<String, Command> getCommands(){
        return Collections.unmodifiableMap(commands);
    }

}
