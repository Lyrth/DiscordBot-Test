package lyr.testbot.util;

import discord4j.core.event.domain.Event;
import lyr.testbot.enums.EventType;

public class EventUtil {
    public static Class<? extends Event> getEvent(String name){
        return EventType.valueOf(name.replaceAll("Event$","")).e;
    }
}
