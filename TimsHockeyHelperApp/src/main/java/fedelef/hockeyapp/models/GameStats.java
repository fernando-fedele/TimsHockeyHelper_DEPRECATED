package fedelef.hockeyapp.models;


public class GameStats {
	
	private String date;
	private int goals;
	private int shots;
	private String timeOnIce;
	
	public GameStats(String date, int goals, int shots, String timeOnIce) {
		this.date = date;
		this.goals = goals;
		this.shots = shots;
		this.timeOnIce = timeOnIce;
	}

	public String getDate() {
		return date;
	}

	public int getGoals() {
		return goals;
	}

	public int getShots() {
		return shots;
	}
	
	public String getShotPct() {
		if (shots == 0) {
			return "0.000";
		}
		return String.format("%.3f", (double) goals / shots);
	}

	public String getTimeOnIce() {
		return timeOnIce;
	}
}