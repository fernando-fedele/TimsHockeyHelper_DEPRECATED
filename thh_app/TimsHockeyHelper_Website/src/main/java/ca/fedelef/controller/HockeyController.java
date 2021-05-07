package ca.fedelef.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import ca.fedelef.comparators.FiveGamesGoalsSorter;
import ca.fedelef.comparators.GoalsPerGameSorter;
import ca.fedelef.comparators.ThreeGamesGoalsSorter;
import ca.fedelef.comparators.TotalGoalsSorter;
import ca.fedelef.model.Player;
import ca.fedelef.repositories.PlayerRepository;


@Controller
@SessionAttributes("group")
public class HockeyController {
	
	@Autowired
	private PlayerRepository pRepo;

	@ModelAttribute("group")
    public List<Player> setObj() {
		return new ArrayList<Player>();
    }
	
	private List<Player> players = new ArrayList<Player>();


	@GetMapping("/")
	public String goHome(Model model, @ModelAttribute("group") List<Player> group) throws IOException, InterruptedException, JSONException {
		
		if (players.isEmpty()) {	
			players = (List<Player>) pRepo.findAll();
			Collections.sort(players);
		}

		model.addAttribute("players", players);
		model.addAttribute("group", group);

		return "index.html";
	}
	
	
	@PostMapping("/")
	public String checkStats(Model model, @ModelAttribute("group") List<Player> group, @RequestParam(value = "pickedPlayers", required = false) String[] playerIDs) {

		if (playerIDs == null || playerIDs.length == 0 || players.isEmpty()) {
			return "redirect:/";
		}
		
		ArrayList<Player> selectedPlayers = getPlayersFromIDs(playerIDs);
		for (Player p : selectedPlayers) {
			if (!group.contains(p)) {
				group.add(p);
			}
		}

		model.addAttribute("players", players);
		return "index.html";
	}
	
	
	@GetMapping("/removePlayer/{id}")
	public String removePlayer(Model model, @ModelAttribute("group") List<Player> group, @PathVariable int id) {

		for (Player p : group) {
			if (p.getId() == id) {
				group.remove(p);
				break;
			}
		}

		return "redirect:/";
	}

	
	@GetMapping("/clearAll")
	public String clearGroup(Model model, @ModelAttribute("group") List<Player> group) {
		group.clear();
		return "redirect:/";
	}
	

	@GetMapping("/generatePicks")
	public String generatePicks(Model model, @ModelAttribute("group") List<Player> group) {

		if (players.isEmpty() || group == null || group.isEmpty()) {
			return "redirect:/";
		}
		
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
