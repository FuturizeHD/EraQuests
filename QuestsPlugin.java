package me.futurize.quests;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.futurize.quests.command.QuestsCmd;
import me.futurize.quests.component.Quest;
import me.futurize.quests.data.QuestsData;
import me.futurize.quests.event.listener.BossListener;
import me.futurize.quests.event.listener.CraftingListener;
import me.futurize.quests.event.listener.EnchantingListener;
import me.futurize.quests.event.listener.FishingListener;
import me.futurize.quests.event.listener.MenuListener;
import me.futurize.quests.event.listener.NPCListener;
import me.futurize.quests.event.listener.QuestListener;
import me.futurize.quests.event.listener.TurnInListener;
import me.futurize.quests.npc.QuestTrait;
import me.futurize.quests.ui.QuestsMenu;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class QuestsPlugin extends JavaPlugin {

	private static QuestsPlugin instance;
    private List<Quest> quests;
    private QuestsData questsData;
    private QuestsMenu questsMenu;

    @Override
    public void onEnable() {
    	instance = this;
        saveDefaultConfig();
        quests = readQuests();
        questsData = new QuestsData(this);
        questsMenu = new QuestsMenu(this);
        
        getCommand("quests").setExecutor(new QuestsCmd(this));
        registerEvents();
        registerNPCs();
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public QuestsData getQuestsData() {
        return questsData;
    }

    public QuestsMenu getQuestsMenu() {
        return questsMenu;
    }

    private List<Quest> readQuests() {
        return getConfig().getConfigurationSection("quests").getKeys(false)
                .stream()
                .map(questId -> new Quest(getConfig(), questId))
                .collect(Collectors.toList());
    }
    
    public static QuestsPlugin getInstance() {
    	return instance;
    }
    
    private void registerNPCs() {
    	if(Bukkit.getServer().getPluginManager().getPlugin("Citizens") == null || Bukkit.getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			Bukkit.getLogger().log(Level.SEVERE, "Citizens not found or not enabled");
			Bukkit.getServer().getPluginManager().disablePlugin(this);	
			return;
		}	

		//Register your trait with Citizens.        
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(QuestTrait.class));	
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new QuestListener(this), this);
        pluginManager.registerEvents(new MenuListener(this), this);
        pluginManager.registerEvents(new FishingListener(this), this);
        pluginManager.registerEvents(new BossListener(this), this);
        pluginManager.registerEvents(new CraftingListener(this), this);
        pluginManager.registerEvents(new EnchantingListener(this), this);
        pluginManager.registerEvents(new TurnInListener(this), this);
        pluginManager.registerEvents(new NPCListener(this), this);
    }
}
