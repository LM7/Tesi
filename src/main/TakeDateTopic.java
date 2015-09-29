package main;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class TakeDateTopic {
	
	public static void main(String[] args) {
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
		String tweet;
		while (itrc.hasNext()) {
			DBObject obj = (DBObject)itrc.next();
			HashMap<String,String> mappa = (HashMap<String, String>) obj.toMap();
			int j = 0;
			for (String stringa: mappa.keySet()) {
				System.out.println(stringa);
				if (stringa.equals("user"+i)) {
					user = mappa.get(stringa);
					System.out.println(user);
				}
				if (stringa.equals("date"+j)) {
					data = mappa.get(stringa);
					System.out.println(data);
					//qui farò le operazioni sulle date
				}
				if (stringa.equals("tweet"+j)) {
					tweet = mappa.get(stringa);
					System.out.println(tweet);
					j++;
					//qui farò le operazioni sui tweet
				}
				//In seguito salverò user, data e tweet di ciò che supererà i "test"
			}
			i++;
		}

		System.out.println("DONE");

	}
		
}

