package me.d0by.singledungeon.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface NMSAdapter {

    void sendPacket(@NotNull Player player, @NotNull Object packet);

    Object packetRemoveEntity(int eid);

}
