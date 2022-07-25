package me.d0by.singledungeon;

import lombok.experimental.UtilityClass;
import me.d0by.singledungeon.utils.config.CFG;
import me.d0by.singledungeon.utils.config.ConfigValue;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * This class holds all configuration values from the 'config.yml' file.
 *
 * @author d0by
 * @since 1.0.0
 */
@UtilityClass
public final class Config {

    private static File file;

    public static void reload() {
        if (file == null) {
            file = new File(SingleDungeon.getInstance().getDataFolder(), "config.yml");
        }
        YamlConfiguration config = CFG.load(Config.class, file);
        if (config != null) {
            SPAWN = config.getLocation("spawn");
        }
    }

    // =========== VALUES =========== //

    @ConfigValue("mongodb.host")
    public static String MONGODB_HOST = "localhost";
    @ConfigValue("mongodb.port")
    public static int MONGODB_PORT = 27017;
    @ConfigValue("mongodb.database")
    public static String MONGODB_DATABASE = "singledungeon";
    @ConfigValue("mongodb.collection")
    public static String MONGODB_COLLECTION = "players";

    @ConfigValue("spawn")
    public static Location SPAWN = null;

    @ConfigValue("dungeon.action_bar")
    public static String DUNGEON_ACTION_BAR = "&7&nWave:&c {wave}&8/&4{waves} &8- &7&nTime left:&c {time_long} &8- &7&nKills:&c {kills}";

    // -- Messages

    @ConfigValue("messages.prefix")
    public static String PREFIX = "&4SingleDungeon&8 | &7";
    @ConfigValue("messages.no_perm")
    public static String NO_PERM = "{prefix}&cYou don't have permission to do that.";
    @ConfigValue("messages.usage")
    public static String USAGE = "{prefix}Usage: &c%s";
    @ConfigValue("messages.dungeon.start")
    public static String DUNGEON_START = "{prefix}Starting dungeon &c%s&7...";
    @ConfigValue("messages.dungeon.not_found")
    public static String DUNGEON_NOT_FOUND = "{prefix}Dungeon &c%s&7 not found.";
    @ConfigValue("messages.dungeon.wave_start")
    public static String DUNGEON_WAVE_START = "{prefix}Starting &c{wave}.&7 wave!";

    // -- Starting Dungeon

    @ConfigValue("messages.dungeon.starting.message")
    public static List<String> DUNGEON_STARTING_MESSAGE = List.of(
            "",
            "&4&lDUNGEON STARTING IN {time_seconds} SECONDS",
            "",
            "&7You have &c{time_long}&7 to prepare for battle!",
            ""
    );
    @ConfigValue("messages.dungeon.starting.title")
    public static String DUNGEON_STARTING_TITLE = "&4&lDungeon Starting";
    @ConfigValue("messages.dungeon.starting.subtitle")
    public static String DUNGEON_STARTING_SUBTITLE = "&7You have &c{time_long}&7 to prepare!";
    @ConfigValue("messages.dungeon.starting.countdown")
    public static String DUNGEON_STARTING_COUNTDOWN = "{prefix}&7Starting in &c{time_long}&7...";

    // -- Dungeon Running

    @ConfigValue("messages.dungeon.running")
    public static List<String> DUNGEON_RUNNING_MESSAGE = List.of(
            "{prefix}Dungeon &c{dungeon}&7 started!"
    );
    @ConfigValue("messages.dungeon.running.title")
    public static String DUNGEON_RUNNING_TITLE = "&4&lDungeon Started";
    @ConfigValue("messages.dungeon.running.subtitle")
    public static String DUNGEON_RUNNING_SUBTITLE = "&7Kill all the monsters!";

    // -- Dungeon Finished WIN

    @ConfigValue("messages.dungeon.finished.win.message")
    public static List<String> DUNGEON_FINISHED_WIN_MESSAGE = List.of(
            "",
            "&4&lDUNGEON FINISHED",
            "",
            "&7You killed all the monsters in time!",
            "",
            "&4Stats:",
            " &8- &7Time: &c{took}",
            " &8- &7Kills: &c{kills}",
            "",
            "&7Wanna play again? Use &c/start <dungeon>&7!",
            ""
    );
    @ConfigValue("messages.dungeon.finished.win.title")
    public static String DUNGEON_FINISHED_WIN_TITLE = "&4&lDungeon Finished";
    @ConfigValue("messages.dungeon.finished.win.subtitle")
    public static String DUNGEON_FINISHED_WIN_SUBTITLE = "&7You &akilled&7 all the monsters, good job!";

    // -- Dungeon Finished DEATH

    @ConfigValue("messages.dungeon.finished.death.message")
    public static List<String> DUNGEON_FINISHED_DEATH_MESSAGE = List.of(
            "",
            "&4&lDUNGEON FINISHED",
            "",
            "&7You were killed and eaten by monsters, try again!",
            "",
            "&7Wanna play again? Use &c/start <dungeon>&7!",
            ""
    );
    @ConfigValue("messages.dungeon.finished.death.title")
    public static String DUNGEON_FINISHED_DEATH_TITLE = "&4&lDungeon Finished";
    @ConfigValue("messages.dungeon.finished.death.subtitle")
    public static String DUNGEON_FINISHED_DEATH_SUBTITLE = "&7You lost, monsters ate you!";

    // -- Dungeon Finished OUT_OF_TIME

    @ConfigValue("messages.dungeon.finished.out_of_time.message")
    public static List<String> DUNGEON_FINISHED_OUT_OF_TIME_MESSAGE = List.of(
            "",
            "&4&lDUNGEON FINISHED",
            "",
            "&7You didn't manage to defeat all monsters in time, better luck next time!",
            "",
            "&7Wanna play again? Use &c/start <dungeon>&7!",
            ""
    );
    @ConfigValue("messages.dungeon.finished.out_of_time.title")
    public static String DUNGEON_FINISHED_OUT_OF_TIME_TITLE = "&4&lDungeon Finished";
    @ConfigValue("messages.dungeon.finished.out_of_time.subtitle")
    public static String DUNGEON_FINISHED_OUT_OF_TIME_SUBTITLE = "&7You ran out of time!";

    // -- Dungeon Finished CANCELLED

    @ConfigValue("messages.dungeon.finished.cancelled.message")
    public static List<String> DUNGEON_FINISHED_CANCELLED_MESSAGE = List.of(
            "",
            "&4&lDUNGEON FINISHED",
            "",
            "&7Your game has been cancelled, sorry!",
            "",
            "&7Wanna play again? Use &c/start <dungeon>&7!",
            ""
    );

}
