package query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class OperationFile {
	
	public static int contNeutral(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file)); //da dove leggere
		String line = reader.readLine();
		int cont = 0;
		while (line != null) {
			if (line.contains("_Neutral")) {
				cont++;
			}
			line = reader.readLine();
		}
		reader.close();
		return cont;
	}
	
	public static String deleteURLAndCapo(String stringa) {
		if (stringa.contains("http:")) {
			stringa = stringa.replaceAll("\n", " ");
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
	
	public static void getRT(FileWriter file) throws IOException {
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("Marino/AllTweetsMarino.txt")); //da dove leggere
		String line = reader.readLine();
		String[] splits;
		String user;
		int lung;
		ArrayList<String> rt = new ArrayList<String>();
		while (line != null) {
			line = deleteURLAndCapo(line); 
			if (line.startsWith("RT")) { //modificare
				splits = line.split(" ");
				if ( !(rt.contains(splits[1])) && (splits[1].startsWith("@")) ) {
					rt.add(splits[1]);
					user = splits[1]; //@Utente:
					lung = user.length();
					user = user.substring(1, lung-1 );
					System.out.println(user);
					outFile.println(user);
				}
			}
			line = reader.readLine();
		}
		outFile.close();
		reader.close();
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		/* Conto gli users neutrali dopo il tweetpast*/
		/*File file = new File("appoggio/AltraProva.txt");
		int result;
		result = contNeutral(file);
		System.out.println(result);*/	
		
		/*
		 * Colleziono tutti gli Users e i RT (eliminando URL e a capo)
		 */
		/*FileWriter file = new FileWriter("Marino/AllUSERSCollection.txt");
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("Marino/AllTweetsMarino.txt")); //da dove leggere
		String line = reader.readLine();
		String[] splits;
		ArrayList<String> users = new ArrayList<String>();
		while (line != null) {
			if ( (line.startsWith("USER:")) ) {
				splits = line.split(" ");
				if ( !(users.contains(splits[1])) ) {
					users.add(splits[1]);
					outFile.println(splits[1]);
				}
			}
			line = reader.readLine();
		}
		outFile.close();
		reader.close();
		FileWriter fileRT = new FileWriter("Marino/AllRTCollection.txt");
		getRT(fileRT);
		System.out.println("DONE");*/
	}

}



/* CODICE UTILIZZATO SPESSO PER ELIMINARE URL E SPAZI A CAPO-> RISCRIVE SULLO STESSO FILE
FileInputStream fstream = null;  
DataInputStream in = null;
BufferedWriter out = null;

try {
	// apro il file
	fstream = new FileInputStream("Marino/SentimentTweetPast2.txt");

	// prendo l'inputStream
	in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String strLine;
	StringBuilder fileContent = new StringBuilder();

	// Leggo il file riga per riga
	while ((strLine = br.readLine()) != null) {
		//System.out.println(strLine); // stampo sulla console la riga corrispondente
		strLine = deleteURLAndCapo(strLine);
		//la trascrivo così com'è-> con la cancellazione url
		fileContent.append(strLine);
		fileContent.append(System.getProperty("line.separator"));

	}

	// Sovrascrivo il file con il nuovo contenuto (aggiornato)
	FileWriter fstreamWrite = new FileWriter("Marino/SentimentTweetPast2.txt");
	out = new BufferedWriter(fstreamWrite);
	out.write(fileContent.toString());

} catch (Exception e) {
	e.printStackTrace();

} finally {
	// chiusura dell'output e dell'input
	try {
		fstream.close();
		out.flush();
		out.close();
		in.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
System.out.println("DONE");*/






//per evitare duplicati CODICE VECCHIO
// ----
/*ArrayList<String> noDupl = new ArrayList<String>();
String set = "";
int contDup = 0;
// ----
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
		set = ""; //solo per evitare duplicati json...
		set = line.toString(); //solo per evitare duplicati json...
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
				set = set + line.toString(); //solo per evitare duplicati json...
				json = json + "'date' : '"+line.toString()+"',";
			}
			else if (!(line.equals(""))) {
				tweet = line.toString();
				tweet = tweet.replaceAll("\"", "^");
				tweet = tweet.replaceAll("\'", "_");
				set = set + line.toString(); //solo per evitare duplicati json...
				if ( !(noDupl.contains(set)) ) { //solo per evitare duplicati json...
					noDupl.add(set); //solo per evitare duplicati json...
					json = json + "'text' : '"+tweet+"'}}";
					insert = true;
				}
				else {
					contDup++;
				}
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
System.out.println("I DUPLICATI SONO "+ contDup);
System.out.println(noDupl.size());*/
