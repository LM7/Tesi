package experiment;

import java.io.FileNotFoundException;
import java.io.IOException;

import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import database.TwitterNeo4j;

public class InformationLinkUser {

	public static void main(String[] args) throws FileNotFoundException, TwitterException, IOException {
		TwitterNeo4j neo = new TwitterNeo4j();
		MainTwitter mt = new MainTwitter();
		ResponseList<Status> stati;
		stati = mt.tweetsOfUser("KateBiting", 1 );
		stati.clear();
		ResponseList<Status> statiLM;
		ResponseList<Status> statiFG;
		statiLM = mt.tweetsOfUser("LM791", 10 );
		statiFG = mt.tweetsOfUser("FlavioGargioli", 10 );
		Status statoLM, statoFG;
		for (Status stato: statiLM) {
			if (stato.getText().contains("Forza Roma")) {
				System.out.println("DA ME");
				statoLM = stato;
				stati.add(statoLM);
			}
		}
		for (Status stato: statiFG) {
			if (stato.getText().contains("Francesco Totti")) {
				System.out.println("DA FLAVIO");
				statoFG = stato;
				stati.add(statoFG);
			}
		}
		
		for (Status stato: stati) {
			System.out.println("DENTRO CREA NEO");
			neo.newStatus(stato);
		}
	}

}
