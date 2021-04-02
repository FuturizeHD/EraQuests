package me.futurize.quests.event.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;
import me.futurize.quests.component.QuestType;
import me.futurize.quests.event.QuestCompletionEvent;

public class TurnInListener implements Listener {

	private QuestsPlugin plugin;
	private List<Quest> quests;

	public TurnInListener(QuestsPlugin plugin) {
		this.plugin = plugin;
		quests = plugin.getQuests().stream().filter(quest -> quest.getType().equals(QuestType.TURN_IN))
				.collect(Collectors.toList());
	}

	@EventHandler
	public void onInteract(CraftItemEvent e) {
		// Make sure this doesn't have more than 1 person
		Player player = (Player) e.getViewers().get(0);
		
		for (Quest quest : quests) {
			if (plugin.getQuestsData().hasStarted(player, quest)) {
				final Material craftItem = Material.getMaterial(quest.getData()[0]);
				
				if (e.getCurrentItem().getType() != craftItem)
					return;
				
				int targetAmount = Integer.parseInt(quest.getData()[1]);
				
				int progress = plugin.getQuestsData().getProgress(player, quest) + 1;
				plugin.getQuestsData().setProgress(player, quest, progress);

				if (progress >= targetAmount) {
					plugin.getServer().getPluginManager().callEvent(new QuestCompletionEvent(quest, player));
				}
			}
		}
	}
}
