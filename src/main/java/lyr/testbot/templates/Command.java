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
    public abstract CommandType getType();
    public abstract String getDesc();
    public abstract String getUsage();
    public abstract int getNumArgs();

    public String getFormattedUsage(){
        return getUsage().replaceAll("([(\\[])","`$1")
            .replaceAll("([)\\]])","$1`");
    }

    public abstract Mono<Reply> execute(CommandObject command);

    protected ClientObject getClient(){
        return Main.client;
    }
    protected GuildSetting getGuildSettingsFor(Snowflake guildId){
        return getClient().getGuildSettings().get(guildId);
    }
    protected String getPrefix(){
        return getClient().getBotConfig().getPrefix();
    }

}
