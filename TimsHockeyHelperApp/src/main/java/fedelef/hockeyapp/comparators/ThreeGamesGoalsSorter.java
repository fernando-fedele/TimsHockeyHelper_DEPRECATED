package fedelef.hockeyapp.comparators;

import java.util.Comparator;

import fedelef.hockeyapp.models.Player;

public class ThreeGamesGoalsSorter implements Comparator<Player> {
	
    @Override
    public int compare(Player p1, Player p2) {
    	Integer goals1 = p1.getGoalsLast3Games();
    	Integer goals2 = p2.getGoalsLast3Games();
        return goals2.compareTo(goals1);
    }
}
