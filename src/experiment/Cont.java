package experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pos.TweetTextMain;

import com.mongodb.util.Hash;

public class Cont {
	
	public static void rewriteUsers(File file) throws Exception {
		FileWriter fileWrite = new FileWriter("DatiMarquez/provaaaa.txt");
		PrintWriter outFile = new PrintWriter(fileWrite);
		BufferedReader reader = new BufferedReader(new FileReader(file)); //da dove leggere
		String line = reader.readLine();
		ArrayList<String> users = new ArrayList<String>();
		while (line != null) {
			if ( !(users.contains(line)) ) {
				users.add(line);
				outFile.println(line);
			}
			line = reader.readLine();
		}
		outFile.close();
		reader.close();
	}
	
	public static void contUsers(File file) throws Exception {
		FileWriter fileWrite = new FileWriter("DatiWindows10/AllUsers.txt");
		PrintWriter outFile = new PrintWriter(fileWrite);
		BufferedReader reader = new BufferedReader(new FileReader(file)); //da dove leggere
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
	}
	
	public static void contScreenNames(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		List<String> lista = new ArrayList<String>();
		HashMap<String, Integer> screenNamesToval = new HashMap<String, Integer>();
		int valore;
		TweetTextMain tweetText = new TweetTextMain();
		while (line != null) {
			if ( !(line.startsWith("USER:")) && !(line.startsWith("DATE:")) && !(line.equals("")) )  { //&& !(line.startsWith("positive")) && !(line.startsWith("negative")) && !(line.startsWith("neutral"))
				lista = tweetText.tweetTextScreenName(line);
				for (String stringa: lista) {
					if ( !(screenNamesToval.containsKey(stringa))) {
						screenNamesToval.put(stringa, 1);
					}
					else {
						valore = screenNamesToval.get(stringa);
						valore = valore +1;
						screenNamesToval.put(stringa, valore);
					}
				}
				lista.clear();
			}
			line = reader.readLine();
		}
		reader.close();
		PrintWriter outFile = new PrintWriter("DatiWindows10/ScreenNames.txt");
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
			if ( !(line.startsWith("USER:")) && !(line.startsWith("DATE:")) && !(line.equals("")) )  {  //&& !(line.startsWith("positive")) && !(line.startsWith("negative")) && !(line.startsWith("neutral"))
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
		PrintWriter outFile = new PrintWriter("DatiWindows10/HashTags.txt");
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
			if (line.startsWith("RT @")) { //RT
				splits = line.split(" ");
				user = splits[1]; //@Utente: //1
				lung = user.length();
				user = user.substring(1, lung-1 );
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
		PrintWriter outFile = new PrintWriter("DatiWindows10/RT.txt");
		for (String stringa: rtToval.keySet()) {
			outFile.print(stringa+": "+rtToval.get(stringa));
			outFile.println();
		}
		outFile.close();
	}

	public static void main(String[] args) throws Exception {
		File file = new File("DatiWindows10/datiIniziali.txt"); //file da LEGGERE
		//rewriteUsers(file);
		contUsers(file);
		contRT(file);
		contHashTag(file);
		contScreenNames(file);
	}

}
