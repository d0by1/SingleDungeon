package me.d0by.singledungeon.nms;

import me.d0by.singledungeon.SingleDungeon;
import me.d0by.singledungeon.utils.Common;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * This class is responsible for providing the correct NMS adapter.
 *
 * @author d0by
 * @see NMSAdapter
 * @since 1.0.0
 */
public class NMSProvider {

    private NMSAdapter adapter;

    /**
     * Create a new instance of {@link NMSProvider}. This will also
     * initialize the correct NMS adapter.
     */
    public NMSProvider() {
        String className = getClass().getPackageName() + ".NMSAdapter_" + Common.getNMSVersion();
        try {
            Class<?> clazz = Class.forName(className);
            adapter = (NMSAdapter) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Logger logger = SingleDungeon.getInstance().getLogger();
            logger.severe("Failed to load NMS adapter for version " + Common.getNMSVersion() + "!");
            logger.severe("Your server version is not supported.");
            logger.severe("Plugin will now disable!");
            logger.severe("Exception:");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(SingleDungeon.getInstance());

        }
    }

    /**
     * Get the correct NMS adapter.
     *
     * @return The correct NMS adapter.
     * @see NMSAdapter
     */
    public NMSAdapter getAdapter() {
        return adapter;
    }

}
