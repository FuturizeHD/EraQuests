package me.futurize.quests.ui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Menu extends Openable {

    default void open(Player player) {
        Inventory underlay = Bukkit.createInventory(null, size(), title());
        player.openInventory(underlay);
    }

    int size();

    String title();
}
