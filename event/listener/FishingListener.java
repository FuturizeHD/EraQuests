package me.futurize.quests.event.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;
import me.futurize.quests.component.QuestType;
import me.futurize.quests.event.QuestCompletionEvent;

public class FishingListener implements Listener {

	private QuestsPlugin plugin;
	private List<Quest> quests;

	public FishingListener(QuestsPlugin plugin) {
		this.plugin = plugin;
		quests = plugin.getQuests().stream().filter(quest -> quest.getType().equals(QuestType.FISHING))
				.collect(Collectors.toList());
	}

	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
			for (Quest quest : quests) {
				if (plugin.getQuestsData().hasStarted(e.getPlayer(), quest)) {
					int progress = plugin.getQuestsData().getProgress(e.getPlayer(), quest) + 1;
					plugin.getQuestsData().setProgress(e.getPlayer(), quest, progress);

					int targetAmount = Integer.parseInt(quest.getData()[0]);
					
					if (progress >= targetAmount) {
						plugin.getServer().getPluginManager().callEvent(new QuestCompletionEvent(quest, e.getPlayer()));
					}
				}
			}
		}
	}
}
