package me.d0by.singledungeon.dungeon;

import com.google.common.base.Enums;
import me.d0by.singledungeon.utils.ItemSlot;
import me.d0by.singledungeon.utils.config.FileConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This registry is responsible for managing all dungeons, waves and monsters.
 *
 * @author d0by
 * @since 1.0.0
 */
public class DungeonRegistry {

    private final @NotNull Map<String, Dungeon> dungeons;
    private final @NotNull Map<String, DungeonWave> waves;
    private final @NotNull Map<String, DungeonMonster> monsters;
    private final @NotNull FileConfig dungeonsConfig;
    private final @NotNull FileConfig wavesConfig;
    private final @NotNull FileConfig monstersConfig;

    /**
     * Create a new instance of {@link DungeonRegistry}. This constructor
     * also loads all dungeons, waves and monsters from the config files.
     */
    public DungeonRegistry() {
        this.dungeons = new HashMap<>();
        this.waves = new HashMap<>();
        this.monsters = new HashMap<>();

        this.dungeonsConfig = new FileConfig("dungeons.yml");
        this.wavesConfig = new FileConfig("waves.yml");
        this.monstersConfig = new FileConfig("monsters.yml");

        this.reload();
    }

    /**
     * Reload this registry loading all monsters, waves and dungeons from their respective config files.
     */
    public void reload() {
        this.shutdown();

        // Load everything from files

        // 1) Load monsters
        this.monstersConfig.reload();

        for (String key : this.monstersConfig.getKeys(false)) {
            EntityType type = Enums.getIfPresent(EntityType.class, this.monstersConfig.getString(key + ".type", "")).orNull();
            if (type == null) {
                continue;
            }
            String name = this.monstersConfig.getString(key + ".name", "");
            double health = this.monstersConfig.getDouble(key + ".health", 20.0);

            ConfigurationSection itemsSection = this.monstersConfig.getConfigurationSection(key + ".items");
            if (itemsSection == null) {
                continue;
            }
            Map<ItemSlot, ItemStack> items = new HashMap<>();
            for (String slot : itemsSection.getKeys(false)) {
                ItemSlot itemSlot = ItemSlot.fromString(slot);
                if (itemSlot == null) {
                    continue;
                }
                Material material = Enums.getIfPresent(Material.class, itemsSection.getString(slot + ".material", "")).orNull();
                if (material == null) {
                    continue;
                }
                ItemStack itemStack = new ItemStack(material);

                ConfigurationSection enchantmentsSection = itemsSection.getConfigurationSection(slot + ".enchantments");
                if (enchantmentsSection != null) {
                    Map<Enchantment, Integer> enchantments = new HashMap<>();
                    for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
                        Enchantment enchantment = Enchantment.getByName(enchantmentKey);
                        if (enchantment == null) {
                            continue;
                        }
                        enchantments.put(enchantment, enchantmentsSection.getInt(enchantmentKey, 1));
                    }
                    itemStack.addUnsafeEnchantments(enchantments);
                }
                items.put(itemSlot, itemStack);
            }
            DungeonMonster monster = new DungeonMonster(key, items, type, name, health);
            this.monsters.put(key, monster);
        }

        // 2) Load waves
        this.wavesConfig.reload();

        for (String key : this.wavesConfig.getKeys(false)) {
            int duration = this.wavesConfig.getInt(key + ".duration", 0);
            List<String> monsterNames = this.wavesConfig.getStringList(key + ".monsters");
            List<DungeonMonster> monsters = monsterNames.stream()
                    .map(this.monsters::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            DungeonWave wave = new DungeonWave(key, monsters, duration);
            this.waves.put(key, wave);
        }

        // 3) Load dungeons
        this.dungeonsConfig.reload();

        for (String key : this.dungeonsConfig.getKeys(false)) {
            Location spawnLocation = this.dungeonsConfig.getLocation(key + ".spawn");
            if (spawnLocation == null) {
                continue;
            }
            Location waveSpawnLocation = this.dungeonsConfig.getLocation(key + ".wave_spawn");
            if (waveSpawnLocation == null) {
                continue;
            }
            List<String> waveNames = this.dungeonsConfig.getStringList(key + ".waves");
            List<DungeonWave> waves = waveNames.stream()
                    .map(this.waves::get)
                    .filter(Objects::nonNull)
                    .toList();
            int prepareTime = this.dungeonsConfig.getInt(key + ".prepare_time", 0);
            Dungeon dungeon = new Dungeon(key, spawnLocation, waveSpawnLocation, waves, prepareTime);
            this.dungeons.put(key, dungeon);
        }
    }

    /**
     * Shutdown this registry, removing all monsters, waves and dungeons from cache.
     */
    public void shutdown() {
        this.dungeons.clear();
        this.waves.clear();
        this.monsters.clear();
    }

    /**
     * Get a dungeon by its name.
     *
     * @param name The name.
     * @return The dungeon, or null if not found.
     * @see Dungeon
     */
    @Nullable
    public Dungeon getDungeon(@NotNull String name) {
        return this.dungeons.get(name);
    }

}
