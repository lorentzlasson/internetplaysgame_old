package com.ibm.ipg;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import com.ibm.ipg.dal.CloudantLayer;
import com.ibm.ipg.dal.HighScore;
import com.ibm.ipg.game.Game;
import com.ibm.ipg.game.GameSettings;
import com.ibm.ipg.game.User;
import com.ibm.ipg.game.GameSettings.GameMode;
import com.ibm.ipg.game.entity.Entity.Direction;
import com.ibm.ipg.game.exception.IpgException;
import com.ibm.ipg.response.Error;
import com.ibm.ipg.response.GameResponse;
import com.ibm.ipg.response.Message;
import com.ibm.ipg.response.Response;


@Path("/game")
public class GameResource {

	private static final String NO_GAME = "Currently no game running";
	private static final String INCORRECT_ADMIN_KEY = "Unauthorized";
	private static Game game;
	
	private static Properties prop = new Properties();
	private static String ADMIN_KEY;

	@POST
	@Path("/new")
	public String newGame(
			@QueryParam("size") String size, 
			@QueryParam("gameMode") GameMode gameMode,
			@QueryParam("playerCap") String playerCap, 
			@QueryParam("adminKey") String adminKey) {
		
		initAdminKey();
		
		// Admin verification
		if (!adminKey.equals(ADMIN_KEY)) 
			return getGameResponse(null, new Error(INCORRECT_ADMIN_KEY, 3));

		// Size
		int intSize = Integer.parseInt(size);
		game = new Game(intSize, intSize, game);
		
		// Game settings
		GameSettings settings = new GameSettings(gameMode, intSize * intSize);
		game.setGameSettings(settings);
		
		// Player cap
		if (playerCap != null) {
			int intPlayerCap = Integer.parseInt(playerCap);
			game.setPlayerAmount(intPlayerCap);			
		}
		
		return getGameResponse(game, new Message("Game started"));
	}

	@POST
	@Path("/end")
	public String endGame(@QueryParam("adminKey") String adminKey) {
		if (!adminKey.equals(ADMIN_KEY)) return getGameResponse(game, new Error(INCORRECT_ADMIN_KEY, 3));
		if (game != null) {
			game.stopDaemon();
			game = null;
			return getGameResponse(game, new Message("Game ended"));
		}
		return getGameResponse(game, new Error(NO_GAME, 1));
	}

	/**
	 * @return Inactive users as json string
	 */
	@POST
	@Path("/user/kickInactives")
	public String kickInactiveUsers(@QueryParam("adminKey") String adminKey) {
		if (!adminKey.equals(ADMIN_KEY))
			return getGameResponse(game, new Error(INCORRECT_ADMIN_KEY, 3));
		
		if (game != null){
			Response response = new Response();
			response.setOther(game.kickInactiveUsers());
			return getResponse(response);
		}
		return getGameResponse(game, new Error(NO_GAME, 1));
	}

	@GET
	@Path("/reveal")
	public String move(@QueryParam("userId") String userId) {		
		try {
			game.reveal(userId);
			return getGameResponse(game, null);
		} catch (IpgException e) {
			return getGameResponse(null, new Error(e));
		}
	}

	@GET
	@Path("/move/{direction}")
	public String move(@PathParam("direction") Direction direction, @QueryParam("userId") String userId){
		if (game != null){
			try {
				game.movePlayer(direction, userId);
				return getGameResponse(game, null);
			} catch (IpgException e) {
				return getGameResponse(null, new Error(e));
			}
		}
		return getGameResponse(game, new Error(NO_GAME, 1));
	}

	@POST
	@Path("/user/new")
	public String newUser() {
		if (game != null){
			try {
				String id = game.newUser();
				System.out.println("New user: "+id);
				return getGameResponse(game, null);
			} catch (IpgException e) {
				return getGameResponse(null, new Error(e));
			}
		}
		return getGameResponse(game, new Error(NO_GAME, 1));
	}

	@POST
	@Path("/user/dispose")
	public String disposeUser(@QueryParam("userId") String userId) {
		Response response = new Response();
		if (game != null) {
			User user = game.kickUser(userId);
			response.setOther(user);
			return getResponse(response);
		}
		return getGameResponse(game, new Error(NO_GAME, 1));
	}

	@GET
	@Path("/highscore")
	public String getHighScores() {
		Response response = new Response();
		List<HighScore> highScores;
		try {
			highScores = CloudantLayer.getHighScores();
		} catch (IpgException e) {
			response.setMessage(new Error(e));
			return getResponse(response);
		}
		response.setOther(highScores);
		return getResponse(response);
	}

	@POST
	@Path("/highscore")
	public String addHighScore(HighScore highScore) {
		String response = CloudantLayer.addHighScore(highScore);
		return getGameResponse(null, new Message(response));
	}
	
	private String getGameResponse(Game game, Message message){
		GameResponse response = new GameResponse();
		if (game != null){
			game.syncPublicUsers();
			response.setGame(game);
		}
		if (message != null){
			response.setMessage(message);
		}
		return getResponse(response);
	}
	
	private String getResponse(Response response){
		Gson gson = new Gson();
		return gson.toJson(response);		
	}
	
	private void initAdminKey(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/keys");
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ADMIN_KEY = prop.getProperty("adminKey");
	}
}