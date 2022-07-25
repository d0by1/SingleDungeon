package me.d0by.singledungeon.nms;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NMSAdapter_v1_18_R2 implements NMSAdapter {

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        if (packet instanceof Packet<?> p) {
            ((CraftPlayer) player).getHandle().b.a.a(p);
        }
    }

    @Override
    public Object packetRemoveEntity(int eid) {
        return new PacketPlayOutEntityDestroy(eid);
    }

}
