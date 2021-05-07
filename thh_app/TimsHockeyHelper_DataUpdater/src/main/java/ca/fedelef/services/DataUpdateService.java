package ca.fedelef.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.fedelef.dao.NHLDataAccessObject;
import ca.fedelef.model.Player;
import ca.fedelef.model.Team;
import ca.fedelef.repositories.PlayerRepository;
import ca.fedelef.repositories.TeamRepository;

@Service
public class DataUpdateService {
	
	@Autowired
	private PlayerRepository pRepo;
	
	@Autowired
	private TeamRepository tRepo;
	
	public void updateDatabase() throws IOException, InterruptedException, JSONException {
		
		List<Player> players = new ArrayList<Player>();
		NHLDataAccessObject nhlDao = new NHLDataAccessObject();
		HttpClient client = HttpClient.newHttpClient();
		
		/* step 1: completely empty database */
		tRepo.deleteAll();
		pRepo.deleteAll();
		
		/* step 2: get all teams */
		List<Team> teams = nhlDao.getTeams(client);
		
		/* step 3: get all players for each team */
		List<Player> tempPlayers;
		for (Team team : teams) {
			tempPlayers = nhlDao.getPlayers(team, client);
			team.setPlayers(tempPlayers);
			players.addAll(tempPlayers);
		}
		
		/* step 4: get additional game data for each player */
		nhlDao.setPlayerStats(players, client);
		nhlDao.setRecentGameStats(players, client);
		
		/* step 5: add all entries to database (team and player tables) */
		pRepo.saveAll(players);
		
		/* step 6: update */
		Date today = new Date();
		
		String fn = "/Users/fernandofedele/thh_app/java_log.txt";
		FileWriter fw = new FileWriter(fn, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(today.toString() + '\n' + "Players successfully added to db: " + pRepo.count() + '\n' + '\n' );
		
		bw.close();
		fw.close();
	}
}
