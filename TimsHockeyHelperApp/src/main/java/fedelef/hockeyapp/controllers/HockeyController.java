package fedelef.hockeyapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fedelef.hockeyapp.comparators.FiveGamesGoalsSorter;
import fedelef.hockeyapp.comparators.GoalsPerGameSorter;
import fedelef.hockeyapp.comparators.ThreeGamesGoalsSorter;
import fedelef.hockeyapp.comparators.TotalGoalsSorter;
import fedelef.hockeyapp.dao.NHLDataAccessObject;
import fedelef.hockeyapp.models.Player;
import fedelef.hockeyapp.models.Team;

@Controller
public class HockeyController {

	private NHLDataAccessObject nhlDao = new NHLDataAccessObject();
	private List<Player> players = new ArrayList<Player>();
	private List<Player> group = new ArrayList<Player>();

	@GetMapping("/")
	public String goHome(Model model) throws IOException, InterruptedException, JSONException {

		if (players.isEmpty()) {
			List<Team> teams = nhlDao.getTeams();

			for (Team team : teams) {
				players.addAll(team.getPlayers());
			}
		}

		Collections.sort(players);

		// fixes bug where there are 2 of every player in players
		if (players.get(0).getId() == players.get(1).getId()) {
			players.clear();
			return "redirect:/";
		}

		model.addAttribute("players", players);
		model.addAttribute("group", group);

		return "index.html";
	}

	@PostMapping("/")
	public String checkStats(Model model, @RequestParam(value = "pickedPlayers", required = false) String[] playerIDs) {

		if (playerIDs == null || playerIDs.length == 0 || players.isEmpty()) {
			return "redirect:/";
		}

		ArrayList<Player> selectedPlayers = getPlayersFromIDs(playerIDs);

		try {
			nhlDao.addPlayerStats(selectedPlayers);
		} catch (IOException | InterruptedException | JSONException e) {
			e.printStackTrace();
		}

		for (Player p : selectedPlayers) {

			if (p.getGameStats() == null) {
				try {
					nhlDao.setGameStats(p);
				} catch (IOException | InterruptedException | JSONException e) {
					e.printStackTrace();
				}
			}

			if (!group.contains(p)) {
				group.add(p);
			}
		}

		model.addAttribute("players", players);
		model.addAttribute("group", group);

		return "index.html";
	}

	@GetMapping("/removePlayer/{id}")
	public String removePlayer(@PathVariable int id) {

		for (Player p : group) {
			if (p.getId() == id) {
				group.remove(p);
				break;
			}
		}

		return "redirect:/";
	}

	@GetMapping("/clearAll")
	public String clearGroup() {
		group.clear();
		return "redirect:/";
	}

	@GetMapping("/generatePicks")
	public String generatePicks(Model model) {

		ArrayList<Player> totalGoals = new ArrayList<Player>(group);
		totalGoals.sort(new TotalGoalsSorter());

		ArrayList<Player> goalsPerGame = new ArrayList<Player>(group);
		goalsPerGame.sort(new GoalsPerGameSorter());

		ArrayList<Player> top3Games = new ArrayList<Player>(group);
		top3Games.sort(new ThreeGamesGoalsSorter());

		ArrayList<Player> top5Games = new ArrayList<Player>(group);
		top5Games.sort(new FiveGamesGoalsSorter());

		model.addAttribute("totalGoals", totalGoals);
		model.addAttribute("goalsPerGame", goalsPerGame);
		model.addAttribute("top3Games", top3Games);
		model.addAttribute("top5Games", top5Games);

		return "rankedPicks.html";
	}

	/*
	 * Helper method to get all matching players from a list of IDs
	 */
	public ArrayList<Player> getPlayersFromIDs(String[] playerIDs) {

		ArrayList<Player> selectedPlayers = new ArrayList<Player>();
		ArrayList<Integer> pickedIDs = new ArrayList<Integer>();

		for (String id : playerIDs) {
			pickedIDs.add(Integer.valueOf(id));
		}

		for (Player p : players) {
			if (pickedIDs.contains(p.getId())) {
				selectedPlayers.add(p);
			}
		}

		return selectedPlayers;
	}
}
