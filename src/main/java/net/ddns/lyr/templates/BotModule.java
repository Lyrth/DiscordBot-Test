package net.ddns.lyr.templates;

import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import net.ddns.lyr.annotations.ModuleEvent;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.modules.bot.Test;
import net.ddns.lyr.utils.AnnotationUtil;
import net.ddns.lyr.utils.Log;
import reactor.core.Disposable;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public abstract class BotModule implements Module {

    //protected Stream<Method> methodsStream;
    //private Stream<Disposable> registeredEvents;

    protected <C extends BotModule> void register(C c){
        AnnotationUtil.getAnnotatedMethods(c.getClass(),ModuleEvent.class)
            .forEach( m -> Main.getEventHandler()
                .registerEvent(
                    c.getClass().getName(),
                    m.getParameterTypes()[0].getName(),
                    m
                )
            );
    }

    //public abstract <E extends Event> void on(E event);

    /*
    @SuppressWarnings("unchecked")  // Class<T> casting
    public <T extends Event> BotModule eregister(EventDispatcher eventDispatcher){
        registeredEvents = methodsStream
            .map(m-> {
                try {
                    return eventDispatcher.on((Class<T>) m.getParameterTypes()[0]).subscribe();
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    Log.logError(m.getParameterTypes().length); return null; }
            }
        );
        return this;
    }

    public void unregister(){
        if(registeredEvents==null) return;
        registeredEvents.forEach(Disposable::dispose);
        registeredEvents = null;
    }
    */
}
