package me.d0by.singledungeon.commands;

import me.d0by.singledungeon.Config;
import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.dungeon.Dungeon;
import me.d0by.singledungeon.dungeon.game.DungeonInstance;
import me.d0by.singledungeon.dungeon.game.DungeonStatus;
import me.d0by.singledungeon.profile.Profile;
import me.d0by.singledungeon.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private static final SingleDungeon PLUGIN = SingleDungeon.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("singledungeon.start") || !(sender instanceof Player player)) {
            Common.tell(sender, Config.NO_PERM);
            return true;
        }

        if (args.length < 1) {
            Common.tell(player, Config.USAGE, "/start <dungeon>");
            return true;
        }

        Dungeon dungeon = PLUGIN.getDungeonRegistry().getDungeon(args[0]);
        if (dungeon == null) {
            Common.tell(player, Config.DUNGEON_NOT_FOUND, args[0]);
            return true;
        }
        Common.tell(player, Config.DUNGEON_START, args[0]);

        Profile profile = PLUGIN.getProfileRegistry().getProfile(player);
        if (profile == null) {
            System.out.println(1);
            return true;
        }
        DungeonInstance instance = new DungeonInstance(dungeon, player);
        profile.setCurrentDungeon(instance);
        instance.setStatus(DungeonStatus.STARTING);
        return true;
    }

}
