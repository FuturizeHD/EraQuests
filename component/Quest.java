package me.futurize.quests.component;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Quest {

    private ConfigurationSection questSection;
    private String id;
    private QuestType type;
    private long cooldown;
    private String[] data;
    private String permission;

    public Quest(FileConfiguration config, String id) {
        this.questSection = config.getConfigurationSection("quests." + id);
        this.id = id;
        this.type = QuestType.fromName(questSection.getString("type"));
        this.cooldown = questSection.getLong("cooldown");
        this.data = questSection.getString("data").split(",");
        this.permission = questSection.getString("permission");
        
        Bukkit.getLogger().log(Level.INFO, "Added Quest: " + id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return format(questSection.getString("name"));
    }
    
    public QuestType getType() {
    	return this.type;
    }
    
    public long getCooldown() {
    	return this.cooldown;
    }
    
    public String[] getData() {
    	return this.data;
    }
    
    public String getPermission() {
    	return this.permission;
    }
    
    public List<String> getCommands(Player player) {
        return getCommands("completion-commands", player);
    }

    private List<String> getCommands(String node, Player player) {
        return questSection.getStringList(node)
                .stream()
                .map(this::format)
                .map(cmd -> cmd.replaceAll("%player%",player.getName()))
                .collect(Collectors.toList());
    }


    private String format(String raw) {
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    private String toHumanTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return String.format("%d days %d hours %d minutes ago", days, hours % 24, minutes % 60);
    }
}
