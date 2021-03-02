package fedelef.hockeyapp.dao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import fedelef.hockeyapp.models.Player;
import fedelef.hockeyapp.models.Team;


public class NHLDataAccessObject {

	public List<Team> getTeams() throws IOException, InterruptedException, JSONException {
		
		var client = HttpClient.newHttpClient();

		var request = HttpRequest.newBuilder(
		       URI.create("https://statsapi.web.nhl.com/api/v1/teams"))
		   .header("accept", "application/json")
		   .build();

		var response = client.send(request, BodyHandlers.ofString());

		
		JSONObject jsonObj = new JSONObject(response.body());
		JSONArray teamsStr = jsonObj.getJSONArray("teams");
		
		List<Team> teams = new ArrayList<Team>();
		JSONObject team;
		List<Player> players;
		
		for (int i = 0; i < teamsStr.length(); i++) {
			team = teamsStr.getJSONObject(i);
			
			players = getPlayers(team.getInt("id"), team.getString("name"));
			
			teams.add(new Team(team.getInt("id"), team.getString("name"), players));
		}
		
		return teams;
	}
	
	public List<Player> getPlayers(int teamID, String teamName) throws IOException, InterruptedException, JSONException {
		
		var client = HttpClient.newHttpClient();

		var request = HttpRequest.newBuilder(
		       URI.create(String.format("https://statsapi.web.nhl.com/api/v1/teams/%d/roster", teamID)))
		   .header("accept", "application/json")
		   .build();

		var response = client.send(request, BodyHandlers.ofString());

		
		JSONObject jsonObj = new JSONObject(response.body());
		JSONArray roster = jsonObj.getJSONArray("roster");
		
		List<Player> players = new ArrayList<Player>();
		JSONObject player;

		
		for (int i = 0; i < roster.length(); i++) {
			player = roster.getJSONObject(i);
			
			//TODO - further limit which players get picked
			if (!player.getJSONObject("position").getString("code").equals("G")) { 
			
			players.add(new Player(
					player.getJSONObject("person").getInt("id"), 
					player.getJSONObject("person").getString("fullName"), 
					player.getInt("jerseyNumber"), 
					player.getJSONObject("position").getString("code"),
					teamName
				));
			
			}
		}
		

		return players;
	}
	
public void addPlayerStats(List<Player> players) throws IOException, InterruptedException, JSONException {
		
		JSONArray splitsArray;
		JSONObject statsObj;
	
		var client = HttpClient.newHttpClient();

		for (Player player : players) {
			
			if (!player.getPosition().equals("G")) {
		
				var request = HttpRequest.newBuilder(
					       URI.create(String.format("https://statsapi.web.nhl.com/api/v1/people/%d/stats?stats=statsSingleSeason&season=20202021", player.getId())))
					   .header("accept", "application/json")
					   .build();
	
				var response = client.send(request, BodyHandlers.ofString());
				
				try {
					splitsArray = new JSONObject(response.body()).getJSONArray("stats").getJSONObject(0).getJSONArray("splits");
					
					// players on the roster may not be active - check if this is something we can fix earlier!
					if (splitsArray.length() != 0) {
						statsObj = new JSONObject(splitsArray.getJSONObject(0), new String[] {"stat"}).getJSONObject("stat");

						player.setStats(
							statsObj.getInt("goals"), 
							statsObj.getInt("games"), 
							statsObj.getInt("assists"), 
							statsObj.getInt("plusMinus"),  
							statsObj.getDouble("shotPct") 
						);
						
						/*
						System.out.println(player.getFullName() + " : " + player.getPosition() + " : " + player.getJerseyNumber() + 
								" - Goals: " + player.getGoals() + " Games Played: " + player.getGamesPlayed() + " Shot Percentage: " + player.getShotPct());		
						*/
					}
					
				} catch (Exception e) {
					System.out.println(player.getPosition() + " failed to get stats");
				}
			}
		}
		

		

		/*
		JSONObject jsonObj = new JSONObject(response.body());
		JSONArray roster = jsonObj.getJSONArray("roster");
		
		List<Player> players = new ArrayList<Player>();
		JSONObject player;
		
		for (int i = 0; i < roster.length(); i++) {
			player = roster.getJSONObject(i);
			players.add(new Player(
					player.getJSONObject("person").getInt("id"), 
					player.getJSONObject("person").getString("fullName"), 
					player.getInt("jerseyNumber"), 
					player.getJSONObject("position").getString("code")
				));
		}
		
		return players;
		*/
	}
}
