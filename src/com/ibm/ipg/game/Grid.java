package com.ibm.ipg.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ibm.ipg.game.entity.Death;
import com.ibm.ipg.game.entity.Entity;
import com.ibm.ipg.game.entity.Entity.Direction;
import com.ibm.ipg.game.entity.Goal;
import com.ibm.ipg.game.entity.Player;
import com.ibm.ipg.game.entity.Subtractor;

public class Grid {

	private static final int RESET_TRY_CAP = 50;
	private Entity[][] grid;
	private Player player;
	
	public Grid(int width, int height) {
		grid = new Entity[height][width];
		player = new Player();
		reset(player);
	}
	
	public void addEntity(Entity entity) {
		reset(entity);
	}
	
	public int movePlayer(Direction direction) {
		int x = player.getX();
		int y = player.getY();
		
		switch (direction) {
		case LEFT:
			if (x > 0) x--;			
			break;

		case UP:
			if (y > 0) y--;	
			break;

		case RIGHT:
			if (x + 1 < grid[0].length) x++;
			break;

		case DOWN:
			if (y + 1 < grid.length) y++;
			break;

		default:
			break;
		}
		
		Entity stepedOn = grid[y][x];
		int flag = Game.FLAG_MOVE_DEFAULT;
		if (stepedOn != null) {
			if (stepedOn instanceof Goal) {
				flag = Game.FLAG_MOVE_GOAL;
			}
			else if (stepedOn instanceof Death) {
				flag = Game.FLAG_MOVE_DEATH;
			}
			else if (stepedOn instanceof Subtractor) {
				flag = Game.FLAG_MOVE_SUBTRACTOR;
			}
			
			if (!(stepedOn instanceof Player)) {
				resetAllButPlayer();
			}
		}
		
		replaceEntity(player, x, y);
		return flag;
	}
	
	private void resetAllButPlayer(){
		List<Entity> replaced = new ArrayList<Entity>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				Entity entity = grid[i][j];
				if (entity != null && !replaced.contains(entity)) {
					reset(entity);
					replaced.add(entity);
//					System.out.println("Replaced: "+entity);
				}
			}
		}
	}
	
	private void reset(Entity entity){
		Random random = new Random();
		int tries = 0;
		int x, y;
		do {
			x = random.nextInt(grid[0].length);
			y = random.nextInt(grid.length);
			tries++;
		} while (grid[y][x] != null && tries < RESET_TRY_CAP); // Max tries to prevent infinite loop
		replaceEntity(entity, x, y);
//		System.out.println("Replacement tries: "+tries);
	}
	
	private void replaceEntity(Entity entity, int x, int y){
		if (entity != null)
			grid[entity.getY()][entity.getX()] = null;
		
		entity.setX(x).setY(y);
		grid[y][x] = entity;
	}
}
