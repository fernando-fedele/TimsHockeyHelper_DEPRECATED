package fedelef.hockeyapp.models;

import java.util.List;

public class Team {

	private int id;
	private String name;
	private List<Player> players;
	
	public Team(int id, String name, List<Player> players) {
		this.id = id;
		this.name = name;
		this.players = players;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}
