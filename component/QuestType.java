package me.futurize.quests.component;

public enum QuestType {

	TURN_IN("turn_in"),
	BOSSES("bosses"),
	CRAFTING("crafting"),
	FISHING("fishing"),
	NPC("npc"),
	FARMING("farming"),
	ENCHANTING("enchanting");
	
	private String name;
	
	QuestType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static QuestType fromName(String name) {
		for (QuestType quest : values())
			if (quest.getName().equals(name))
				return quest;
		return null;
	}
}
