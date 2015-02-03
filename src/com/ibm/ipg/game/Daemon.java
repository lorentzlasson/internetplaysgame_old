package com.ibm.ipg.game;

import java.util.List;

public class Daemon implements Runnable{

	private static final long INTERVAL_UPDATE = 1000; // 1 second
	
	private boolean running;
	private long lastUpdate;	
	
	private Game game;
	
	public Daemon(Game game) {
		this.game = game;
		running = true;
		lastUpdate = System.currentTimeMillis();
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void run() {
		while (running) {
			long now = System.currentTimeMillis();
			if (now - lastUpdate >= INTERVAL_UPDATE) {
				kickInactives();
				provideResources();
//				System.out.println("Daemon tick: "+now);
				lastUpdate = now;
				
				try {
					Thread.sleep(990);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void kickInactives(){
		List<User> kicked = game.kickInactiveUsers();
		if (kicked.size() > 0) {
			System.out.println("Kicked: "+kicked);			
		}		
	}
	
	private void provideResources() {
		game.provideResources();
	}
}
