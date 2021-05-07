package ca.fedelef.comparators;

import java.util.Comparator;

import ca.fedelef.model.Player;


public class ThreeGamesGoalsSorter implements Comparator<Player> {
	
	@Override
    public int compare(Player p1, Player p2) {
		Integer goals1 = p1.getGoals_per_3_games();
		Integer goals2 = p2.getGoals_per_3_games();
		return goals2.compareTo(goals1);
    }
}
