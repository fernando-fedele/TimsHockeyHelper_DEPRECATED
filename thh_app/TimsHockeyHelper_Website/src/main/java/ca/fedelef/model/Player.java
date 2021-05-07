package ca.fedelef.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Player implements Comparable<Player> {

	@Id
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Team team;
	
	private String full_name;
	private int jersey_number;
	private String position;
	
	private int goals;
	private int games_played;
	private int assists;
	private int plus_minus;
	private double shot_pct;
	private int goals_per_3_games;
	private int goals_per_5_games;
	
	public double getGoalsPerGame() {
		return games_played != 0 ? goals / ((double) games_played) : 0;
	}
	
	public String getFormattedName() {
		return full_name + " - " + team.getName() + " - " + position;
	}
	
	@Override
	public int compareTo(Player p) {
		return this.full_name.compareTo(p.getFull_name());
	}
}