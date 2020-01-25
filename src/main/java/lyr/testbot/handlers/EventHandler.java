package lyr.testbot.handlers;

import discord4j.core.event.EventDispatcher;
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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        activeBotModules.computeIfAbsent(module.getName(), name -> subscribeModule(module));
    }

    public void unregisterBotEvent(BotModule module){
        unregisterBotEvent(module.getName());
    }

    public void unregisterBotEvent(String moduleName){
        Log.debugFormat("Unregistering %s...", moduleName);
        activeBotModules.remove(moduleName).dispose();
    }


    public void updateGuildModules(GuildSetting setting){
        Map<String, GuildModule> availableGuildModules = Main.client.availableGuildModules.get();
        Snowflake guildId = setting.guildId;
        activeGuildModules.computeIfAbsent(guildId, (id) -> new HashMap<>());
        availableGuildModules.forEach((moduleName,module) -> {
            if (setting.enabledModules.contains(moduleName)){  // It should be enabled
                if (activeGuildModules.get(guildId).containsKey(moduleName))  // Already enabled
                    return;
                Log.debugFormat("| Enabling module %s...", moduleName);
                GuildModule guildModule = module.newInstance(setting);
                activeGuildModules.get(guildId).put(moduleName,subscribeModule(guildModule));
            } else { // Should be disabled.
                if (!activeGuildModules.get(guildId).containsKey(moduleName))  // Already disabled
                    return;
                Log.debugFormat("| Disabling module %s...", moduleName);
                activeGuildModules.get(guildId).remove(moduleName).dispose();
            }
        });
    }

    private Disposable.Composite subscribeModule(Module module){
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
    


}
