package ca.fedelef.comparators;

import java.util.Comparator;
import ca.fedelef.model.Player;

public class TotalGoalsSorter implements Comparator<Player> {
	
	@Override
	public int compare(Player p1, Player p2) {
		Integer goals1 = p1.getGoals();
		Integer goals2 = p2.getGoals();
		return goals2.compareTo(goals1);
    }
}