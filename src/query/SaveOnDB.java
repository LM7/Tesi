package query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.neo4j.shell.impl.SystemOutput;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class SaveOnDB {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("Marino/PastSentimentAllTweets.txt"));
		String line = reader.readLine();
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("marinoCollectionPast");
		BasicDBObject remove = new BasicDBObject();
		collection.remove(remove);
		boolean text = false;
		String[] splits;
		BasicDBObject document = new BasicDBObject();
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				document = new BasicDBObject();
				document.put("user", splits[1]);
			}
			else {
				if ( line.matches("\\d{4}-\\d{2}-\\d{2}") ) {			//line.startsWith("DATE:") per quelli attuali
					//splits = line.split(" ");
					document.put("date", line);				//splits[1] per quelli attuali
				}
				else {
					if (! (line.equals("")) && !text ) {
						document.put("text", line);
						text = true;
					}
					else if (!(line.equals(""))) {
						document.put("sentiment", line);
						collection.insert(document);
						text = false;
					}
				}
			}
			line = reader.readLine();
		}
		reader.close();
		System.out.println("DONE");
	}
	

}
