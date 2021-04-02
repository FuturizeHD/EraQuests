package me.futurize.quests.event.listener;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.collect.Maps;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;
import me.futurize.quests.event.QuestCompletionEvent;
import mkremins.fanciful.FancyMessage;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class QuestListener implements Listener {

	private static final String PRIVATE_KEY = "hwkPswjVtCr98va7zT7EUEY";
	private Map<Integer, Quest> npcQuests = Maps.newHashMap();
	
	public QuestListener(QuestsPlugin plugin) {
		// Go through all saved NPCs and add their id to the map
		File file = new File("npcs.yml");
		
		if(!file.exists()) {
            try {
            	file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        Quest quest = plugin.getQuests().stream().filter(q -> q.getId().equals("fishing")).findAny().orElse(null);
        Quest quest2 = plugin.getQuests().stream().filter(q -> q.getId().equals("crafting")).findAny().orElse(null);
        Quest quest3 = plugin.getQuests().stream().filter(q -> q.getId().equals("enchanting")).findAny().orElse(null);
        
        Bukkit.broadcastMessage(quest.getName());
        
        npcQuests.put(0, quest);
        npcQuests.put(1, quest2);
        npcQuests.put(2, quest3);
        
/*        config.getKeys(false).forEach(key -> npcQuests.put(Integer.parseInt(key), quest));*/
        
        Bukkit.getLogger().log(Level.INFO, "Loaded " + npcQuests.size() + " Quest NPC(s)");
	}

	// Change to citizens' event
	@EventHandler
	public void onNPCInteract(NPCRightClickEvent e) {
		Player player = e.getClicker();
		NPC clicked = e.getNPC();
		
		Quest quest = npcQuests.get(clicked.getId());
		
		if (quest == null)
			return;
		
		// Add commands to this
		player.sendMessage(ChatColor.BLUE + quest.getName());
		new FancyMessage(ChatColor.GRAY + "Accept Quest").tooltip("Click to accept the quest").command("/quests " + PRIVATE_KEY + " " + quest.getId() + " start").send(player);
		new FancyMessage(ChatColor.GRAY + "Turn In Quest").tooltip("Click to turn in this quest").command("/quests " + PRIVATE_KEY + " " + quest.getId() + " stop").send(player);
		new FancyMessage(ChatColor.GRAY + "Open Shop").tooltip("Click this to open the shop").send(player);
	}

    @EventHandler
    public void on(QuestCompletionEvent event) {
    	Bukkit.broadcastMessage("Completed the quest");
        /*if(!(plugin.getQuestsData().hasCompleted(event.getPlayer(), event.getQuest()))) {
            if(plugin.getQuestsData().completeQuest(event.getPlayer(), event.getQuest())) {
                //first person
                String broadcastMsg = event.getQuest().getBroadcastFirst(event.getPlayer());
                if(broadcastMsg != null) {
                    Bukkit.broadcastMessage(broadcastMsg);
                }
                
                event.getQuest().getFirstCommands(event.getPlayer())
                        .forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
            }
            else {
                //not first
                String broadcastMsg = event.getQuest().getBroadcastOthers(event.getPlayer());
                if(broadcastMsg != null) {
                    Bukkit.broadcastMessage(broadcastMsg);
                }
                event.getQuest().getOthersCommands(event.getPlayer())
                        .forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
            }
        }*/
    }
    
    public Map<Integer, Quest> getQuestsMap() {
    	return this.npcQuests;
    }
}
