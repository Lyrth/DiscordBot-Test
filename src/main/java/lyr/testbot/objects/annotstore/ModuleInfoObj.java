package lyr.testbot.objects.annotstore;

import lyr.testbot.annotations.ModuleInfo;
import lyr.testbot.templates.Command;
import lyr.testbot.templates.Module;

import java.lang.annotation.Annotation;

public class ModuleInfoObj implements ModuleInfo {
    private final String name;
    private final String[] aliases;
    private final String desc;
    private final Class<? extends Command>[] commands;
    private final boolean essential;

    public ModuleInfoObj(final Class<? extends Module> module){
        final ModuleInfo annotation = module.getAnnotation(ModuleInfo.class);
        name = annotation.name().equals("\0") ? module.getSimpleName() : annotation.name();
        aliases = annotation.aliases();
        desc = annotation.desc();
        commands = annotation.commands();
        essential = annotation.essential();
    }

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
    }

    public String desc() {
        return desc;
    }

    public Class<? extends Command>[] commands() {
        return commands;
    }

    public boolean essential() {
        return essential;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
