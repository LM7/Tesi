package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import twitter4j.MainTwitter;
import twitter4j.TwitterException;

/***
 * Classe per recuperare il maggior numero di followers di una pagina e salvarli nel DB
 * @author lorenzomartucci
 *
 */

public class TakeUsers {

	public static void main(String[] args) throws TwitterException, FileNotFoundException, UnsupportedEncodingException {
		PrintWriter outUsers = new PrintWriter("Users.txt", "UTF-8");
		MainTwitter mt = new MainTwitter();
		String pagina = "LM791";
		int numFollowers = 50;
		ArrayList<String> followers = mt.followersOfUser(pagina, numFollowers);
		/*DATABASE*/
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
		
		
		BasicDBList lista = new BasicDBList();
		for (String follower: followers) {
			lista.add(follower);
		}
		BasicDBObject document = new BasicDBObject("followers", lista);
		collection.insert(document);
		
		
		
		DBCursor cursor = collection.find();
		//System.out.println("DATABASE");
		while (cursor.hasNext()) {
			//String s = cursor.next().toString();
			BasicDBList e = (BasicDBList) cursor.next().get("followers");
			int lunghezza = e.size();
			for (int j = 0; j < lunghezza  ; j++) {
				//System.out.println(e.get(j));
				outUsers.println(j);
				outUsers.println(e.get(j));
			}
		}
		
		outUsers.close();
		
		System.out.println("DONE");

	}

}
