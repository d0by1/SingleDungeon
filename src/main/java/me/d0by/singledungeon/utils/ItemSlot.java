package me.d0by.singledungeon.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This enum holds all item slots, which can be used by monsters.
 *
 * @author d0by
 * @since 1.0.0
 */
public enum ItemSlot {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    MAINHAND,
    OFFHAND;

    /**
     * Get a {@link ItemSlot} from a string.
     *
     * @param s The string to get the {@link ItemSlot} from.
     * @return The {@link ItemSlot} or null if the string is invalid.
     */
    @Nullable
    public static ItemSlot fromString(@NotNull String s) {
        for (ItemSlot slot : values()) {
            if (slot.name().equalsIgnoreCase(s)) {
                return slot;
            }
        }
        return null;
    }

}
