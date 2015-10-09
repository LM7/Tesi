package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pos.Tagger;
import pos.TweetTextMain;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class TakeDateTopic {
	
	public static void main(String[] args) throws ParseException, IOException {
		FileWriter file = new FileWriter("RightTweets.txt"); //per accodare-> true
		PrintWriter outRightTweets = new PrintWriter(file);
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("collezione");
		DBCursor cursor = collection.find();
		System.out.println( "NUMERO USERS "+(collection.count()-1) );
		Iterator<DBObject> itrc = cursor.iterator();
		itrc.next();
		DBObject obj;
		DBObject obj2;
		int lungTweet;
		String user;
		String data;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tweet;
		TweetTextMain tweetext = new TweetTextMain();
		List<String> hashtags;
		Tagger tagger = new Tagger();
		ArrayList<String> tweetWithTags;
		long numeroUsers = (collection.count()-2 );
		boolean topic;
		while (itrc.hasNext()) {
			int j = 0;
			while ( j <= numeroUsers ) {
				obj = (DBObject)itrc.next();
				user = obj.get("user"+j).toString();
				System.out.println("USER "+user+" NUMERO "+j+" SU "+ numeroUsers );
				int i = 0;
				lungTweet = obj.toMap().size()-3;
				while ( i <=  lungTweet ) {
					System.out.println("TWEET NUMERO "+i+" SU "+ lungTweet);
					obj2 = (DBObject) obj.get("tweet"+i);
					data = obj2.get("date").toString();
					//qui farò le operazioni sulle date
					//Date date = sdf.parse(data);
					tweet = obj2.get("text").toString();
					//qui farò le operazioni sui tweet
					topic = false;
					hashtags = tweetext.tweetTextHashTags(tweet); //lista di hashtags
					tweetWithTags = tagger.taggerNlp(tweet); //tweet taggato con gli elementi di tagger
					for (String stringa: hashtags) {
						if (stringa.equalsIgnoreCase("Volkswagen") || stringa.equalsIgnoreCase("VolkswagenScandal") ) {
							topic = true;
						}
					}
					for (String stringa: tweetWithTags) {
						if (stringa.contains("NNP:") && stringa.contains("Volkswagen")) {
							topic = true;
						}
					}
					if ( !topic ) {
						//se il topic non corrisponde elimino il tweet dall'user
						DBObject query = new BasicDBObject("user"+j, user);
						DBObject update = new BasicDBObject();
					    update.put("$unset", new BasicDBObject("tweet"+i,""));
					    collection.update(query, update);
					}
					else {
						outRightTweets.println(data);
						outRightTweets.println(tweet);
						outRightTweets.println();
					}
					i++;
				}
				j++;
			}
		}
		
		outRightTweets.close();
		System.out.println("DONE");

	}
		
}

