package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TweetPast {

	public static void main(String[] args) throws IOException, TwitterException {
		MainTwitter mt = new MainTwitter();
		FileWriter file = new FileWriter("appoggio/Prova.txt", true);
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("Marino/UserProMarino.txt"));
		String line = reader.readLine();
		ResponseList<Status> stati = null;
		while (line != null) {
			stati = mt.tweetsOfUser(line.toString(), 10000);
			System.out.println(line.toString());
			for (Status stato: stati) {
				outFile.println("USER: "+line.toString()+" ..........");
				outFile.println(stato.getCreatedAt());
				outFile.println(stato.getText());
				outFile.println();
			}
			outFile.println();
			line = reader.readLine();
		}
		file.close();
		reader.close();
	}

}
