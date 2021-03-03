package fedelef.hockeyapp.comparators;

import java.util.Comparator;

import fedelef.hockeyapp.models.Player;

public class FiveGamesGoalsSorter implements Comparator<Player> {
	
    @Override
    public int compare(Player p1, Player p2) {
    	Integer goals1 = p1.getGoalsLast5Games();
    	Integer goals2 = p2.getGoalsLast5Games();
        return goals2.compareTo(goals1);
    }
}
