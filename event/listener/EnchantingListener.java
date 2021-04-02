package me.futurize.quests.event.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;
import me.futurize.quests.component.QuestType;
import me.futurize.quests.event.QuestCompletionEvent;

public class EnchantingListener implements Listener {

	private QuestsPlugin plugin;
	private List<Quest> quests;

	public EnchantingListener(QuestsPlugin plugin) {
		this.plugin = plugin;
		quests = plugin.getQuests().stream().filter(quest -> quest.getType().equals(QuestType.ENCHANTING))
				.collect(Collectors.toList());
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		Player player = (Player) e.getEnchanter();
		for (Quest quest : quests) {
			if (plugin.getQuestsData().hasStarted(player, quest)) {
				Bukkit.broadcastMessage(e.getEnchantsToAdd().toString());
				final Enchantment enchantment = Enchantment.getByName(quest.getData()[0]);
				final int level = Integer.parseInt(quest.getData()[1]);

				if (!e.getEnchantsToAdd().containsKey(enchantment) || e.getEnchantsToAdd().get(enchantment) != level)
					return;

				int progress = plugin.getQuestsData().getProgress(player, quest) + 1;
				plugin.getQuestsData().setProgress(player, quest, progress);

				int targetAmount = Integer.parseInt(quest.getData()[2]);

				if (progress >= targetAmount) {
					plugin.getServer().getPluginManager().callEvent(new QuestCompletionEvent(quest, player));
				}
			}
		}
	}
}
