package me.d0by.singledungeon.dungeon;

import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.nms.NMSAdapter;
import me.d0by.singledungeon.utils.Common;
import me.d0by.singledungeon.utils.ItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * This class represents a dungeon monster. Monsters are spawned in waves.
 *
 * @param name        The name of the monster.
 * @param items       The equipment to give the monster.
 * @param type        The type of the monster.
 * @param displayName The display name of the monster.
 * @param health      The health of the monster.
 */
public record DungeonMonster(@NotNull String name,
                             @NotNull Map<ItemSlot, ItemStack> items,
                             @NotNull EntityType type,
                             @Nullable String displayName,
                             double health) {

    /**
     * Spawn this monster for a player at the given location.
     *
     * @param player   The player to spawn for.
     * @param location The location to spawn at.
     * @return The spawned entity's id.
     */
    @SuppressWarnings("deprecation")
    public int spawn(@NotNull Player player, @NotNull Location location) {
        // Spawn the entity
        Entity entity = player.getWorld().spawnEntity(location, type);
        entity.setPersistent(false);
        if (displayName != null && !displayName.isBlank()) {
            entity.setCustomName(Common.formatString(displayName));
            entity.setCustomNameVisible(true);
        }
        if (entity instanceof LivingEntity living) {
            living.setRemoveWhenFarAway(false);
            living.setMaxHealth(health);
            living.setHealth(health);

            // Set the equipment
            EntityEquipment equipment = living.getEquipment();
            if (equipment != null) {
                equipment.setHelmet(items.get(ItemSlot.HELMET));
                equipment.setChestplate(items.get(ItemSlot.CHESTPLATE));
                equipment.setLeggings(items.get(ItemSlot.LEGGINGS));
                equipment.setBoots(items.get(ItemSlot.BOOTS));
                equipment.setItemInHand(items.get(ItemSlot.MAINHAND));
                equipment.setItemInOffHand(items.get(ItemSlot.OFFHAND));
            }
        }

        // Remove the entity for all the other players
        NMSAdapter nms = SingleDungeon.getInstance().getNmsProvider().getAdapter();
        Object removeEntityPacket = nms.packetRemoveEntity(entity.getEntityId());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) {
                continue;
            }
            nms.sendPacket(onlinePlayer, removeEntityPacket);
        }

        // Return the entity's id
        return entity.getEntityId();
    }

}
