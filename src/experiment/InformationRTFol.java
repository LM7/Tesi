package experiment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class InformationRTFol {

	public static void main(String[] args) throws FileNotFoundException, TwitterException, IOException {
		MainTwitter mt = new MainTwitter();
		Twitter twitter = mt.getTwitter();
		ResponseList<Status> stati  = null;
		stati = mt.tweetsOfUser("JeremyClarkson", 20);
		Status statoImp = null;
		//int cont = 0;
		for (Status stato: stati) {
			if (stato.getText().contains("I'm very happy")) {
				statoImp = stato;
				//cont++;
			}
		}
		long idStatus = statoImp.getId();
		long cursor =-1;
		//int j = 0;
		IDs ids;
		ArrayList<String> users = new ArrayList<String>();
		do {
			System.out.println("CURSOR:"+cursor);
			ids = twitter.getRetweeterIds(idStatus, cursor);
			//j = j + 200;
			for (long id : ids.getIDs()) {
	              User utente = twitter.showUser(id);
	              users.add(utente.getScreenName());
	        }
		}
		while ( ((cursor = ids.getNextCursor()) != 0) ); //&& j < 3000
		System.out.println(users.size());
		
		ArrayList<String> followings = new ArrayList<String>();
		int v = 0;
		int ny = 0;
		for (String user: users) {
			followings = mt.followingsOfUser(user);
			for (String following : followings) {
				System.out.println(following);
				if (following.contains("Volkswagen")) {
					v++;
				}
				else if (following.contains("nytimes")) {
					ny++;
				}
			}
		}
		System.out.println(v+ " vs "+ny);
	}

}
