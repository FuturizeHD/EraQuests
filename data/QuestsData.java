package me.futurize.quests.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestsData {

    private QuestsPlugin plugin;
    private File disk;
    private FileConfiguration config;

    public QuestsData(QuestsPlugin plugin) {
        this.plugin = plugin;
        initDataFile();
    }

    public int getProgress(Player player, Quest quest) {
        return config.getInt("progress." + player.getUniqueId().toString() + "." + quest.getId());
    }
    
    public String getProgressFormatted(Player player, Quest quest) {
        int raw = config.getInt("progress." + player.getUniqueId().toString() + "." + quest.getId());
        
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(raw);
    }

    public void setProgress(Player player, Quest quest, int progress) {
        config.set("progress." + player.getUniqueId().toString() + "." + quest.getId(), progress);
        save();
    }
    
    public boolean hasStarted(Player player, Quest quest) {
        return config.getBoolean("started." + player.getUniqueId().toString() + "." + quest.getId());
    }
    
    public void setStarted(Player player, Quest quest, boolean started) {
        config.set("started." + player.getUniqueId().toString() + "." + quest.getId(), started);
        save();
    }
    
    public long getCooldownExpiration(Player player, Quest quest) {
        return config.getLong("cooldowns." + player.getUniqueId().toString() + "." + quest.getId());
    }
    
    public void setCooldownExpiration(Player player, Quest quest, long expiration) {
        config.set("cooldowns." + player.getUniqueId().toString() + "." + quest.getId(), expiration);
        save();
    }

    public boolean hasCompleted(Player player, Quest quest) {
        List<String> completedPlayers = config.getStringList("quests." + quest.getId() + ".players");
        return isComplete(quest) && completedPlayers.contains(player.getUniqueId().toString());
    }

    public ConfigurationSection getFirst(Quest quest) {
        return config.getConfigurationSection("quests." + quest.getId() + ".first");
    }

    public int getAmountCompleted(Quest quest) {
        return config.getStringList("quests." + quest.getId() + ".players").size();
    }

    public boolean completeQuest(Player player, Quest quest) {
        List<String> completedPlayers = config.getStringList("quests." + quest.getId() + ".players");

        if(completedPlayers == null || completedPlayers.size() < 1) {
            completedPlayers = new ArrayList<>();
            completedPlayers.add(player.getUniqueId().toString());
            config.set("quests." + quest.getId() + ".players", completedPlayers);
            save();
            return true;
        }
        else {
            completedPlayers.add(player.getUniqueId().toString());
            config.set("quests." + quest.getId() + ".players",completedPlayers);
            save();
            return false;
        }
    }

    public boolean isComplete(Quest quest) {
        List<String> completedPlayers = config.getStringList("quests." + quest.getId() + ".players");
        return completedPlayers != null && completedPlayers.size() >= 1;
    }

    private synchronized void save() {
        try {
            config.save(disk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDataFile() {
        disk = new File(plugin.getDataFolder(), "data.yml");
        if(!disk.exists()) {
            try {
                disk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(disk);
    }
}
