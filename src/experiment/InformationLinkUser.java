package experiment;

import java.io.FileNotFoundException;
import java.io.IOException;

import twitter4j.MainTwitter;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import database.TwitterNeo4j;

public class InformationLinkUser {
	public static void main(String[] args) throws FileNotFoundException, TwitterException, IOException {
		MainTwitter mt = new MainTwitter();
		Twitter twitter = mt.getTwitter();
		PagableResponseList<User> followingsUser = null;
		long cursor = -1;
		int j = 0;
		do {
			followingsUser = twitter.getFriendsList("LM791", cursor, 200);//200
			j = j + 200;
			for (User user: followingsUser) {
				System.out.println(user.getScreenName());
			}
			System.out.println("GETNEXT "+followingsUser.getNextCursor()); //il prossimo cursor
		}
		while ((cursor = followingsUser.getNextCursor()) != 0 && j < 3000); 
		
	}

}
