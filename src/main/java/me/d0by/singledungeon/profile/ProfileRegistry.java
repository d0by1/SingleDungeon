package me.d0by.singledungeon.profile;

import me.d0by.singledungeon.dungeon.game.DungeonInstance;
import me.d0by.singledungeon.dungeon.game.DungeonResult;
import me.d0by.singledungeon.dungeon.game.DungeonStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This registry holds all player profiles.
 *
 * @author d0by
 * @since 1.0.0
 */
public class ProfileRegistry {

    private final @NotNull Map<String, Profile> profiles;

    /**
     * Create a new instance of {@link ProfileRegistry}. This constructor
     * also creates profiles for all online players, calling {@link #reload()}.
     */
    public ProfileRegistry() {
        this.profiles = new HashMap<>();
        this.reload();
    }

    /**
     * Reload this registry, adding profiles for players, that don't have one yet.
     */
    public void reload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!profiles.containsKey(player.getName())) {
                Profile profile = new Profile(player.getName());
                profile.load();

                register(profile);
            }
        }
    }

    /**
     * Shutdown this registry, ending all active dungeons and saving all profiles.
     */
    public void shutdown() {
        for (Profile profile : profiles.values()) {
            DungeonInstance dungeon = profile.getCurrentDungeon();
            if (dungeon != null) {
                dungeon.setResult(DungeonResult.CANCELLED);
                dungeon.setStatus(DungeonStatus.FINISHED);
                profile.setCurrentDungeon(null);
            }
            profile.save();
        }
        profiles.clear();
    }

    public void register(@NotNull Profile profile) {
        profiles.put(profile.getName(), profile);
    }

    @Nullable
    public Profile getProfile(@NotNull String name) {
        return profiles.get(name);
    }

    @Nullable
    public Profile getProfile(@NotNull Player player) {
        return profiles.get(player.getName());
    }

    public void removeProfile(@NotNull String name) {
        Profile profile = profiles.remove(name);
        DungeonInstance dungeon = profile.getCurrentDungeon();
        if (dungeon != null) {
            dungeon.setResult(DungeonResult.CANCELLED);
            dungeon.setStatus(DungeonStatus.FINISHED);
            profile.setCurrentDungeon(null);
        }
    }

    @NotNull
    public Collection<Profile> getProfiles() {
        return profiles.values();
    }

}
