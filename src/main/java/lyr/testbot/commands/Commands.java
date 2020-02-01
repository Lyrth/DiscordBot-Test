package lyr.testbot.commands;

import lyr.testbot.commands.admin.*;
import lyr.testbot.commands.admin.Module;  // Since upgrade to JDK 11, "Module" is now part of java.lang.
import lyr.testbot.commands.general.*;
import lyr.testbot.commands.music.*;
import lyr.testbot.templates.Command;

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
            new Usage(),
            new Gc(),

            new Shop(),

            // General
            new lyr.testbot.commands.general.Commands(),
            new Help(),
            new Me(),
            new Ping(),

            // Music
            new Join(),
            new Play(),
            new Stop()
        );
    }

    private void add(Command... c){
        for(Command command : c)
            if (!commands.containsKey(command.getName().toLowerCase())) {
                commands.put(command.getName().toLowerCase(), command);
            }
    }

    public Map<String, Command> getCommands(){
        return Collections.unmodifiableMap(commands);
    }

}
