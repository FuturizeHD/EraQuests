package me.futurize.quests.event.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.futurize.quests.QuestsPlugin;

public class MenuListener implements Listener {

    private QuestsPlugin plugin;

    public MenuListener(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if(event.getClickedInventory() !=  null
                && event.getClickedInventory().getName() != null
                && event.getClickedInventory().getName().equals(plugin.getQuestsMenu().title())) {
            event.setCancelled(true);
        }
    }
}
