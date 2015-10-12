package query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class SaveOnDB {
	
	public static String deleteURL(String stringa) {
		if (stringa.contains("http")) {
			String[] splits;
			splits = stringa.split(" ");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].startsWith("http")) {
					splits[i] = "";
				}
			}
			stringa = "";
			for (int j = 0; j <splits.length; j++) {
				if (j == 0) {
					stringa = splits[j];
				}
				else {
					stringa = stringa + " " +splits[j];
				}
			}
		}
		return stringa;
	}

	public static void main(String[] args) throws IOException {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("streamCollection");
		// svuota database
		BasicDBObject remove = new BasicDBObject();
		collection.remove(remove);
		BufferedReader reader = new BufferedReader(new FileReader("StreamQueryTweet.txt"));
		String line = reader.readLine();
		String json = "";
		String tweet;
		String[] splits;
		boolean insert = false;
		DBObject dbObject;
		while (line != null) {
			line = deleteURL(line);
			if (line.startsWith("USER:")) {
				json = "";
				splits = line.split(" ");
				json = "{'user' : '"+splits[1]+"',";
			}
			else {
				if (line.startsWith("LINGUA:")) {
					splits = line.split(" ");
					json = json + "'language' : '"+splits[1]+"',";
				}
				else {
					if (line.matches("\\d{4}-\\d{2}-\\d{2}")) {
						json = json + "'date' : '"+line.toString()+"',";
					}
					else if (!(line.equals(""))) {
						tweet = line.toString();
						tweet = tweet.replaceAll("\"", "^");
						tweet = tweet.replaceAll("\'", "_");
						json = json + "'text' : '"+tweet+"'}}";
						insert = true;
					}
				}
			}
			if (insert) {
				dbObject = (DBObject)JSON.parse(json);
				collection.insert(dbObject);
				insert = false;
			}
			line = reader.readLine();
		}
		reader.close();
		
		DBCursor cursor = collection.find();
		String s;
		while (cursor.hasNext()) {
			s = cursor.next().toString();
			System.out.println(s);
		}
	}

}
