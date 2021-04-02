package me.futurize.quests.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;

public class QuestsMenu implements Menu {

	private QuestsPlugin plugin;

	public QuestsMenu(QuestsPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void open(Player player) {
		Inventory underlay = Bukkit.createInventory(null, size(), title());
		plugin.getQuests().stream().map(quest -> makeEntry(quest, player)).forEach(underlay::addItem);
		player.openInventory(underlay);
	}

	@Override
	public int size() {
		return 18;
	}

	@Override
	public String title() {
		return ChatColor.AQUA + "Quests";
	}

	private ItemStack makeEntry(Quest quest, Player player) {
		Material entryMaterial;
		if (plugin.getQuestsData().hasCompleted(player, quest)) {
			entryMaterial = Material.EMERALD_BLOCK;
		} else {
			entryMaterial = Material.QUARTZ_BLOCK;
		}

		ItemStack questItem = new ItemStack(entryMaterial);
		ItemMeta questItemMeta = questItem.getItemMeta();
		questItemMeta.setDisplayName(quest.getName());
		//int progress = plugin.getQuestsData().getProgress(player, quest);

		questItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		// questItemMeta.setLore(quest.getIncomplete(progress));
		questItem.setItemMeta(questItemMeta);
		questItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		return questItem;
	}
}
