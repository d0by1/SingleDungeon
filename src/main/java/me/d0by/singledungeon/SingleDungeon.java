package me.d0by.singledungeon;

import lombok.Getter;
import me.d0by.singledungeon.commands.StartCommand;
import me.d0by.singledungeon.database.DB;
import me.d0by.singledungeon.database.MongoDB;
import me.d0by.singledungeon.dungeon.DungeonRegistry;
import me.d0by.singledungeon.dungeon.game.DungeonListener;
import me.d0by.singledungeon.nms.NMSProvider;
import me.d0by.singledungeon.profile.ProfileListener;
import me.d0by.singledungeon.profile.ProfileRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class of the plugin.
 * <p>
 * This plugin is a simple mini-game, that allows players to play solo dungeons
 * with waves of enemies.
 *
 * @author d0by
 * @version 1.0.0
 */
@Getter
public final class SingleDungeon extends JavaPlugin {

    @Getter
    private static SingleDungeon instance;
    private NMSProvider nmsProvider;
    private DB database;
    private DungeonRegistry dungeonRegistry;
    private ProfileRegistry profileRegistry;

    public SingleDungeon() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();

        this.nmsProvider = new NMSProvider();
        this.database = new MongoDB();
        this.dungeonRegistry = new DungeonRegistry();
        this.profileRegistry = new ProfileRegistry();

        Bukkit.getPluginManager().registerEvents(new ProfileListener(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonListener(), this);

        this.getCommand("start").setExecutor(new StartCommand());
    }

    @Override
    public void onDisable() {
        if (this.profileRegistry != null) {
            this.profileRegistry.shutdown();
        }
        if (this.dungeonRegistry != null) {
            this.dungeonRegistry.shutdown();
        }
        if (this.database != null) {
            this.database.disconnect();
        }
    }

    /**
     * Reload the configuration.
     */
    public void reload() {
        Config.reload();
    }

}
