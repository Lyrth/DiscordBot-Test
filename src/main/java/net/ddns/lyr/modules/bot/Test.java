package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.channel.*;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;

import java.time.Duration;

public class Test extends BotModule {

    @ModuleEvent
    public void on(TypingStartEvent event){
        /*event.getUser()
            .flatMap(User::getPrivateChannel)
            .flatMap( ch -> ch.createMessage("I saw you typing!"))
            .delayElement(Duration.ofSeconds(15))
            .flatMap( m -> m.edit(s -> s.setContent("[REDACTED]")))
            .subscribe(m -> {},err -> {
                Log.logError("> REEEE CANNOT SEND MESSAGE ;-;");
                err.printStackTrace();
            });*/
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

}
