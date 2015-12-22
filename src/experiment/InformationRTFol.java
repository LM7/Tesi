package experiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.MainTwitter;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class InformationRTFol {

	public static void main(String[] args) throws FileNotFoundException, TwitterException, IOException {
		FileWriter file = new FileWriter("ProvaFollowingsContro.txt", true);
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("Prova.txt"));
		String line = reader.readLine();
		MainTwitter mt = new MainTwitter();
		Twitter twitter = mt.getTwitter();
		PagableResponseList<User> followingsUser = null;
		long cursor = -1;
		System.out.println("LOADING");
		int j = 0;
		while (line != null) {
			outFile.println("USER: "+line);
			do {
				System.out.println("CURSOR:"+cursor);
				followingsUser = twitter.getFriendsList(line.toString(), cursor, 200);//200
				System.out.println(line.toString());
				j = j + 200;
				for (User utente: followingsUser) {
					outFile.println(utente.getScreenName()); // qui il nome con @
				}
				System.out.println("GETNEXT "+followingsUser.getNextCursor()); //il prossimo cursor
			}
			while ((cursor = followingsUser.getNextCursor()) != 0 && j < 3000); //j < 3000
			line = reader.readLine();
			cursor = -1;
		}
		reader.close();
		file.close();
	}

}
