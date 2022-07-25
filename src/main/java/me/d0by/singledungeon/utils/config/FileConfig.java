package me.d0by.singledungeon.utils.config;

import lombok.Getter;
import me.d0by.singledungeon.SingleDungeon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * This class extends {@link YamlConfiguration} and is used to load and save
 * the configuration file. It also provides a method to reload the configuration
 * file. You can also use it as normal {@link YamlConfiguration}.
 *
 * @author d0by
 * @since 1.0.0
 */
@Getter
public class FileConfig extends YamlConfiguration {

    private static final SingleDungeon PLUGIN = SingleDungeon.getInstance();
    protected final String path;
    protected final File file;

    /**
     * Creates a new instance of {@link FileConfig}.
     * <p>
     * This constructor also creates the file if it doesn't exist and
     * loads the configuration.
     * </p>
     *
     * @param path The path to the file. Must be a relative path to .yml file.
     */
    public FileConfig(@NotNull String path) {
        this.path = path;
        this.file = new File(PLUGIN.getDataFolder(), path);
        this.createFile();
        this.reload();
    }

    /**
     * Creates a new instance of {@link FileConfig}.
     * <p>
     * This constructor also creates the file if it doesn't exist and
     * loads the configuration.
     * </p>
     *
     * @param file The file to load. Must be a .yml file.
     */
    public FileConfig(@NotNull File file) {
        this.path = file.getName();
        this.file = file;
        this.createFile();
        this.reload();
    }

    /**
     * Creates the file if it doesn't exist. If the file is also a resource,
     * it will be copied as the default configuration.
     */
    public void createFile() {
        if (!file.exists()) {
            PLUGIN.getDataFolder().mkdirs();

            // If file isn't a resource, create from scratch
            if (PLUGIN.getResource(this.path) == null) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                PLUGIN.saveResource(this.path, false);
            }
        }
    }

    /**
     * Saves this configuration to the file.
     */
    public void saveData() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the configuration from the file.
     */
    public void reload() {
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Location getLocation(@NotNull String path) {
        World world = Bukkit.getWorld(getString(path + ".world", "world"));
        double x = getDouble(path + ".x", 0);
        double y = getDouble(path + ".y", 0);
        double z = getDouble(path + ".z", 0);
        float yaw = (float) getDouble(path + ".yaw", 0);
        float pitch = (float) getDouble(path + ".pitch", 0);
        return new Location(world, x, y, z, yaw, pitch);
    }

}