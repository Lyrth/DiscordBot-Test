package net.ddns.lyr.utils.config;


import discord4j.core.object.util.Snowflake;
import net.ddns.lyr.main.Main;
import net.ddns.lyr.templates.GuildModule;
import net.ddns.lyr.utils.Log;

import java.util.HashMap;
import java.util.Map;

public class GuildConfig {

    private static final String GUILDS_FOLDER = BotConfig.ROOT_FILE_FOLDER + "/guilds";
    private static final String GUILD_CONFIG_FILENAME = "guild.json";
    private static final String MODULE_CONFIG_DIR = "modules";

    public static HashMap<Snowflake, GuildSetting> readAllConfig(){
        HashMap<Snowflake,GuildSetting> map = new HashMap<>();

        String[] dirs = FileUtil.listDirs(GUILDS_FOLDER);
        if (dirs == null) {
            Log.log("> No 'guilds' folder found. Creating one.");
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
            final String guildDir = String.format("%s/%s",GUILDS_FOLDER,dir);
            final String configFile = String.format("%s/%s",guildDir,GUILD_CONFIG_FILENAME);
            GuildSetting guildSetting = FileUtil.readFile(configFile, GuildSetting.class);
            if (guildSetting == null) {  // File doesn't exist.
                Log.logf("> Creating new config for guild %s...", dir);
                FileUtil.createDir(guildDir);
                guildSetting = new GuildSetting(guildId);
                if (!FileUtil.createFile(configFile,guildSetting))
                    Log.logfError(">>> Cannot create config file for guild %s.", dir);
            }
            guildSetting.setModulesSettings(readModulesSettings(guildDir));
            map.put(guildId,guildSetting);
        }
        return map;
    }

    //   ModuleName, <SettingKey, SettingValue>
    @SuppressWarnings("unchecked")  // Gson casts to HashMap<String,String> just fine
    public static HashMap<String,HashMap<String,String>> readModulesSettings(String guildDir){
        HashMap<String,HashMap<String,String>> map = new HashMap<>();
        Map<String, GuildModule> availableGuildModules = Main.client.availableGuildModules;

        String[] files = FileUtil.listFiles(guildDir + "/" + MODULE_CONFIG_DIR);
        if (files == null){  // modules dir doesn't exist
            Log.logf("> Creating modules config dir at %s...", guildDir);
            FileUtil.createDir(guildDir + "/" + MODULE_CONFIG_DIR);
            return map;
        }
        if (files.length == 0) return map;  // no configs inside
        for (String file : files){
            String moduleName = file.replaceAll(".*?([^\\\\/]+).json$","$1");
            if (!availableGuildModules.containsKey(moduleName)) continue;
            map.put(moduleName, (HashMap<String,String>)
                FileUtil.readFile(String.format("%s/%s/%s",guildDir,MODULE_CONFIG_DIR,file), HashMap.class));
            // Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            // map.put(moduleName, FileUtil.readFile(String.format("%s%s/%s",guildDir,MODULE_CONFIG_DIR,file),type));
        }
        return map;
    }

    //   ModuleName, <SettingKey, SettingValue>
    public static void updateModulesSettings(HashMap<String,HashMap<String,String>> settings, String guildId){
        final String dir = String.format("%s/%s/%s", GUILDS_FOLDER, guildId, MODULE_CONFIG_DIR);
        FileUtil.createDir(dir);  // make sure dir exists
        settings.forEach((moduleName,map) -> {
            int err = FileUtil.updateFile(String.format("%s/%s.json",dir,moduleName), map);
            if ((err & 1) > 0) Log.logfWarn(">> Cannot delete backup %s settings file for %s.",moduleName,guildId);
            if ((err & 2) > 0) Log.logWarn(">> Cannot rename settings. Overwriting.");
            if ((err & 4) > 0) Log.logError(">>> Cannot modify settings.");
            if ((err & 8) > 0) Log.logfError(">>> Cannot create settings file %s.json.", moduleName);
            if ((err & 12) > 0) return;        // Error
            Log.logfDebug("> %s config for guild %s updated.", moduleName, guildId);
        });
    }

    public static void updateModuleSettings(String moduleName, HashMap<String,String> settings, String guildId){
        final String dir = String.format("%s/%s/%s", GUILDS_FOLDER, guildId, MODULE_CONFIG_DIR);
        FileUtil.createDir(dir);  // make sure dir exists
        int err = FileUtil.updateFile(String.format("%s/%s.json",dir,moduleName), settings);
        if ((err & 1) > 0) Log.logfWarn(">> Cannot delete backup %s settings file for %s.",moduleName,guildId);
        if ((err & 2) > 0) Log.logWarn(">> Cannot rename settings. Overwriting.");
        if ((err & 4) > 0) Log.logError(">>> Cannot modify settings.");
        if ((err & 8) > 0) Log.logfError(">>> Cannot create settings file %s.json.", moduleName);
        if ((err & 12) > 0) return;        // Error
        Log.logfDebug("> %s config for guild %s updated.", moduleName, guildId);
        ;
    }

    public static void updateGuildSettings(GuildSetting setting){
        final String dir = String.format("%s/%s", GUILDS_FOLDER, setting.guildId.asString());
        FileUtil.createDir(dir);  // make sure dir exists
        int err = FileUtil.updateFile(String.format("%s/%s",dir,GUILD_CONFIG_FILENAME), setting);
        if ((err&1) > 0) Log.logfWarn(">> Cannot delete backup config for guild %s.", setting.guildId.asString());
        if ((err&2) > 0) Log.logfWarn(">> Cannot rename config for guild %s. Overwriting.", setting.guildId.asString());
        if ((err&4) > 0) Log.logError(">>> Cannot modify config.");
        if ((err&8) > 0) Log.logfError(">>> Cannot create config file for guild %s.", setting.guildId.asString());
        if ((err&12)> 0) return;        // Error
        Log.logfDebug("> Config for %s updated.", setting.guildId.asString());
    }

}
