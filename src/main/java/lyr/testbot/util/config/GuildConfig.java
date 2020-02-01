package lyr.testbot.util.config;


import discord4j.core.object.util.Snowflake;
import lyr.testbot.main.Main;
import lyr.testbot.modules.GuildModules;
import lyr.testbot.templates.GuildModule;
import lyr.testbot.util.FuncUtil;
import lyr.testbot.util.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GuildConfig {

    private static final String GUILDS_FOLDER = BotConfig.ROOT_FILE_FOLDER + "/guilds";
    private static final String GUILD_CONFIG_FILENAME = "guild.json";
    private static final String MODULE_CONFIG_DIR = "modules";

    /*
    public static HashMap<Snowflake, GuildSetting> readAllConfig(){
        HashMap<Snowflake,GuildSetting> map = new HashMap<>();

        String[] dirs = FileUtil.listDirs(GUILDS_FOLDER);
        if (dirs == null) {
            Log.info("> No 'guilds' folder found. Creating one.");
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
                Log.infoFormat("> Creating new config for guild %s...", dir);
                FileUtil.createDir(guildDir);
                guildSetting = new GuildSetting(guildId);
                if (!FileUtil.createFile(configFile,guildSetting))
                    Log.errorFormat(">>> Cannot create config file for guild %s.", dir);
            }
            guildSetting.setModulesSettings(readModulesSettings(guildDir));
            map.put(guildId,guildSetting);
        }
        return map;
    }
     */

    public static Mono<HashMap<Snowflake, GuildSetting>> readAllConfig(){
        ConcurrentHashMap<Snowflake,GuildSetting> map = new ConcurrentHashMap<>();

        Function<Snowflake,Mono<Void>> read = guildId -> {
            final String guildDir = String.format("%s/%s",GUILDS_FOLDER,guildId.asString());
            final String configFile = String.format("%s/%s",guildDir,GUILD_CONFIG_FILENAME);
            return FileUtil.readFile(configFile, GuildSetting.class)
                .switchIfEmpty(Mono.fromRunnable(() ->
                        Log.infoFormat("> Creating new config for guild %s...", guildId.asString())
                    )
                    .then(FileUtil.createDir(guildDir))
                    .map($ -> new GuildSetting(guildId))
                    .filterWhen(gs -> FileUtil.createFile(configFile,gs).thenReturn(true).onErrorReturn(false))
                    .doOnError(err -> Log.errorFormat(">>> Cannot create config file for guild %s.", guildId.asString()))
                )
                .zipWith(readModulesSettings(guildDir), (gs, ms) ->
                    Mono.fromCallable(() -> map.put(guildId,gs))
                        .then(gs.setModulesSettings(ms))                 // TODO: WHY NEED TO REWRITE ?!?!??!?!??!?
                )
                .flatMap(FuncUtil::it);
        };

        return FileUtil.isDirectory(GUILDS_FOLDER)
            .filter(exists -> !exists)   // if not exists,
            .doOnNext($ -> Log.info("> No 'guilds' folder found. Creating one."))
            .flatMap($ -> FileUtil.createDir(GUILDS_FOLDER))
            .map($ -> map)
            .switchIfEmpty(FileUtil.listDirs(GUILDS_FOLDER)    // else
                //.parallel()
                //.runOn(Schedulers.parallel())
                .map(Snowflake::of)
                .doOnError($ -> Log.warnFormat(">> Folder %s isn't a valid guild folder!"))
                .flatMap(read)
                //.sequential()
                .then()
                .thenReturn(map)
            )
            .map(HashMap::new);
    }


    //   ModuleName, <SettingKey, SettingValue>
    /*
    @SuppressWarnings("unchecked")  // Gson casts to HashMap<String,String> just fine
    public static HashMap<String,HashMap<String,String>> readModulesSettings(String guildDir){
        HashMap<String,HashMap<String,String>> map = new HashMap<>();
        Map<String, GuildModule> availableGuildModules;
        if (Main.client.availableGuildModules != null)
            availableGuildModules = Main.client.availableGuildModules.get();
        else
            availableGuildModules = new HashMap<>();

        String[] files = FileUtil.listFiles(guildDir + "/" + MODULE_CONFIG_DIR);
        if (files == null){  // modules dir doesn't exist
            Log.infoFormat("> Creating modules config dir at %s...", guildDir);
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
    */


    //   ModuleName, <SettingKey, SettingValue>
    @SuppressWarnings("unchecked")  // Gson casts to HashMap<String,String> just fine
    public static Mono<HashMap<String,HashMap<String,String>>> readModulesSettings(String guildDir){
        ConcurrentHashMap<String,HashMap<String,String>> map = new ConcurrentHashMap<>();

        Mono<Map<String, GuildModule>> agm = Mono.justOrEmpty(Main.client.availableGuildModules)
            .map(GuildModules::get)
            .defaultIfEmpty(new HashMap<>());

        Function<String,Mono<HashMap<String,String>>> read = file -> {
            String moduleName = file.replaceAll(".*?([^\\\\/]+).json$","$1");
            return agm
                .filter(gm -> gm.containsKey(moduleName))    // if contains
                .flatMap(gm ->
                    FileUtil.readFile(String.format("%s/%s/%s",guildDir,MODULE_CONFIG_DIR,file), HashMap.class)
                )
                .map(settings ->
                    (HashMap<String,String>) settings)
                .doOnNext(settings ->
                    map.put(moduleName
                        ,settings
                    ));
        };

        return FileUtil.isDirectory(guildDir + "/" + MODULE_CONFIG_DIR)
            .filter(FuncUtil::negate)   // if not exists,
            .doOnNext($ -> Log.infoFormat("> Creating modules config dir at %s...", guildDir))
            .flatMap($ -> FileUtil.createDir(guildDir + "/" + MODULE_CONFIG_DIR))
            .map($ -> map)
            .switchIfEmpty(    // else
                FileUtil.listFiles(guildDir + "/" + MODULE_CONFIG_DIR)
                    //.parallel()
                    //.runOn(Schedulers.parallel())
                    .flatMap(read)
                    //.sequential()
                    .then()
                    .thenReturn(map)
            )
            .map(HashMap::new)
        ;
    }

    //   ModuleName, <SettingKey, SettingValue>
    public static Mono<Void> updateModulesSettings(HashMap<String,HashMap<String,String>> settings, String guildId){
        final String dir = String.format("%s/%s/%s", GUILDS_FOLDER, guildId, MODULE_CONFIG_DIR);
        return FileUtil.createDir(dir)
            .thenMany(Flux.fromIterable(settings.entrySet()))
            .flatMap(entry ->
                FileUtil.updateFile(String.format("%s/%s.json",dir,entry.getKey()), entry.getValue())
                    .doOnNext($ -> Log.debugFormat("%s config for guild %s updated.", entry.getKey(), guildId))
                    .doOnError($ -> Log.errorFormat(">>> %s config update for guild %s failed!", entry.getKey(), guildId))
            )
            .then();
    }

    public static Mono<Void> updateModuleSettings(String moduleName, HashMap<String,String> settings, String guildId){
        final String dir = String.format("%s/%s/%s", GUILDS_FOLDER, guildId, MODULE_CONFIG_DIR);
        return FileUtil.createDir(dir)
            .then(FileUtil.updateFile(String.format("%s/%s.json",dir,moduleName), settings))
            .doOnNext($ -> Log.debugFormat("%s config for guild %s updated.", moduleName, guildId))
            .doOnError($ -> Log.errorFormat(">>> %s config update for guild %s failed!", moduleName, guildId))
            .then();
    }

    public static Mono<Void> updateGuildSettings(GuildSetting setting){
        final String dir = String.format("%s/%s", GUILDS_FOLDER, setting.guildId.asString());
        return FileUtil.createDir(dir)
            .then(FileUtil.updateFile(String.format("%s/%s",dir,GUILD_CONFIG_FILENAME), setting))
            .doOnNext($ -> Log.debugFormat("Config for guild %s updated.", setting.guildId.asString()))
            .doOnError($ -> Log.errorFormat(">>> Config update for guild %s failed!", setting.guildId.asString()))
            .then();
    }

}
