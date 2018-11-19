package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.commands.Commands;
import net.ddns.lyr.handlers.CommandHandler;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;

public class Core extends BotModule {

    private CommandHandler commandHandler;

    public Core(){
        commandHandler = new CommandHandler(new Commands().getCommands());
    }

    @ModuleEvent
    public void on(MessageCreateEvent event){
        commandHandler.handle(event)
            .subscribe( i -> {}, err -> Log.logError(err.getMessage()));
    }

    @ModuleEvent
    public void on(ReadyEvent event){
        Log.log("> Logged in as " + event.getSelf().getUsername());
        System.gc();
    }

    public String getName() {
        return "test";
    }

}
