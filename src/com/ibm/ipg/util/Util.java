package com.ibm.ipg.util;

import com.ibm.ipg.game.entity.Entity;

public class Util {
	
	public static boolean collision(Entity entity, Entity entity2){
		if (entity == null || entity2 == null) 
			return false;
		
		return collision(entity.getX(), entity.getY(), entity2.getX(), entity2.getY());
	}
	
	public static boolean collision(Entity entity, int x, int y){
		if (entity == null) 
			return false;
		
		return collision(entity.getX(), entity.getY(), x, y);
	}
	
	private static boolean collision(int x, int y, int x2, int y2){		
		boolean sameCol = x == x2;
		boolean sameRow = y == y2;
		return sameCol && sameRow;
	}
}
