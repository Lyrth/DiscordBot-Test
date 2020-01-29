package lyr.testbot.util.config;

import com.google.gson.annotations.SerializedName;
import lyr.testbot.util.Log;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class BotConfig {

    public static final String ROOT_FILE_FOLDER = "DiscordBot-Test";

    private static final String CONFIG_FILE = ROOT_FILE_FOLDER + "/config.json";

    @SerializedName("Bot Token")
    private String token;

    @SerializedName("Bot (Default) Prefix")
    private String prefix;

    @SerializedName("Bot Settings")
    private HashMap<String,String> botSettings = new HashMap<>();

    private BotConfig(String token, String prefix){
        this.token  = token;
        this.prefix = prefix;
    }

    public static Mono<BotConfig> readConfig(){
        return FileUtil.readFileM(CONFIG_FILE,BotConfig.class)
            .switchIfEmpty(Mono.fromRunnable(() -> Log.info("> No config detected, creating one."))
                .then(FileUtil.createDir(ROOT_FILE_FOLDER))
                .thenReturn(new BotConfig("",";"))
                .filterWhen(cfg -> FileUtil.createFileM(CONFIG_FILE,cfg).thenReturn(true).onErrorReturn(false))
                .doOnError($ -> Log.errorFormat(">>> Cannot create config file %s.", CONFIG_FILE))
            )
            .doOnNext(cfg -> {
                if (cfg.getToken().isEmpty())
                    Log.errorFormat(">>> Please update %s to include the bot token.",CONFIG_FILE);
            });
    }

    public Mono<Void> updateConfig(){
        return FileUtil.updateFileM(CONFIG_FILE,this)
            .doOnNext($ -> Log.info("> Bot config updated."))
            .doOnError($ -> Log.error(">>> Bot config update error!"))
            .then();
    }

    public String getToken(){
        return token;
    }
    public String getPrefix(){
        return prefix;
    }

    public HashMap<String, String> getBotSettings() {
        return botSettings;
    }

    public String getBotSetting(String key){
        return botSettings.getOrDefault(key,"");
    }

    public void setBotSetting(String key, String value) {
        botSettings.put(key,value);
    }
}
