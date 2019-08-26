package lyr.testbot.objects.annotstore;

import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.templates.Command;

import java.lang.annotation.Annotation;

public class CommandInfoObj implements CommandInfo {
    private final String name;
    private final String[] aliases;
    private final CommandType type;
    private final String desc;
    private final String usage;
    private final int minArgs;

    public CommandInfoObj(final Class<? extends Command> command){
        final CommandInfo annotation = command.getAnnotation(CommandInfo.class);
        name = annotation.name().equals("\0") ? command.getSimpleName().toLowerCase() : annotation.name();
        aliases = annotation.aliases();
        type = annotation.type();
        desc = annotation.desc();
        usage = annotation.usage().equals("\0") ? name : annotation.usage();
        minArgs = annotation.minArgs();
    }

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
    }

    public CommandType type() {
        return type;
    }

    public String desc() {
        return desc;
    }

    public String usage() {
        return usage;
    }

    public int minArgs() {
        return minArgs;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
