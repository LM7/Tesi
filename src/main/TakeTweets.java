package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import language.TextCatMain;

import org.omg.Messaging.SyncScopeHelper;

import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

/***
 * Classe per recuperare i tweet degli utenti all'interno del database
 * @author lorenzomartucci
 *
 */

public class TakeTweets {

	public static void main(String[] args) throws FileNotFoundException, TwitterException, IOException {
		PrintWriter outTweets = new PrintWriter("Tweets.txt", "UTF-8");
		MainTwitter mt = new MainTwitter();
		ResponseList<Status> stati = null;
		int numTweet = 10;
		String language;
		Date date;
		String tweet;
		String user;
		// NON svuotare il db
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("collezione");	
		DBCursor cursorPrev = collection.find();
		System.out.println("DATABASE ALL'INIZO");
		while (cursorPrev.hasNext()) {
			String s = cursorPrev.next().toString();
			System.out.println(s);
		}
		DBCursor cursor = collection.find();
		while (cursor.hasNext()  ) {
			BasicDBList e = (BasicDBList) cursor.next().get("followers");
			int lunghezza = e.size();
			System.out.println("LUNGHEZZA :"+lunghezza);
			for (int j = 0; j < lunghezza  ; j++) {
				user = e.get(j).toString();
				System.out.println("USER: "+user);
				try {
					stati = mt.tweetsOfUser(user, numTweet);
					System.out.println("NUMERO TWEET :"+stati.size());
					BasicDBObject document = new BasicDBObject();
					document.put("user", user);
					outTweets.println(user);
					int i = 0;
					for (Status stato: stati) {
						language = stato.getLang();
						date = stato.getCreatedAt();
						tweet = stato.getText();
						/*if (TextCatMain.lang(tweet).equals("EN")) {
							// il tweet è in inglese, allora lo inserisco
						}*/
						outTweets.println(i);
						document.put("language"+i, language);
						outTweets.println(language);
						document.put("date"+i, date);
						outTweets.println(date);
						document.put("tweet"+i, tweet);
						outTweets.println(tweet);
						i++;
					}
					outTweets.println();
					collection.insert(document);
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			}
		}
		
		
		DBCursor cursorEnd = collection.find();
		System.out.println("DATABASE ALLA FINE");
		while (cursorEnd.hasNext()) {
			String s = cursorEnd.next().toString();
			System.out.println(s);
		}
		
		outTweets.close();
		System.out.println("THE END");
	}
	

}
