package com.ibm.ipg.response;

import com.ibm.ipg.game.Game;

public class GameResponse extends Response{
	
	private Game game;
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
}
