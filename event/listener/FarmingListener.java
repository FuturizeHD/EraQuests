package me.futurize.quests.event.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;
import me.futurize.quests.component.QuestType;
import me.futurize.quests.event.QuestCompletionEvent;

public class FarmingListener implements Listener {

	private QuestsPlugin plugin;
	private List<Quest> quests;

	public FarmingListener(QuestsPlugin plugin) {
		this.plugin = plugin;
		quests = plugin.getQuests().stream().filter(quest -> quest.getType().equals(QuestType.FARMING))
				.collect(Collectors.toList());
	}

	@EventHandler
	public void onFarmMob(EntityDeathEvent e) {
		if (e.getEntity().getKiller() == null || !(e.getEntity().getKiller() instanceof Player))
			return;
		
		// Make sure this doesn't have more than 1 person
		Player player = (Player) e.getEntity().getKiller();
		
		for (Quest quest : quests) {
			if (plugin.getQuestsData().hasStarted(player, quest)) {
				final EntityType mobType = EntityType.valueOf(quest.getData()[0]);
				
				if (e.getEntityType() != mobType)
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
