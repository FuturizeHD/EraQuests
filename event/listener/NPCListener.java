package me.futurize.quests.event.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;
import me.futurize.quests.component.QuestType;
import me.futurize.quests.event.QuestCompletionEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCListener implements Listener {

	private QuestsPlugin plugin;
	private List<Quest> quests;

	public NPCListener(QuestsPlugin plugin) {
		this.plugin = plugin;
		quests = plugin.getQuests().stream().filter(quest -> quest.getType().equals(QuestType.NPC))
				.collect(Collectors.toList());
	}

	@EventHandler
	public void onInteract(NPCRightClickEvent e) {
		Player player = e.getClicker();
		for (Quest quest : quests) {
			if (plugin.getQuestsData().hasStarted(player, quest)) {
				int progress = plugin.getQuestsData().getProgress(player, quest) + 1;
				plugin.getQuestsData().setProgress(player, quest, progress);

				int targetAmount = Integer.parseInt(quest.getData()[0]);

				if (progress >= targetAmount) {
					plugin.getServer().getPluginManager().callEvent(new QuestCompletionEvent(quest, player));
				}
			}
		}
	}
}
