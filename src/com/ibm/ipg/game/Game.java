package com.ibm.ipg.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.ibm.ipg.game.User.PublicUser;
import com.ibm.ipg.game.entity.Death;
import com.ibm.ipg.game.entity.Entity;
import com.ibm.ipg.game.entity.Entity.Direction;
import com.ibm.ipg.game.entity.Entity.EntityType;
import com.ibm.ipg.game.entity.Goal;
import com.ibm.ipg.game.entity.Subtractor;
import com.ibm.ipg.game.exception.IpgException;

public class Game {
	
	private static final long INACTIVITY_THRESHOLD = 60000; // 1 minute

	protected static final int FLAG_MOVE_DEFAULT = 0;
	protected static final int FLAG_MOVE_GOAL = 1;
	protected static final int FLAG_MOVE_DEATH = 2;
	protected static final int FLAG_MOVE_SUBTRACTOR = 3;
	
	private transient Thread thread;
	private transient Daemon daemon;
	
	private transient String[] ids;
	private transient Map<String, User> users = new HashMap<String, User>();
	
	private List<PublicUser> pubUsers;
	private User user;
	
	private Grid grid;

	/**
	 * Also start daemon thread
	 * @param width
	 * @param height
	 */
	public Game(int width, int height, Game game) {
		if (game != null) game.stopDaemon();
		this.ids = new String[]{ "1", "2", "3", "4", "5" };
		grid = new Grid(width, height);
		startDaemon();
	}
	
	public void setGameSettings(GameSettings settings){
		Map<EntityType, Integer> entityTypes = settings.getEntityTypes();
		Iterator<Entry<EntityType, Integer>> it = entityTypes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<EntityType, Integer> entry = (Entry<EntityType, Integer>)it.next();
	        EntityType type = entry.getKey();
	        int amount = entry.getValue();
	        for (int i = 0; i < amount; i++) {
				addEntity(type);
			}
	    }
	}
	
	private void addEntity(EntityType entityType) {
		Entity entity = null;
		switch (entityType) {
		case GOAL:
			entity = new Goal();
			break;

		case DEATH:
			entity = new Death();
			break;

		case SUBSTRACTOR:
			entity = new Subtractor();
			break;
			
		default:
			break;
		}
		grid.addEntity(entity);
	}
	
	private void startDaemon(){
		daemon = new Daemon(this);
		thread = new Thread(daemon);
		thread.start();
	}
	
	public void setPlayerAmount(int playerAmount) {
		ids = new String[playerAmount];
		for (int i = 0; i < playerAmount; i++) {
			ids[i] = String.valueOf(i+1);
		}
	}
	
	public void stopDaemon() {
		try {
			if (daemon != null) daemon.setRunning(false);
			if (thread != null) thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void movePlayer(Direction direction, String key) throws IpgException {
		if (playerAction(key, User.COST_MOVE)) {
			int flag = grid.movePlayer(direction);
			switch (flag) {
			case FLAG_MOVE_GOAL:
				user.increaseScore(users.size()-1);				
				break;

			case FLAG_MOVE_DEATH:
				user.gameOver();			
				break;

			case FLAG_MOVE_SUBTRACTOR:
				user.lowerResource(1);
				break;
				
			default:
				break;
			}
		}
	}
	
	public void reveal(String key) throws IpgException {
		if (playerAction(key, User.COST_REVEAL)) {
		}
	}

	public String newUser() throws IpgException {
		String id = getAvailableId();
		
		// Room left in game
		if (id != null) {
			String key = UUID.randomUUID().toString();
			User user = new User(id, key);
			users.put(key, user);
			this.user = user;
			return key;
		}
		throw new IpgException("Game is full", 2);
	}

	public User kickUser(String key) {
		User user = users.remove(key);
		return user;
	}

	/**
	 * @return Inactive users
	 */
	public List<User> kickInactiveUsers() {
		List<User> inactiveUsers = new ArrayList<User>();
		Iterator<Entry<String, User>> it = users.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, User> pairs = (Entry<String, User>)it.next();
	        User user = pairs.getValue();
	        long since = user.sinceLastActivity();
//			if (since > INACTIVITY_THRESHOLD) {
//				inactiveUsers.add(user);
//		        it.remove();
//			}
	    }
		return inactiveUsers;
	}

	public void provideResources() {
		for (User user: users.values()) {
			user.increaseResource(User.RESOURCE_TICK);
		}
	}
	
	public void syncPublicUsers() {
		pubUsers = new ArrayList<PublicUser>();
		for (User user : users.values()) {
			pubUsers.add(user.getAsPublicUser());
		}
	}
	
	private boolean playerAction(String key, int cost) throws IpgException{
		User user = users.get(key);
		if (user == null)
			throw new IpgException("Cannot find player. Refresh browser to enter game.", 2);		
		
		this.user = user;
		if (this.user.getResource() < cost)
			throw new IpgException("Not enough resources.", 2);			
		
		user.lowerResource(cost);
		user.activate();
		return true;
	}

	private String getAvailableId() {
		List<String> availableIds = new ArrayList<String>(Arrays.asList(ids));
		for (User user : users.values()) {
			availableIds.remove(user.getId());
		}
		if (availableIds.size() > 0)
			return availableIds.get(0);
		
		return null;
	}
}
