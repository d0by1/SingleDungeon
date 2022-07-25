package me.d0by.singledungeon.dungeon;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class represents a dungeon.
 *
 * @param name              The name of the dungeon.
 * @param spawnLocation     The location to spawn at.
 * @param waveSpawnLocation The location to spawn the waves at.
 * @param waves             The waves of the dungeon.
 * @param prepareTime       The time to prepare for the first wave.
 */
public record Dungeon(@NotNull String name,
                      @NotNull Location spawnLocation,
                      @NotNull Location waveSpawnLocation,
                      @NotNull List<DungeonWave> waves,
                      int prepareTime) {

}
