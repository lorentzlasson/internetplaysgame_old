package com.ibm.ipg.game;

import java.util.HashMap;
import java.util.Map;

import com.ibm.ipg.game.entity.Entity.EntityType;

public class GameSettings {
	
	public enum GameMode {
		DEFAULT, DANGER, SAFE
	}
	
	private Map<EntityType, Integer> entityTypes = new HashMap<EntityType, Integer>();

	/**
	 * Select from mode constants
	 * @param gameMode
	 */
	public GameSettings(GameMode gameMode, int cells) {
		switch (gameMode) {
		case DEFAULT:
			entityTypes.put(EntityType.GOAL, 1);
			entityTypes.put(EntityType.DEATH, 1);
			entityTypes.put(EntityType.SUBSTRACTOR, Math.round(cells / 10));
			break;

		case DANGER:
			entityTypes.put(EntityType.GOAL, Math.round(cells / 6));
			entityTypes.put(EntityType.DEATH, Math.round(cells / 6));
			entityTypes.put(EntityType.SUBSTRACTOR, Math.round(cells / 6));
			break;

		case SAFE:
			entityTypes.put(EntityType.GOAL, 1);
			entityTypes.put(EntityType.DEATH, 1);
			break;
			
		default:
			break;
		}
	}

	public Map<EntityType, Integer> getEntityTypes() {
		return entityTypes;
	}
}
