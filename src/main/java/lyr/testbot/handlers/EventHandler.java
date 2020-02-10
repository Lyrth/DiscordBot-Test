package lyr.testbot.handlers;

import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import discord4j.core.object.util.Snowflake;
import lyr.testbot.main.Main;
import lyr.testbot.templates.BotModule;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.templates.Module;
import lyr.testbot.util.Log;
import lyr.testbot.util.ReflectionUtil;
import lyr.testbot.util.config.GuildSetting;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class EventHandler {
    private EventDispatcher eventDispatcher;

    // <moduleName, module>
    private HashMap<String, Disposable.Composite>
        activeBotModules = new HashMap<>();

    // <guildId, <moduleName, module>>
    private HashMap<Snowflake, HashMap<String, Disposable.Composite>>
        activeGuildModules = new HashMap<>();

    public EventHandler(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        Log.debug("EventHandler ready.");
    }

    public void registerBotEvent(BotModule module){
        Log.debugFormat("| Enabling bot module %s...", module.getName());
        activeBotModules.computeIfAbsent(module.getName(), $ -> subscribeModule(module));
    }

    public void unregisterBotEvent(BotModule module){
        unregisterBotEvent(module.getName());
    }

    public void unregisterBotEvent(String moduleName){
        Log.debugFormat("Unregistering %s...", moduleName);
        activeBotModules.remove(moduleName).dispose();
    }


    public Mono<Void> updateGuildModules(GuildSetting setting){
        Snowflake guildId = setting.guildId;
        return Mono.fromRunnable(() -> activeGuildModules.computeIfAbsent(guildId, $ -> new HashMap<>()))
            .thenMany(Flux.fromIterable(Main.client.availableGuildModules.get().entrySet()))
            .doOnNext(entry -> {
                if (setting.enabledModules.contains(entry.getKey())){  // It should be enabled
                    if (activeGuildModules.get(guildId).containsKey(entry.getKey()))  // Already enabled
                        return;
                    Log.debugFormat("| Enabling module %s...", entry.getKey());
                    GuildModule guildModule = entry.getValue().newInstance(setting);
                    activeGuildModules.get(guildId).put(entry.getKey(),subscribeModule(guildModule));
                } else { // Should be disabled.
                    if (!activeGuildModules.get(guildId).containsKey(entry.getKey()))  // Already disabled
                        return;
                    Log.debugFormat("| Disabling module %s...", entry.getKey());
                    activeGuildModules.get(guildId).remove(entry.getKey()).dispose();
                }
            })
            .then();
    }

    private Disposable.Composite subscribeModule(Module module){  // TODO: no more subscribe
        List<Method> methods = ReflectionUtil.getDeclaredMethodsByName(module.getClass(),"on");
        Disposable.Composite subscribers = Disposables.composite();
        // automatically add command handling
        if (module.getCommands().size() > 0)
            subscribers.add(module.subscribeTo(eventDispatcher,"HandleCommand"));
        for (Method m : methods){
            subscribers.add(
                module.subscribeTo(eventDispatcher,m.getParameterTypes()[0].getSimpleName())
            );
        }
        return subscribers;
    }

    ConcurrentHashMap<
        Flux<? extends Event>,
        ConcurrentHashMap<
            String,
            Function<? extends Event,Mono<Void>>
            >
        > thing;

    public void registerBotEvent1(BotModule module){
        Log.debugFormat("| Enabling bot module %s...", module.getName());
        activeBotModules.computeIfAbsent(module.getName(), $ -> subscribeModule(module));

        List<Method> methods = ReflectionUtil.getDeclaredMethodsByName(module.getClass(),"on");
        if (module.getCommands().size() > 0);



    }

    private Disposable.Composite subscribeModule1(Module module){  // TODO: no more subscribe
        List<Method> methods = ReflectionUtil.getDeclaredMethodsByName(module.getClass(),"on");
        Disposable.Composite subscribers = Disposables.composite();
        // automatically add command handling
        if (module.getCommands().size() > 0)
            subscribers.add(module.subscribeTo(eventDispatcher,"HandleCommand"));
        for (Method m : methods){
            thing.computeIfAbsent(eventDispatcher.on((Class<? extends Event>)m.getParameterTypes()[0]), e -> new ConcurrentHashMap<>());
            subscribers.add(
                module.subscribeTo(eventDispatcher,m.getParameterTypes()[0].getSimpleName())
            );
        }
        return subscribers;
    }




}
