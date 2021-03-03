package fedelef.hockeyapp.models;

import java.util.ArrayList;

public class Player implements Comparable<Player> {
	// player info
	private int id;
	private String fullName;
	private int jerseyNumber;
	private String position;
	private String currentTeam;
	
	// stats
	private int goals;
	private int gamesPlayed;
	private int assists;
	private int plusMinus;
	private double shotPct;
	
	//gamestats
	private ArrayList<GameStats> gameStats; // index 0 = latest game, index 4 = oldest game 
	
	public Player(int id, String fullName, int jerseyNumber, String position, String currentTeam) {
		this.id = id;
		this.fullName = fullName;
		this.jerseyNumber = jerseyNumber;
		this.position = position;
		this.currentTeam = currentTeam;
	}

	
	// player info
	public int getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public int getJerseyNumber() {
		return jerseyNumber;
	}

	public String getPosition() {
		return position;
	}
	
	public String getCurrentTeam() {
		return currentTeam;
	}
	
	public String getFormattedName() {
		return fullName + " - " + currentTeam + " - " + position;
	}
	
	// stats
	public void setStats(int goals, int gamesPlayed, int assists, int plusMinus, double shotPct) {
		this.goals = goals;
		this.gamesPlayed = gamesPlayed;
		this.assists = assists;
		this.plusMinus = plusMinus;
		this.shotPct = shotPct;
	}
	
	public int getGoals() {
		return goals;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public int getAssists() {
		return assists;
	}

	public int getPlusMinus() {
		return plusMinus;
	}

	public double getShotPct() {
		return shotPct;
	}
	
	public String getGoalsPerGame() {
		return String.format("%.3f", this.getGoalsPerGameValue());
	}
	
	public double getGoalsPerGameValue() {
		if (this.gamesPlayed == 0) {
			return 0.0;
		}
		return ((double)this.goals) / this.gamesPlayed;
	}
	
	// gamestats
	public void setGameStats(ArrayList<GameStats> gameStats) {
		this.gameStats = gameStats;
	}
	
	public ArrayList<GameStats> getGameStats() {
		return gameStats;
	}
	
	public int getGoalsLast3Games() {
		int totalGoals = 0;
		for (int i = 0; i < 3; i++) {
			totalGoals += gameStats.get(i).getGoals();
		}
		
		return totalGoals;	
	}
	
	public int getGoalsLast5Games() {
		int totalGoals = 0;
		for (int i = 0; i < 5; i++) {
			totalGoals += gameStats.get(i).getGoals();
		}
		
		return totalGoals;	
	}
	
	@Override
	public int compareTo(Player player) {
        return this.getFormattedName().compareTo(player.getFormattedName());
    }
	
	@Override
	public String toString() {
		return this.getFormattedName();
	}

}
