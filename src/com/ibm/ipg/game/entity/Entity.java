package com.ibm.ipg.game.entity;

public class Entity {
	
	public enum EntityType {
		GOAL, DEATH, SUBSTRACTOR
	}
	
	public enum Direction {
		LEFT, UP, RIGHT, DOWN
	}
	
	private String type;
	private int x, y;

	public Entity(String type) {
		this.type = type;
	}
	
	public int getX() {
		return x;
	}

	public Entity setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public Entity setY(int y) {
		this.y = y;
		return this;
	}

	public String getType() {
		return type;
	}
}
