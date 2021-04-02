package me.futurize.quests.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.futurize.quests.QuestsPlugin;
import me.futurize.quests.component.Quest;

public class QuestsCmd implements CommandExecutor {

	private static final String PRIVATE_KEY = "hwkPswjVtCr98va7zT7EUEY";
    private QuestsPlugin plugin;

    public QuestsCmd(QuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if (args.length != 2)
            	return false;
            
            Player player = (Player) sender;
            
            String key = args[0];
            
            if (!key.equals(PRIVATE_KEY))
            	return true;
            
            String questName = args[1];
            String action = args[2];
            
            Quest quest = plugin.getQuests().stream()
            .filter(q -> q.getId().equals(questName))
            .findAny().orElse(null);
            
            if (!player.hasPermission(quest.getPermission())) {
            	player.sendMessage(ChatColor.RED + "You do not have permission to start that quest");
            	return true;
            }
            
            plugin.getQuestsData().setStarted(player, quest, true);
            
            player.sendMessage(ChatColor.GREEN + "You have started " + questName);
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use quest commands!");
        }
        return false;
    }
}
