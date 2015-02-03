package com.ibm.ipg.dal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ibm.ipg.game.exception.IpgException;

public class CloudantLayer {

	private static final String URL_BASE = "http://b4b9bddd-2a19-4df4-9593-d9f16c548ba2-bluemix.cloudant.com";

	public static String addHighScore(HighScore highScore) {
		try {
			URL url = new URL(URL_BASE+"/highscore/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic YjRiOWJkZGQtMmExOS00ZGY0LTk1OTMtZDlmMTZjNTQ4YmEyLWJsdWVtaXg6NDZlZDMyY2RiZDA2NmFlNzJkNjNjZTNjZWZiMTRmMzhmMGEwOWEyYjQ3OWZkODNkOTAxNjQ2MjBkNzU3OWQ0OQ==");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			String payload = new Gson().toJson(highScore, HighScore.class);			
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (payload);
			wr.flush ();
			wr.close ();
			String jsonResponse = stringFromInputStream(connection.getInputStream());
			return jsonResponse;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<HighScore> getHighScores() throws IpgException{
		try {
			URL url = new URL(URL_BASE+"/highscore/_find");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic YjRiOWJkZGQtMmExOS00ZGY0LTk1OTMtZDlmMTZjNTQ4YmEyLWJsdWVtaXg6NDZlZDMyY2RiZDA2NmFlNzJkNjNjZTNjZWZiMTRmMzhmMGEwOWEyYjQ3OWZkODNkOTAxNjQ2MjBkNzU3OWQ0OQ==");
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			String payload = "{\"selector\":{\"score\":{\"$gt\":0}},\"fields\":[\"nickName\",\"score\"],\"sort\":[{\"score\":\"desc\"}],\"limit\":10,\"skip\":0}";
			wr.writeBytes (payload);
			wr.flush ();
			wr.close ();
			String jsonResponse = stringFromInputStream(connection.getInputStream());			
			String jsonHighScores = new JsonParser().parse(jsonResponse).getAsJsonObject().get("docs").toString();
			System.out.println(jsonHighScores);
			Type type = new TypeToken<List<HighScore>>(){}.getType();
			List<HighScore> highScores = new Gson().fromJson(jsonHighScores, type);
			return highScores;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			throw new IpgException("No internet connection, cannot load high scores.", 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String stringFromInputStream(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
}
