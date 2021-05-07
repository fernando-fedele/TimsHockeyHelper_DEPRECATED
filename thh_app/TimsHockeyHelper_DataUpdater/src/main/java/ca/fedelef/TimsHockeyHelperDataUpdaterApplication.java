package ca.fedelef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.fedelef.services.DataUpdateService;

@SpringBootApplication
public class TimsHockeyHelperDataUpdaterApplication implements CommandLineRunner {

	@Autowired
	private DataUpdateService dataUpdateService;
	
	public static void main(String[] args) {
		SpringApplication.run(TimsHockeyHelperDataUpdaterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dataUpdateService.updateDatabase();
		
	}
}
