package net.ddns.lyr.templates;

import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.guild.GuildEvent;
import net.ddns.lyr.utils.Log;
import reactor.core.Disposable;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public abstract class GuildModule implements Module {

    protected Stream<Method> methodsStream;
    private Stream<Disposable> registeredEvents;

    @SuppressWarnings("unchecked")  // Class<T> casting
    public void register(EventDispatcher eventDispatcher){
        registeredEvents = methodsStream
            .map(m-> {
                try {
                    return eventDispatcher.on((Class<GuildEvent>) m.getParameterTypes()[0]).subscribe();
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    Log.logError(m.getParameterTypes().length); return null; }
            }
        );
    }

    public void unregister(){
        if(registeredEvents==null) return;
        registeredEvents.forEach(Disposable::dispose);
        registeredEvents = null;
    }
}
