package main;

import java.io.IOException;
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
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("collezione");
		DBCursor cursor = collection.find();
		Iterator<DBObject> itrc = cursor.iterator();
		itrc.next();
		int i = 0; //per tenere il conto degli user
		String user;
		String data;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tweet;
		TweetTextMain tweetext = new TweetTextMain();
		List<String> hashtags;
		Tagger tagger = new Tagger();
		ArrayList<String> tweetWithTags;
		while (itrc.hasNext()) {
			DBObject obj = (DBObject)itrc.next();
			HashMap<String,String> mappa = (HashMap<String, String>) obj.toMap();
			int j = 0;
			for (String stringa: mappa.keySet()) {
				if (stringa.equals("user"+i)) {
					user = mappa.get(stringa);
				}
				if (stringa.equals("date"+j)) {
					data = mappa.get(stringa);
					//qui farò le operazioni sulle date
					Date date = sdf.parse(data); // data in "Date" in modo da poter fare operazioni after, before...
				}
				if (stringa.equals("tweet"+j)) {
					tweet = mappa.get(stringa);
					j++;
					//qui farò le operazioni sui tweet
					hashtags = tweetext.tweetTextHashTags(tweet); //lista di hashtags
					tweetWithTags = tagger.taggerNlp(tweet); //tweet taggato con gli elementi di tagger
				}
				/*In seguito salverò user, data e tweet di ciò che supererà i "test",
				andando a modificare il db?! Potrei eliminare la roba inutile o salvarli in altro modo...*/
			}
			i++;
		}

		System.out.println("DONE");

	}
		
}

