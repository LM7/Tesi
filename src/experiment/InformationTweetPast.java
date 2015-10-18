package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import database.TwitterNeo4j;
import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;


public class InformationTweetPast {

	public static void main(String[] args) throws IOException, TwitterException {
		TwitterNeo4j neo = new TwitterNeo4j();
		MainTwitter mt = new MainTwitter();
		FileWriter file = new FileWriter("MarinoTweetPastPos.txt");
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("UserProMarino.txt"));
		String line = reader.readLine();
		ResponseList<Status> stati = null;
		while (line != null) {
			stati = mt.tweetsOfUser(line.toString(), 100);
			System.out.println(line.toString());
			outFile.println(line.toString());
			for (Status stato: stati) {
				if (stato.getText().contains("Marino")) {
					neo.newStatus(stato);
					outFile.println(stato.getCreatedAt());
					outFile.println(stato.getText());
					outFile.println();
				}
			}
			outFile.println();
			line = reader.readLine();
		}
		file.close();
		reader.close();
		//prova assurda
		System.out.println("CONTRO");
		FileWriter file2 = new FileWriter("MarinoTweetPastNeg.txt");
		PrintWriter outFile2 = new PrintWriter(file2);
		BufferedReader reader2 = new BufferedReader(new FileReader("UserControMarino.txt"));
		String line2 = reader2.readLine();
		ResponseList<Status> stati2 = null;
		while (line2 != null) {
			stati2 = mt.tweetsOfUser(line2.toString(), 100);
			System.out.println(line2.toString());
			outFile2.println(line2.toString());
			for (Status stato: stati2) {
				if (stato.getText().contains("Marino")) {
					neo.newStatus(stato);
					outFile2.println(stato.getCreatedAt());
					outFile2.println(stato.getText());
					outFile2.println();
				}
			}
			outFile2.println();
			line2 = reader2.readLine();
		}
		file2.close();
		reader2.close();
	}

}
