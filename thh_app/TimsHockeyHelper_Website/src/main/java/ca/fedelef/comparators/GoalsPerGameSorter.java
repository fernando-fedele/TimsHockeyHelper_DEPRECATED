package ca.fedelef.comparators;

import java.util.Comparator;

import ca.fedelef.model.Player;

public class GoalsPerGameSorter implements Comparator<Player> {
	
	@Override
	public int compare(Player p1, Player p2) {
		Double gpg1 = p1.getGoalsPerGame();
		Double gpg2 = p2.getGoalsPerGame();
		return gpg2.compareTo(gpg1);
    }
}
