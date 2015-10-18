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
	
	public static void getRT(FileWriter file) throws IOException {
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("NewMarsWater.txt")); //da dove leggere
		String line = reader.readLine();
		String[] splits;
		String user;
		int lung;
		ArrayList<String> rt = new ArrayList<String>();
		while (line != null) {
			if (line.startsWith("TEXTprendinegrt:")) {
				splits = line.split(" ");
				if ( !(rt.contains(splits[2])) && (splits[2].startsWith("@")) ) {
					rt.add(splits[2]);
					user = splits[2]; //@Utente:
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
		FileWriter file = new FileWriter("UserControMarsWater.txt");
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("NewMarsWater.txt")); //da dove leggere
		String line = reader.readLine();
		//int cont = 0;
		String[] splits;
		ArrayList<String> users = new ArrayList<String>();
		while (line != null) {
			if ( (line.startsWith("USERprendineg:")) ) {
				splits = line.split(" ");
				if ( !(users.contains(splits[1])) ) {
					users.add(splits[1]);
					outFile.println(splits[1]);
				}
				//cont++;
			}
			line = reader.readLine();
		}
		outFile.close();
		reader.close();
		FileWriter fileRT = new FileWriter("RTnegMarsWater.txt");
		getRT(fileRT);
		
		//System.out.println(cont);

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
		System.out.println("DONE");
	}

}
