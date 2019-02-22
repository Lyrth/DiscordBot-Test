package lyr.testbot.templates;

import discord4j.core.object.util.Snowflake;
import lyr.testbot.enums.CommandType;
import lyr.testbot.main.Main;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.util.config.GuildSetting;
import reactor.core.publisher.Mono;

public abstract class Command {

    public abstract String getName();
    protected abstract CommandType getType();
    protected abstract String getDesc();
    protected abstract String getUsage();
    protected abstract int getNumArgs();

    public abstract Mono<Reply> execute(CommandObject command);

    protected ClientObject getClient(){
        return Main.client;
    }
    protected GuildSetting getGuildSettingsFor(Snowflake guildId){
        return getClient().getGuildSettings().get(guildId);
    }

}
