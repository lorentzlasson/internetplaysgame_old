package com.ibm.ipg.game;

public class User {

	public static final int COST_MOVE = 8;
	public static final int COST_REVEAL = 3;
	public static final int RESOURCE_TICK = 1;
	private static final int MAX_RESOURCE = 20;
	
	private String id;
	private String key;
	private int score;
	private int achievedScore;
	private boolean alive;
	private long lastActivity;
	private int resource;
	
	public User(String id, String key) {
		this.id = id;
		this.key = key;
		score = 0;
		resource = MAX_RESOURCE;
		activate();
	}
	
	public String getKey() {
		return key;
	}
	
	public String getId() {
		return id;
	}
	
	public void increaseScore(int amount) {
		score += amount;
	}
	
	public void gameOver() {
		achievedScore = score;
		score = 0;
		alive = false;
	}
	
	public void activate() {
		this.alive = true;
		lastActivity = System.currentTimeMillis();
	}
	
	public void increaseResource(int amount) {
		if (resource + amount <= MAX_RESOURCE) {
			resource += amount;			
		}
		else {
			resource = MAX_RESOURCE;
		}
	}
	
	public void lowerResource(int amount) {
		resource -= amount;
	}
	
	public int getResource() {
		return resource;
	}

	public long sinceLastActivity() {
		return System.currentTimeMillis() - lastActivity;
	}
	
	public int getAchievedScore() {
		return achievedScore;
	}

	public boolean isAlive() {
		return alive;
	}

	public PublicUser getAsPublicUser() {
		return new PublicUser(id, score);
	}
	
	public class PublicUser {
		private String id;
		private int score;
		
		public PublicUser(String id, int score) {
			super();
			this.id = id;
			this.score = score;
		}

		public String getId() {
			return id;
		}

		public int getScore() {
			return score;
		}
	}
}
