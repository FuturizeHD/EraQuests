package me.futurize.quests.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.futurize.quests.component.Quest;

public class QuestCompletionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Quest quest;
    private Player player;

    public QuestCompletionEvent(Quest quest, Player player) {
        this.quest = quest;
        this.player = player;
    }

    public Quest getQuest() {
        return quest;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
