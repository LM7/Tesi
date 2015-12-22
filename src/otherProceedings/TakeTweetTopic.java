package otherProceedings;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import twitter4j.MainTwitter;
import twitter4j.TwitterException;

public class TakeTweetTopic {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, TwitterException {
		//Database
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("collezione");	

		// svuota database
		BasicDBObject remove = new BasicDBObject();
		collection.remove(remove);
		
		
		MainTwitter twitter = new MainTwitter();
		HashMap<String, ArrayList<String>> tweets = new HashMap<String,ArrayList<String>>();
		String topic = "#saveiturbe";
		String user = "LM791";
		String dataStart = "2015-07-20";
		tweets = twitter.userFollowersOnTopic(topic, user, dataStart); //Tweets riguardo delle parole chiave
		
		/* Con tagger -> HT sono gli #; analizzare gli # se corrispondono a parole chiavi o se con tagme hanno categorie collegate al topic di interesse */
		
		//Provare a salvare la mappa in MongoDB
		
		BasicDBObject document = new BasicDBObject();
		document.put("user", user);

		collection.insert(document);
		
		DBCursor cursor = collection.find();
		int j = 1;
		while (cursor.hasNext()) {
			String s = cursor.next().toString();
			System.out.println(s);
			j++;
		}
		System.out.println("DONE");

		
	}

}
