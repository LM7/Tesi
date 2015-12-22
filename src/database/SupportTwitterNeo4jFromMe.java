package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SupportTwitterNeo4jFromMe {
	
	public static String getID(String stringa) {
		String[] splits = stringa.split(" ");
		return splits[1];
	}
	
	public static String getUser(String stringa) {
		String[] splits = stringa.split(" ");
		return splits[3];
	}
	
	public static String getText(String stringa) {
		String[] splits = stringa.split(" ");
		String text = "";
		for (int i = 4; i < splits.length; i++) {
			if (i == 4) {
				text = text + splits[i];
			}
			else {
				text = text + " " + splits[i];
			}
		}
		return text;
	}
	
	public static boolean isRT(String stringa) {
		if (stringa.contains("RT@")) {
			return true;
		}
		return false;
	}
	
	public static String[] getRT(String stringa) {
		String[] splits = stringa.split(" ");
		String[] userText = new String[2];
		String text = "";
		for (int i = 5; i < splits.length; i++) {
			if (i == 5) {
				text = text + splits[i];
			}
			else {
				text = text + " " + splits[i];
			}
		}
		userText[1] = text;
		
		String RTuser = splits[4];
		int lung = RTuser.length();
		String user = RTuser.substring(3,lung-1);
		userText[0] = user;
		return userText;
		
	}
	
	
	
	
	public static void createFile(File file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		PrintWriter outFile = new PrintWriter("FileSuCuiScrivere.txt");
		int cont = 0;
		String appoggio = "";
		while (line != null) {
			if (line.startsWith("USER:")) {
				appoggio = "ID: "+cont;
				appoggio = appoggio + " " + line;
			}
			else if ( !(line.startsWith("DATE: ")) && !(line.contains("positive")) && !(line.contains("negative")) && !(line.contains("neutral")) && !(line.equals("")) ) {
				appoggio = appoggio + " " + line;
				outFile.println(appoggio);
				appoggio = "";
				cont++;
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	public static void rewrite(File file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		PrintWriter outFile = new PrintWriter("FileSuCuiScrivere.txt");
		while (line != null) {
			if ( !(line.contains("positive")) &&  !(line.contains("neutral")) && !(line.contains("negative")) ) {
				outFile.println(line);
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	public static void getUsers(File file) throws Exception {
		PrintWriter outFile = new PrintWriter("FileSuiCuiScrivere");
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
	
	
	public static void main(String[] args) throws Exception {
		File file = new File(""); //da dove leggere
		//createFile(file);
		//rewrite(file);
		getUsers(file);
	}

}
