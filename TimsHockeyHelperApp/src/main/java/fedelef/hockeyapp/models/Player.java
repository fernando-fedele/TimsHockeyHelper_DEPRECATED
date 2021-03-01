package fedelef.hockeyapp.models;


public class Player implements Comparable<Player> {
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
	
	public Player(int id, String fullName, int jerseyNumber, String position, String currentTeam) {
		this.id = id;
		this.fullName = fullName;
		this.jerseyNumber = jerseyNumber;
		this.position = position;
		this.currentTeam = currentTeam;
	}

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
		double gpg = ((double)this.goals) / this.gamesPlayed;
		return String.format("%.3f", gpg);
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
