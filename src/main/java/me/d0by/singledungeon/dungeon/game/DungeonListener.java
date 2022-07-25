package me.d0by.singledungeon.dungeon.game;

import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class DungeonListener implements Listener {

    private static final SingleDungeon PLUGIN = SingleDungeon.getInstance();

    private void cancelIfInDungeon(Player player, Cancellable e) {
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player);
        if (profile != null && profile.getCurrentDungeon() != null) {
            e.setCancelled(true);
        }
    }

    private void finishDungeon(Player player, DungeonResult result) {
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player);
        if (profile == null) {
            return;
        }

        DungeonInstance dungeon = profile.getCurrentDungeon();
        if (dungeon != null) {
            dungeon.setResult(result);
            dungeon.setStatus(DungeonStatus.FINISHED);
            profile.setCurrentDungeon(null);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        finishDungeon(e.getPlayer(), DungeonResult.CANCELLED);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        finishDungeon(e.getEntity(), DungeonResult.DEATH);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        int eid = e.getEntity().getEntityId();
        PLUGIN.getProfileRegistry().getProfiles().stream()
                .map(Profile::getCurrentDungeon)
                .filter(Objects::nonNull)
                .filter(d -> d.getEntityIds().contains(eid))
                .findFirst()
                .ifPresent((d) -> {
                    e.getDrops().clear();
                    e.setDroppedExp(0);

                    d.killMonster(eid);
                });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        cancelIfInDungeon(e.getPlayer(), e);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        cancelIfInDungeon(e.getPlayer(), e);
    }

}
