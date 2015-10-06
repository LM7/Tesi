package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

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
		int numTweet = 100;
		String language;
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
		/*DBCursor cursorPrev = collection.find();
		System.out.println("DATABASE ALL'INIZO");
		while (cursorPrev.hasNext()) {
			String s = cursorPrev.next().toString();
			System.out.println(s);
		}*/
		DBCursor cursor = collection.find();
		while (cursor.hasNext()  ) {
			BasicDBList e = (BasicDBList) cursor.next().get("followers");
			int lunghezza = e.size();
			System.out.println("LUNGHEZZA :"+lunghezza);
			for (int j = 0; j < lunghezza  ; j++) {
				user = e.get(j).toString();
				try {
					stati = mt.tweetsOfUser(user, numTweet);
					System.out.println("USER: "+user+" NUMERO: "+j);
					System.out.println("NUMERO TWEET :"+stati.size());
					String json = "{'user"+j+"' : '"+user+"',";
					outTweets.println(user);
					int i = 0;
					for (Status stato: stati) {
						System.out.println("TWEET NUMERO: "+i);
						if (i!=0) { //se non è il primo ci vuole la virgola
							json = json + ",";
						}
						language = stato.getLang();
						date = stato.getCreatedAt();
						String dataStringa = sdf.format(date);
						tweet = stato.getText();
						if (TextCatMain.lang(tweet).equals("EN")) {
							outTweets.println("ENGLISH FOR TEXTCAT");
						}
						else {
							outTweets.println("NO ENGLISH FOR TEXTCAT");
						}
						tweet = tweet.replaceAll("\"", "^");
						tweet = tweet.replaceAll("\'", "_");
						outTweets.println(i);
						json = json + "'tweet"+i+"' : {'language' : '"+language+"', 'date' : '"+dataStringa+"', 'text' : '"+tweet+"'}";
						outTweets.println(language);
						outTweets.println(dataStringa);
						outTweets.println(tweet);
						i++;
					}
					json = json + "}}" ;
					DBObject dbObject = (DBObject)JSON.parse(json);
					outTweets.println();
					collection.insert(dbObject);
					json = "";
					
					
				} catch (Exception e2) {
					System.out.println("ERRORE");
					System.out.println(e2.getMessage());
				}
			}
		}
		
		
		/*DBCursor cursorEnd = collection.find();
		System.out.println("DATABASE ALLA FINE");
		while (cursorEnd.hasNext()) {
			String s = cursorEnd.next().toString();
			System.out.println(s);
		}*/
		
		outTweets.close();
		System.out.println("THE END");
	}
	

}
