package fedelef.hockeyapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fedelef.hockeyapp.dao.NHLDataAccessObject;
import fedelef.hockeyapp.models.Player;
import fedelef.hockeyapp.models.Team;


@Controller
public class HockeyController {

	private NHLDataAccessObject nhlDao = new NHLDataAccessObject();
	private List<Player> players = new ArrayList<Player>();

	
	@GetMapping("/")
	public String goHome(Model model) throws IOException, InterruptedException, JSONException {
		
		if (players.isEmpty()) {
			List<Team> teams = nhlDao.getTeams();
			
			for (Team team : teams) {
				players.addAll(team.getPlayers());
			}
			
			Collections.sort(players);
		}
		
		model.addAttribute("players", players);
		
		return "index.html";
	}
	
	@PostMapping("/")
	public String checkStats(Model model, @RequestParam(value="pickedPlayers" ,required=false) String[] playerIDs) {
		
		ArrayList<Player> selectedPlayers = getPlayersFromIDs(playerIDs);
		//System.out.println(selectedPlayers);
		
		try {
			nhlDao.addPlayerStats(selectedPlayers);
		} catch (IOException | InterruptedException | JSONException e) {
			e.printStackTrace();
		}
		
		for (Player p : selectedPlayers) {
			System.out.println(p.getFormattedName() + " : " + p.getGoalsPerGame());
		}
		
		model.addAttribute("players", players);
		return "index.html";
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
