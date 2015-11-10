package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ForNeutralUsers {

	public static void main(String[] args) throws Exception {
		PrintWriter outFile = new PrintWriter("appoggio/AncoraProva.txt");
		BufferedReader reader = new BufferedReader(new FileReader("appoggio/AltraProva.txt"));
		String line = reader.readLine();
		
		BufferedReader reader2 = new BufferedReader(new FileReader("NaiveBayes/TuttiGliUsers.txt"));
		String line2 = reader2.readLine();
		ArrayList<String> users = new ArrayList<String>();
		while (line2 != null) {
			users.add(line2);
			line2 = reader2.readLine();
		}
		reader2.close();
		
		String[] splits;
		String user;
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				user = splits[1];
				if (users.contains(user)) {
					users.remove(user);
					System.out.println("rimosso "+user);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		
		for (String stringa: users) {
			outFile.println(stringa);
		}
		outFile.close();
	}

}
