package database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class OperationDB {

	public static void main(String[] args) throws IOException {
		/*PrintWriter outOp = new PrintWriter("DatabaseProvaOperation.txt");
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("streamCollection");
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			Map<String, Object> ogg = cursor.next().toMap();
			for (String stringa: ogg.keySet()) {
				outOp.print(stringa+" : ");
				outOp.print(ogg.get(stringa).toString()+";");
			}
			outOp.println();
		}*/
		
		
		
	}

}
