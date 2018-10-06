package net.ddns.lyr.utils;

import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import net.ddns.lyr.enums.EventType;

public class EventUtil {
    public static Class<? extends Event> getEvent(String name){
        return EventType.valueOf(name.replaceAll("Event$","")).e;
    }
}
