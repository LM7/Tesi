package database;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class OperationMongoDB {

	public static void main(String[] args) throws JSONException, FileNotFoundException {
		PrintWriter outDB = new PrintWriter("");
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection(""); //collezione
		DBCursor cursor = collection.find();
		/*Svuota database
		BasicDBObject remove = new BasicDBObject();
		collection.remove(remove);*/
		//cursor.sort(new BasicDBObject("_id", 1)); //ordino il db per chiave, quindi per inserimento
		while (cursor.hasNext()) {
			//BasicDBList object = (BasicDBList) cursor.next().toMap().get("followers"); //quanti sono gli users
			//System.out.println(object.toString());
			//System.out.println(object.size());
			String s = cursor.next().toString(); //stampa database
			outDB.println(s);
			System.out.println(s);
			
			//per eliminare....
			/*try {
				int i = 0;
				for (Object objects: object) {

					System.out.println(objects.toString());
					BasicDBObject documentDel = new BasicDBObject();
					documentDel.put("user"+i, objects.toString());
					collection.remove(documentDel);


					i++;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}*/
			
			
		}
		outDB.close();
		
		
		// elimina tutto l'user
		/*BasicDBObject documentDel = new BasicDBObject();
		documentDel.put("user", "Lollo");
		collection.remove(documentDel);*/
		
		//cosa inserire
		/*String json = "{'user' : 'Lollo'," +
				"'tweet0' : {'data' : 99, 'language' : 'en', 'testo' : 'bellone'},"
				+ "'tweet1':{'data' : 100, 'language' : 'en', 'testo' : 'cdcdecejbkdwdw'}}}";

		DBObject dbObject = (DBObject)JSON.parse(json);

		collection.insert(dbObject);*/
		
		
		//accedere
		/*DBCursor cursorProva = collection.find();
		Iterator<DBObject> itrc = cursor.iterator();
		itrc.next();
		while (itrc.hasNext()) {
			DBObject obj = (DBObject)itrc.next().get("tweet0");
			String s = obj.get("testo").toString();
			System.out.println(s);
		}*/
		
		//aggiornare field
		/*BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.append("user", "Lollo");
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("tweet0", ""));
		collection.updateMulti(searchQuery, updateQuery);*/
		
		//eliminare field
		/*DBObject query = new BasicDBObject("user", "Lollo");
		DBObject update = new BasicDBObject();
	    update.put("$unset", new BasicDBObject("tweet1",""));
	    collection.update(query, update);*/
		
		

		/*System.out.println("DATABASE FINALE");
		DBCursor cursorEnd = collection.find();
		while (cursorEnd.hasNext()) {
			String s = cursorEnd.next().toString();
			System.out.println(s);
		}*/
	}

}
