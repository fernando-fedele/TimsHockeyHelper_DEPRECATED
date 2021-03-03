package fedelef.hockeyapp.comparators;

import java.util.Comparator;

import fedelef.hockeyapp.models.Player;

public class GoalsPerGameSorter implements Comparator<Player> {
	
    @Override
    public int compare(Player p1, Player p2) {
    	Double gpg1 = p1.getGoalsPerGameValue();
    	Double gpg2 = p2.getGoalsPerGameValue();
        return gpg2.compareTo(gpg1);
    }
}
