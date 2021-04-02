package me.futurize.quests.event.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;

public class BossListener implements Listener {
	
    private QuestsPlugin plugin;
    private Quest quest;

    public BossListener(QuestsPlugin plugin) {
        this.plugin = plugin;
/*        quest = plugin.getQuests().stream()
                .filter(quest -> quest.getType().getName().equals("fishing"))
                .findAny().orElse(null);*/
    }

	// Change to MythicMobs' event
	@EventHandler
	public void onBossKill(EntityDeathEvent e) {
		// Code here
	}
}
