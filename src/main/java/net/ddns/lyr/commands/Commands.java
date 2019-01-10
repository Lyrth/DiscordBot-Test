package net.ddns.lyr.commands;

import net.ddns.lyr.commands.admin.*;
import net.ddns.lyr.commands.general.*;
import net.ddns.lyr.templates.Command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Commands {

    private Map<String, Command> commands = new HashMap<>();

    public Commands(){
        add(new Ping());
        add(new Me());
        add(new Test());
        add(new Module());
    }

    private void add(Command command){
        commands.put(command.getName(),command);
    }

    public Map<String, Command> getCommands(){
        return Collections.unmodifiableMap(commands);
    }

}
