package ca.fedelef.dao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import ca.fedelef.model.Player;
import ca.fedelef.model.Team;

public class NHLDataAccessObject {

	/* retrieves a list of all active teams in the NHL through the public API */
	public List<Team> getTeams(HttpClient client) throws IOException, InterruptedException, JSONException {
		
		var request = HttpRequest.newBuilder(URI.create("https://statsapi.web.nhl.com/api/v1/teams"))
				.header("accept", "application/json").build();
		var response = client.send(request, BodyHandlers.ofString());

		JSONObject jsonObj = new JSONObject(response.body());
		JSONArray teamsStr = jsonObj.getJSONArray("teams");

		List<Team> teams = new ArrayList<Team>();
		
		JSONObject team;
		for (int i = 0; i < teamsStr.length(); i++) {
			team = teamsStr.getJSONObject(i);
			
			/* UPDATE - Seattle Kraken needs to be excluded */
			if (team.getInt("id") != 55) {
				teams.add(Team.builder()
						.id(team.getInt("id"))
						.name(team.getString("name"))
						.build());
			}
		}

		return teams;
	}
	
	/* sets the list of players with their stat totals for the given team through the public API */
	public List<Player> getPlayers(Team team, HttpClient client) throws IOException, InterruptedException, JSONException {
		
		var request = HttpRequest
				.newBuilder(URI.create(String.format("https://statsapi.web.nhl.com/api/v1/teams/%d/roster", team.getId())))
				.header("accept", "application/json").build();
		var response = client.send(request, BodyHandlers.ofString());

		JSONObject jsonObj = new JSONObject(response.body());
		JSONArray roster = jsonObj.getJSONArray("roster");

		List<Player> players = new ArrayList<Player>();
		JSONObject player;

		for (int i = 0; i < roster.length(); i++) {
			player = roster.getJSONObject(i);

			if (!"G".equals(player.getJSONObject("position").getString("code"))) {
				
				/* UPDATE - recently signed players without jersey numbers need to be handled elegantly */
				int jN;
				try {
					jN = player.getInt("jerseyNumber");
				} catch (Exception E) {
					jN = -1;
				}
				

				Player p = Player.builder()
									.id(player.getJSONObject("person").getInt("id"))
									.full_name(player.getJSONObject("person").getString("fullName"))
									.jersey_number(jN)
									.position(player.getJSONObject("position").getString("code"))
									.team(team)
									.build();
				
				players.add(p);
			}
		}

		return players;
	}
	
	/* sets the season-long stats for a player through the public API */
	public void setPlayerStats(List<Player> players, HttpClient client) throws IOException, InterruptedException, JSONException {

		JSONArray splitsArray;
		JSONObject statsObj;

		for (Player player : players) {
		
			var request = HttpRequest.newBuilder(URI.create(String.format(
					"https://statsapi.web.nhl.com/api/v1/people/%d/stats?stats=statsSingleSeason&season=20202021",
					player.getId()))).header("accept", "application/json").build();
			var response = client.send(request, BodyHandlers.ofString());
	
			try {
				splitsArray = new JSONObject(response.body()).getJSONArray("stats").getJSONObject(0)
						.getJSONArray("splits");
	
				if (splitsArray.length() != 0) {
					statsObj = new JSONObject(splitsArray.getJSONObject(0), new String[] { "stat" })
							.getJSONObject("stat");
	
					
					player.setGoals(statsObj.getInt("goals"));
					player.setGames_played(statsObj.getInt("games"));
					player.setAssists(statsObj.getInt("assists"));
					player.setPlus_minus(statsObj.getInt("plusMinus"));
					player.setShot_pct(statsObj.getDouble("shotPct"));
				}
	
			} catch (Exception e) {
				System.out.println(player.getPosition() + " failed to get stats");
			}
		}
	}
	
	/* sets the previous game stats (last 3 games and last 5 games) for a player through the public API */
	public void setRecentGameStats(List<Player> players, HttpClient client) throws IOException, InterruptedException, JSONException {

		for (Player player : players) {
			var request = HttpRequest.newBuilder(URI.create(String.format(
					"https://statsapi.web.nhl.com/api/v1/people/%d/stats?stats=gameLog&season=20202021", player.getId())))
					.header("accept", "application/json").build();
			var response = client.send(request, BodyHandlers.ofString());
	
			JSONArray jsonSplits = new JSONObject(response.body()).getJSONArray("stats").getJSONObject(0)
					.getJSONArray("splits");
	
			int goals_last_3_games = 0;
			int goals_last_5_games = 0;
			int temp_goals;
			
			for (int i = 0; i < 5; i++) {
				try {
					temp_goals = jsonSplits.getJSONObject(i).getJSONObject("stat").getInt("goals");
				} catch (Exception e) {
					temp_goals = 0;
				}
				
				if (i < 3) {
					goals_last_3_games += temp_goals;
				}
				goals_last_5_games += temp_goals;
				
				
				/*
				try {
					gameStats.add(new GameStats(jsonSplits.getJSONObject(i).getString("date"),
							jsonSplits.getJSONObject(i).getJSONObject("stat").getInt("goals"),
							jsonSplits.getJSONObject(i).getJSONObject("stat").getInt("shots"),
							jsonSplits.getJSONObject(i).getJSONObject("stat").getString("timeOnIce")));
				} catch (Exception e) {
					gameStats.add(new GameStats("N/A", 0, 0, "00:00"));
				}
				*/
			}
	
			player.setGoals_per_3_games(goals_last_3_games);
			player.setGoals_per_5_games(goals_last_5_games);
		}
	}
}
