package net.ddns.lyr.modules.bot;

import discord4j.core.event.domain.channel.*;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageEditSpec;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.templates.BotModule;
import net.ddns.lyr.utils.Log;

import java.time.Duration;

public class Test extends BotModule {

    public String getName() {
        return this.getClass().getSimpleName();
    }

}
