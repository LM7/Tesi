package naiveBayes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import weka.FileForExcel;

public class GetUser {

	public static void main(String[] args) throws Exception {
		ArrayList<String> users = new ArrayList<String>();
		PrintWriter outFile = new PrintWriter("NaiveBayes/TuttiGliUsersSecondaParte.txt");
		BufferedReader reader = new BufferedReader(new FileReader("NaiveBayes/secondoTestingReady.txt"));
		String line = reader.readLine();
		String[] splits;
		String userduepunti, user;
		int lung;
		int cont = 0;
		while (line!=null) {
			splits = line.split(" ");
			userduepunti = splits[0];
			lung = userduepunti.length();
			user = userduepunti.substring(0, lung-1);
			System.out.println(user);
			if ( !(users.contains(user)) ) {
				users.add(user);
				outFile.println(user);
				cont++;
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
		System.out.println(cont);
	}
}
