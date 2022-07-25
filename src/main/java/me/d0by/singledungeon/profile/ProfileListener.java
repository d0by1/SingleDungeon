package me.d0by.singledungeon.profile;

import me.d0by.singledungeon.SingleDungeon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SingleDungeon.getInstance().getProfileRegistry().register(new Profile(player.getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        SingleDungeon.getInstance().getProfileRegistry().removeProfile(player.getName());
    }

}
