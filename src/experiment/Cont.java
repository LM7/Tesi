package experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pos.TweetTextMain;

import com.mongodb.util.Hash;

public class Cont {
	
	public static void contScreenNames(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		List<String> lista = new ArrayList<String>();
		HashMap<String, Integer> screenNamesToval = new HashMap<String, Integer>();
		int valore;
		TweetTextMain tweetText = new TweetTextMain();
		int lunghezza;
		int ultimo;
		boolean avanti = false;
		while (line != null) {
			/*if ( !(line.startsWith("USER:")) && !(line.matches("\\d{4}-\\d{2}-\\d{2}")) && !(line.startsWith("DATE:")) && !(line.equals(""))
					&& !(line.startsWith("positive")) && !(line.startsWith("negative")) && !(line.startsWith("neutral")) )  {*/
			if (line.startsWith("TEXT:")) {
				lista = tweetText.tweetTextScreenName(line);
				for (String stringa: lista) {
					lunghezza = stringa.length();
					ultimo = lunghezza-1;
					if (stringa.charAt(ultimo) == ':') {
						avanti = true;
					}
					if ( !(screenNamesToval.containsKey(stringa)) && !avanti ) {
						screenNamesToval.put(stringa, 1);
					}
					else if (!avanti) {
						valore = screenNamesToval.get(stringa);
						valore = valore +1;
						screenNamesToval.put(stringa, valore);
					}
				}
				lista.clear();
				avanti = false;
			}
			line = reader.readLine();
		}
		reader.close();
		PrintWriter outFile = new PrintWriter("ProvaMarinoNonDimissioniScreenNames.txt");
		for (String stringa: screenNamesToval.keySet()) {
			outFile.print(stringa+": "+screenNamesToval.get(stringa));
			outFile.println();
		}
		outFile.close();
	}
	
	public static void contHashTag(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		List<String> lista = new ArrayList<String>();
		HashMap<String, Integer> hashToval = new HashMap<String, Integer>();
		int valore;
		TweetTextMain tweetText = new TweetTextMain();
		while (line != null) {
			/*if ( !(line.startsWith("USER:")) && !(line.matches("\\d{4}-\\d{2}-\\d{2}")) && !(line.startsWith("DATE:")) && !(line.equals(""))
					&& !(line.startsWith("positive")) && !(line.startsWith("negative")) && !(line.startsWith("neutral")) )  {*/
			if (line.startsWith("TEXT:")) {
				lista = tweetText.tweetTextHashTags(line);
				for (String stringa: lista) {
					if ( !(hashToval.containsKey(stringa)) ) {
						hashToval.put(stringa, 1);
					}
					else {
						valore = hashToval.get(stringa);
						valore = valore +1;
						hashToval.put(stringa, valore);
					}
				}
				lista.clear();
			}
			line = reader.readLine();
		}
		reader.close();
		PrintWriter outFile = new PrintWriter(".txt");
		for (String stringa: hashToval.keySet()) {
			outFile.print(stringa+": "+hashToval.get(stringa));
			outFile.println();
		}
		outFile.close();
	}
	
	public static void contRT(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		int lung = 0;
		String user = "";
		HashMap<String, Integer> rtToval = new HashMap<String, Integer>();
		int valore;
		while (line != null) {
			if (line.startsWith("TEXT: RT")) { //RT
				splits = line.split(" ");
				user = splits[2]; //@Utente: //1
				lung = user.length();
				user = user.substring(1, lung-1 );
				System.out.println(user);
				if ( !(rtToval.containsKey(user)) ) {
					rtToval.put(user, 1);
				}
				else {
					valore = rtToval.get(user);
					valore = valore +1;
					rtToval.put(user, valore);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		PrintWriter outFile = new PrintWriter(".txt");
		for (String stringa: rtToval.keySet()) {
			outFile.print(stringa+": "+rtToval.get(stringa));
			outFile.println();
		}
		outFile.close();
	}

	public static void main(String[] args) throws IOException {
		File file = new File("ProvaMarinoNonDimissioni.txt"); //file da LEGGERE
		//contRT(file);
		//contHashTag(file);
		contScreenNames(file);
	}

}
