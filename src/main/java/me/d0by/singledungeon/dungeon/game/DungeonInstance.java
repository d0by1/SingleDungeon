package me.d0by.singledungeon.dungeon.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.d0by.singledungeon.Config;
import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.dungeon.Dungeon;
import me.d0by.singledungeon.dungeon.DungeonWave;
import me.d0by.singledungeon.profile.Profile;
import me.d0by.singledungeon.utils.Common;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents an actual dungeon run. It is responsible for managing
 * the game loop, waves, players, etc.
 *
 * @author d0by
 * @since 1.0.0
 */
@Getter
@Setter
public class DungeonInstance {

    private final @NotNull Dungeon dungeon;
    private final @NotNull Player player;
    private final @NotNull AtomicInteger time;
    private final @NotNull AtomicInteger totalTime;
    private final @NotNull Set<Integer> entityIds;
    private DungeonStatus status;
    private DungeonResult result;
    private int kills;

    @Setter(AccessLevel.NONE)
    private int currentWave;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int taskId;

    public DungeonInstance(@NotNull Dungeon dungeon, @NotNull Player player) {
        this.dungeon = dungeon;
        this.player = player;
        this.currentWave = 1;
        this.time = new AtomicInteger(dungeon.prepareTime());
        this.totalTime = new AtomicInteger(0);
        this.entityIds = new HashSet<>();
    }

    @SuppressWarnings("deprecation")
    public void setStatus(@NotNull DungeonStatus status) {
        this.status = status;

        switch (status) {
            case STARTING -> {
                // Teleport to spawn
                player.teleport(dungeon.spawnLocation());
                player.setGameMode(GameMode.SURVIVAL);

                // Send starting message
                for (String line : Config.DUNGEON_STARTING_MESSAGE) {
                    Common.tell(player, line);
                }

                // Send starting title
                player.sendTitle(
                        Common.formatString(player, Config.DUNGEON_STARTING_TITLE),
                        Common.formatString(player, Config.DUNGEON_STARTING_SUBTITLE),
                        20, 60, 20
                );

                // Play sounds
                player.playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_AMBIENT, 1, 1);

                // Start countdown
                taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SingleDungeon.getInstance(), () -> {
                    int time = this.time.get();
                    if (time % 60 == 0 || time == 30 || time == 10 || time <= 5) {
                        if (time == 0) {
                            setStatus(DungeonStatus.RUNNING);
                        }
                        Common.tell(player, Config.DUNGEON_STARTING_COUNTDOWN);
                    }
                    this.time.decrementAndGet();
                }, 0, 20);
            }
            case RUNNING -> {
                Bukkit.getScheduler().cancelTask(taskId);

                // Send running message
                for (String line : Config.DUNGEON_RUNNING_MESSAGE) {
                    Common.tell(player, line);
                }

                // Send running title
                player.sendTitle(
                        Common.formatString(player, Config.DUNGEON_RUNNING_TITLE),
                        Common.formatString(player, Config.DUNGEON_RUNNING_SUBTITLE),
                        20, 60, 20
                );

                // Play sounds
                player.playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_AMBIENT, 1, 1);

                // Start wave 1
                Bukkit.getScheduler().runTask(SingleDungeon.getInstance(), this::startWave);

                // Start game loop - spawning more waves, time limit?, etc.
                taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SingleDungeon.getInstance(), () -> {
                    int time = this.time.get();
                    if (time == 0) {
                        if (currentWave == dungeon.waves().size()) {
                            setResult(DungeonResult.OUT_OF_TIME);
                            setStatus(DungeonStatus.FINISHED);
                        } else {
                            currentWave++;
                            Bukkit.getScheduler().runTask(SingleDungeon.getInstance(), this::startWave);
                        }
                    }

                    // Update action bar
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(Common.formatString(player, Config.DUNGEON_ACTION_BAR))
                    );

                    this.time.decrementAndGet();
                    totalTime.incrementAndGet();
                }, 0, 20);
            }
            case FINISHED -> {
                Bukkit.getScheduler().cancelTask(taskId);
                Common.heal(player);

                // Teleport to spawn
                if (Config.SPAWN != null) {
                    player.teleport(Config.SPAWN);
                }

                switch (result) {
                    case WIN -> {
                        // Send finished message
                        for (String line : Config.DUNGEON_FINISHED_WIN_MESSAGE) {
                            Common.tell(player, line);
                        }
                        // Send finished title
                        player.sendTitle(
                                Common.formatString(player, Config.DUNGEON_FINISHED_WIN_TITLE),
                                Common.formatString(player, Config.DUNGEON_FINISHED_WIN_SUBTITLE),
                                20, 60, 20
                        );
                        // Play sounds
                        player.playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_DEATH, 1, 1);
                    }
                    case DEATH -> {
                        // Send finished message
                        for (String line : Config.DUNGEON_FINISHED_DEATH_MESSAGE) {
                            Common.tell(player, line);
                        }
                        // Send finished title
                        player.sendTitle(
                                Common.formatString(player, Config.DUNGEON_FINISHED_DEATH_TITLE),
                                Common.formatString(player, Config.DUNGEON_FINISHED_DEATH_SUBTITLE),
                                20, 60, 20
                        );
                        // Play sounds
                        player.playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_DEATH, 1, 1);
                    }
                    case OUT_OF_TIME -> {
                        // Send finished message
                        for (String line : Config.DUNGEON_FINISHED_OUT_OF_TIME_MESSAGE) {
                            Common.tell(player, line);
                        }
                        // Send finished title
                        player.sendTitle(
                                Common.formatString(player, Config.DUNGEON_FINISHED_OUT_OF_TIME_TITLE),
                                Common.formatString(player, Config.DUNGEON_FINISHED_OUT_OF_TIME_SUBTITLE),
                                20, 60, 20
                        );
                        // Play sounds
                        player.playSound(player.getLocation(), Sound.ENTITY_VINDICATOR_DEATH, 1, 1);
                    }
                    default -> {
                        // Send finished message
                        for (String line : Config.DUNGEON_FINISHED_CANCELLED_MESSAGE) {
                            Common.tell(player, line);
                        }
                    }
                }

                // Save stats
                Profile profile = SingleDungeon.getInstance().getProfileRegistry().getProfile(player);
                if (profile != null) {
                    profile.setGames(profile.getGames() + 1);
                    if (result == DungeonResult.WIN) {
                        profile.setWins(profile.getWins() + 1);
                    }
                    profile.setKills(profile.getKills() + kills);
                    profile.setCurrentDungeon(null);
                    profile.save();
                }
            }
        }
    }

    public void killMonster(int eid) {
        kills++;
        entityIds.remove(eid);
        if (entityIds.isEmpty()) {
            if (currentWave == dungeon.waves().size()) {
                setResult(DungeonResult.WIN);
                setStatus(DungeonStatus.FINISHED);
            } else {
                currentWave++;
                Bukkit.getScheduler().runTask(SingleDungeon.getInstance(), this::startWave);
            }
        }
    }

    private void startWave() {
        DungeonWave wave = dungeon.waves().get(currentWave - 1);
        Set<Integer> spawnedEntityIds = wave.spawn(player, dungeon.waveSpawnLocation());
        entityIds.addAll(spawnedEntityIds);
        time.set(wave.duration());

        Common.tell(player, Config.DUNGEON_WAVE_START);
    }

}
