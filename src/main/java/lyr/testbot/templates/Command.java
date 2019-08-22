package lyr.testbot.templates;

import discord4j.core.object.util.Snowflake;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.main.Main;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.util.config.GuildSetting;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public abstract class Command {

//    public abstract String getName();
//    public abstract String[] getAliases();
//    public abstract CommandType getType();
//    public abstract String getDesc();
//    public abstract String getUsage();
//    public abstract int getMinArgs();

    protected CommandInfo commandInfo = this.getClass().getAnnotation(CommandInfo.class);

    public String getName(){
        return commandInfo.name();
    }

    public List<String> getAliases(){
        return Arrays.asList(commandInfo.aliases());
    }

    public CommandType getType(){
        return commandInfo.type();
    }

    public String getDesc(){
        return commandInfo.desc();
    }

    public String getUsage(){
        return commandInfo.usage();
    }

    public int getMinArgs(){
        return commandInfo.minArgs();
    }

    public String getFormattedUsage(){
        return getUsage().replaceAll("([(\\[])","`$1")
            .replaceAll("([)\\]])","$1`");
    }

    protected static ClientObject getClient(){
        return Main.client;
    }
    protected static GuildSetting getGuildSettingsFor(Snowflake guildId){
        return getClient().getGuildSettings().get(guildId);
    }
    protected static String getPrefix(){
        return getClient().getBotConfig().getPrefix();
    }

    public abstract Mono<Reply> execute(CommandObject command);

}
