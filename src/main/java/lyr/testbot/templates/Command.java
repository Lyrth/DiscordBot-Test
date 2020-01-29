package lyr.testbot.templates;

import discord4j.core.object.util.Snowflake;
import lyr.testbot.annotations.CommandInfo;
import lyr.testbot.enums.CommandType;
import lyr.testbot.main.Main;
import lyr.testbot.objects.ClientObject;
import lyr.testbot.objects.CommandObject;
import lyr.testbot.objects.annotstore.CommandInfoObj;
import lyr.testbot.objects.builder.Reply;
import lyr.testbot.util.config.GuildSetting;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@CommandInfo(name = "__Command_Base__")
public abstract class Command {

    protected final CommandInfoObj commandInfo = new CommandInfoObj(this.getClass());

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

    public static Mono<String> getSetting(Snowflake guildId, String module, String key){
        return getGuildSettingsFor(guildId).getModuleSetting(module, key);
    }

    protected Mono<String> getSettingOrDefault(Snowflake guildId, String module, String key, String defaultValue){
        return getSetting(guildId, module, key).defaultIfEmpty(defaultValue);
    }

    protected Mono<Void> setSetting(Snowflake guildId, String module, String key, String value){
        return getGuildSettingsFor(guildId).setModuleSetting(module, key, value);
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
