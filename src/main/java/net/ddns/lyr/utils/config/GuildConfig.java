package net.ddns.lyr.utils.config;


import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.utils.Log;

import java.util.HashMap;

public class GuildConfig {

    private static final String GUILDS_FOLDER = BotConfig.ROOT_FILE_FOLDER + "/guilds";
    private static final String GUILD_CONFIG_FILENAME = "guild.json";

    public static HashMap<Snowflake, GuildSetting> readAllConfig(){
        HashMap<Snowflake,GuildSetting> map = new HashMap<>();

        String[] dirs = FileUtil.listDirs(GUILDS_FOLDER);
        if (dirs == null) {
            Log.log("> No 'guild' folder found. Creating one.");
            FileUtil.createDir(GUILDS_FOLDER);
            return map;
        }
        if (dirs.length == 0) return map;
        for (String dir : dirs){
            Snowflake guildId;
            try {
                guildId = Snowflake.of(dir);
            } catch(NumberFormatException e){  // Not a guild folder?
                continue;
            }
            final String configFile = String.format("%s/%s/%s",GUILDS_FOLDER,dir,GUILD_CONFIG_FILENAME);
            GuildSetting guildSetting = FileUtil.readFile(configFile, GuildSetting.class);
            if (guildSetting == null) {  // File doesn't exist.
                Log.logf("> Creating new config for guild %s", dir);
                FileUtil.createDir(GUILDS_FOLDER + "/" + dir);
                guildSetting = new GuildSetting();
                if (!FileUtil.createFile(configFile,guildSetting))
                    Log.logfError(">>> Cannot create config file for guild %s.", dir);
            }
            map.put(guildId,guildSetting);
        }
        return map;
    }

}
