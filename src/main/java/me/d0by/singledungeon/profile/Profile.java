package me.d0by.singledungeon.profile;

import lombok.Getter;
import lombok.Setter;
import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.dungeon.game.DungeonInstance;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a player profile. It is used to hold player stats.
 */
@Getter
@Setter
public class Profile {

    private static final SingleDungeon PLUGIN = SingleDungeon.getInstance();

    private final @NotNull String name;
    private DungeonInstance currentDungeon;
    private int games = 0;
    private int wins = 0;
    private int kills = 0;

    public Profile(@NotNull String name) {
        this.name = name;
        this.currentDungeon = null;
        this.load();
    }

    public void load() {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> PLUGIN.getDatabase().loadProfile(this));
    }

    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> PLUGIN.getDatabase().saveProfile(this));
    }

}
