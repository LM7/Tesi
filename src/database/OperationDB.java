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
		BufferedReader reader = new BufferedReader(new FileReader("Prova.txt"));
		String line = reader.readLine();
		String ciao = "ehila\nciaoooo\n\ndajeeee";
		ciao = ciao.replaceAll("\n", " ");
		System.out.println(ciao);
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
		
		
		// INDIVIDUA I TWEET SU PIU' RIGHE
		/*BufferedReader reader = new BufferedReader(new FileReader("StreamQueryTweet.txt"));
		String line = reader.readLine();
		int cont = 0;
		int numRiga = 0;
		boolean contato = false;
		while (line != null) {
			if ( (line.startsWith("USER:")) ) {
				cont = 0; //conta le righe che occupano i tweet
				contato = false; //verifica se ho contato le righe che precedono il tweet
			}
			if ( !(line.startsWith("USER:")) && !(line.startsWith("LINGUA:")) && !(line.matches("\\d{4}-\\d{2}-\\d{2}")) && !(line.equals(""))  ) {
				if (!contato) {
					numRiga = numRiga +4;
				}
				contato = true;
				cont++;
				if (cont > 1) {
					numRiga++;
					System.out.println(numRiga);
				}
			}
			if (line.equals("")) {
				numRiga++;
			}
			line = reader.readLine();
		}*/
	}

}
