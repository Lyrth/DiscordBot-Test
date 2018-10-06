package net.ddns.lyr.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.ddns.lyr.utils.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BotConfig {

    private static final String CONFIG_FILE = "config.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SerializedName("Bot Token")
    private String token;

    @SerializedName("Bot (Default) Prefix")
    private String prefix;

    @SerializedName("Owner ID")
    private String botOwner;

    private BotConfig(String token, String prefix){
        this.token  = token;
        this.prefix = prefix;
    }

    static BotConfig readConfig(){
        try {
            String json = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            Log.logf("> Reading config from %s...",CONFIG_FILE);
            BotConfig config = gson.fromJson(json,BotConfig.class);
            if (config.getToken().isEmpty())
                Log.logfError(">>> Please update %s to include the bot token.",CONFIG_FILE);
            return config;
        } catch(IOException e) {
            Log.log("> No config detected, creating one.");
            BotConfig config = new BotConfig("","!");
            String json = gson.toJson(config);
            try(PrintStream ps = new PrintStream(CONFIG_FILE)) {
                ps.print(json);
                Log.logfError(">>> Please update %s to include the bot token.",CONFIG_FILE);
            } catch(FileNotFoundException ee){
                Log.logfError(">>> Cannot create config file %s.",CONFIG_FILE);
            }
            return config;
        }

    }

    void updateConfig(){
        File current = new File(CONFIG_FILE);
        File backup  = new File(CONFIG_FILE + ".bak");
        if (backup.exists()) if (!backup.delete()) Log.logWarn(">> Cannot delete backup config.");
        else if (!current.renameTo(backup)) Log.logWarn(">> Cannot rename config. Overwriting.");
        if (current.exists() && !current.delete()) {
            Log.logError(">>> Cannot modify config.");
            return;
        }
        String json = gson.toJson(this);
        try(PrintStream ps = new PrintStream(CONFIG_FILE)) {
            ps.print(json);
        } catch(FileNotFoundException ee){
            Log.logfError(">>> Cannot create config file %s.",CONFIG_FILE);
        }
        Log.log("> Config updated.");
    }

    String getToken(){
        return token;
    }
    public String getPrefix(){
        return prefix;
    }
    public String getBotOwner(){
        return botOwner;
    }

}
