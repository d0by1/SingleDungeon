package me.d0by.singledungeon.utils;

import lombok.experimental.UtilityClass;
import me.d0by.singledungeon.Config;
import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.dungeon.game.DungeonInstance;
import me.d0by.singledungeon.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A collection of utility methods.
 *
 * @author d0by
 * @since 1.0.0
 */
@UtilityClass
public final class Common {

    private static final SingleDungeon PLUGIN = SingleDungeon.getInstance();

    /**
     * Translate all color codes including rgb in the given string.
     *
     * @param string The string to translate.
     * @return The translated string.
     */
    @NotNull
    public static String colorize(@NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Format the given string replacing all java format placeholders with the given arguments,
     * replacing the '{prefix}' placeholder and colorizing the string.
     *
     * @param message The message to format.
     * @param args    The arguments to format.
     * @return The formatted string.
     */
    @NotNull
    public static String formatString(@NotNull String message, Object... args) {
        return formatString(null, message, args);
    }

    /**
     * Format the given string replacing all java format placeholders with the given arguments,
     * replacing the '{prefix}' placeholder and colorizing the string.
     *
     * @param message The message to format.
     * @param args    The arguments to format.
     * @return The formatted string.
     */
    @NotNull
    public static String formatString(@Nullable CommandSender sender, @NotNull String message, Object... args) {
        message = String.format(message, args);
        message = message.replace("{prefix}", Config.PREFIX);
        if (sender instanceof Player player) {
            message = replacePlaceholders(player, message);
            System.out.println(message);
        }
        message = colorize(message);
        return message;
    }

    /**
     * Send a message to the given {@link CommandSender}. This method formats the message
     * before sending it using the {@link #formatString(String, Object...)} method.
     *
     * @param sender  The {@link CommandSender} to send the message to.
     * @param message The message to send.
     * @param args    The arguments to format.
     */
    public static void tell(@NotNull CommandSender sender, @NotNull String message, Object... args) {
        sender.sendMessage(formatString(sender, message, args));
    }

    /**
     * Replace all built-in placeholders in the given string.
     *
     * @param player The player to replace the placeholders for.
     * @param string The string to replace placeholders in.
     * @return The string with replaced placeholders.
     */
    public String replacePlaceholders(@NotNull Player player, @NotNull String string) {
        Profile profile = PLUGIN.getProfileRegistry().getProfile(player);
        if (profile == null) {
            return string;
        }
        DungeonInstance d = profile.getCurrentDungeon();
        if (d == null) {
            return string;
        }
        return string
                .replace("{player}", player.getName())
                .replace("{dungeon}", d.getDungeon().name())
                .replace("{wave}", d.getCurrentWave() + "")
                .replace("{waves}", d.getDungeon().waves().size() + "")
                .replace("{kills}", d.getKills() + "")
                .replace("{time}", formatDuration(d.getTime().get()))
                .replace("{time_long}", formatDurationLong(d.getTime().get()))
                .replace("{time_seconds}", d.getTime().get() + "")
                .replace("{took}", formatDuration(d.getTotalTime().get()))
                .replace("{took_long}", formatDurationLong(d.getTotalTime().get()))
                ;
    }

    /**
     * Format the given amount of seconds to minutes and seconds.
     *
     * @param seconds The amount of seconds.
     * @return The formatted string. Example: "1:00"
     */
    public static String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int secondsLeft = seconds % 60;
        return String.format("%d:%02d", minutes, secondsLeft);
    }

    /**
     * Format the given amount of seconds to minutes and seconds.
     *
     * @param seconds The amount of seconds.
     * @return The formatted string. Example: "1 minute 10 seconds"
     */
    public static String formatDurationLong(int seconds) {
        if (seconds == 0) {
            return "0 seconds";
        }

        StringBuilder sb = new StringBuilder();
        int minutes = seconds / 60;
        int secondsLeft = seconds % 60;

        if (minutes > 0) {
            sb.append(minutes).append(" minute");
            if (minutes > 1) {
                sb.append("s");
            }
        }

        if (secondsLeft > 0) {
            sb.append(" ");
            sb.append(secondsLeft).append(" second");
            if (secondsLeft > 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }

    /**
     * Heal the given player.
     *
     * @param player The player to heal.
     */
    @SuppressWarnings("deprecation")
    public static void heal(@NotNull Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setRemainingAir(player.getMaximumAir());
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setArrowsInBody(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    /**
     * Get the version of the server.
     *
     * @return The version of the server.
     */
    public static String getNMSVersion() {
        return SingleDungeon.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
