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
	
	public static String deleteURLAndCapo(String stringa) {
		if (stringa.contains("http")) {
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
	
	
	
	/*
	 * Colleziono tutti gli Users e i RT (eliminando URL e a capo)
	 */
	public static void main(String[] args) throws IOException {
		FileWriter file = new FileWriter("Marino/AllUSERSCollection.txt");
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
		System.out.println("DONE");
	}

}
