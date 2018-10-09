package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.channel.*;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.AnnotationUtil;
import net.ddns.lyr.utils.Log;

import java.time.Duration;

public class Test extends BotModule {

    @ModuleEvent
    public void on(TypingStartEvent event){
        event.getUser().flatMap(User::getPrivateChannel)
            .subscribe( ch ->
                ch.createMessage("I saw you typing!").subscribe(
                    m -> // On success
                        m.edit(new MessageEditSpec().setContent("[REDACTED]"))
                            .delaySubscription(Duration.ofSeconds(15))
                            .subscribe()
                    ,err -> {
                        Log.logError("REEEE CANNOT SEND MESSAGE ;-;");
                        err.printStackTrace();
                    })
            );
    }

    public String getName() {
        return "test";
    }

}
