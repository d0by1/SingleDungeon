package me.d0by.singledungeon.dungeon;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a dungeon wave. Each wave spawns monsters.
 *
 * @param name
 * @param monsters
 * @param duration
 */
public record DungeonWave(@NotNull String name,
                          @NotNull List<DungeonMonster> monsters,
                          int duration) {

    /**
     * Spawn all monsters from this wave for the given player.
     *
     * @param player   The player to spawn for.
     * @param location The location to spawn at.
     * @return The spawned entities' ids.
     */
    public Set<Integer> spawn(@NotNull Player player, @NotNull Location location) {
        return monsters.stream()
                .map(monster -> monster.spawn(player, location))
                .collect(Collectors.toSet());
    }

}
