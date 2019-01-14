package net.ddns.lyr.utils.config;

import com.google.gson.annotations.SerializedName;
import net.ddns.lyr.utils.Log;

public class BotConfig {

    public static final String ROOT_FILE_FOLDER = "DiscordBot-Test";

    private static final String CONFIG_FILE = ROOT_FILE_FOLDER + "/config.json";

    @SerializedName("Bot Token")
    private String token;

    @SerializedName("Bot (Default) Prefix")
    private String prefix;

    @SerializedName("Owner ID")
    private String botOwner;

    private BotConfig(){}

    private BotConfig(String token, String prefix){
        this.token  = token;
        this.prefix = prefix;
    }

    public static BotConfig readConfig(){
        BotConfig config = FileUtil.readFile(CONFIG_FILE,BotConfig.class);
        if (config == null) {
            Log.log("> No config detected, creating one.");
            FileUtil.createDir(ROOT_FILE_FOLDER);
            config = new BotConfig("","!");
            if (!FileUtil.createFile(CONFIG_FILE,config)) {
                Log.logfError(">>> Cannot create config file %s.", CONFIG_FILE);
                return null;
            }
        }
        if (config.getToken().isEmpty())
            Log.logfError(">>> Please update %s to include the bot token.",CONFIG_FILE);
        return config;

    }

    public void updateConfig(){
        int err = FileUtil.updateFile(CONFIG_FILE,this);
        if ((err&1) > 0) Log.logWarn(">> Cannot delete backup config.");
        if ((err&2) > 0) Log.logWarn(">> Cannot rename config. Overwriting.");
        if ((err&4) > 0) Log.logError(">>> Cannot modify config.");
        if ((err&8) > 0) Log.logfError(">>> Cannot create config file %s.",CONFIG_FILE);
        if ((err&12)> 0) return;        // Error
        Log.log("> Config updated.");
    }

    public String getToken(){
        return token;
    }
    public String getPrefix(){
        return prefix;
    }
    public String getBotOwner(){
        return botOwner;
    }

}
