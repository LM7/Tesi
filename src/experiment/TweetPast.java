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
		FileWriter file = new FileWriter("Marino/MarinoTweetPastNeg.txt");
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("appoggio/Prova.txt"));
		String line = reader.readLine();
		ResponseList<Status> stati = null;
		while (line != null) {
			stati = mt.tweetsOfUser(line.toString(), 1000);
			System.out.println(line.toString());
			outFile.println(line.toString());
			for (Status stato: stati) {
				if (stato.getText().contains("Marino")) {
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
	}

}
