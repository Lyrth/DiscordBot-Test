package net.ddns.lyr.templates;

import net.ddns.lyr.enums.CommandType;
import net.ddns.lyr.objects.CommandObject;
import reactor.core.publisher.Mono;

public abstract class Command {


    public abstract String getName();
    protected abstract CommandType getType();
    protected abstract String getDesc();
    protected abstract String getUsage();
    protected abstract int getNumArgs();

    public abstract Mono<String> execute(CommandObject command);

}
