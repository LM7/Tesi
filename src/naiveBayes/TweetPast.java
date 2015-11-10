package naiveBayes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;

public class TweetPast {

	public static void main(String[] args) throws Exception {
		MainTwitter mt = new MainTwitter();
		FileWriter file = new FileWriter("NaiveBayes/testingPast.txt", true);
		PrintWriter outFile = new PrintWriter(file);
		FileWriter file2 = new FileWriter("appoggio/UtentiForseNeutrali.txt", true);
		PrintWriter outUsersNeutral = new PrintWriter(file2);
		BufferedReader reader = new BufferedReader(new FileReader("NaiveBayes/usersPerTweetPast.txt")); //file users
		String line = reader.readLine();
		ResponseList<Status> stati = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataLimite = "2015-10-01";
		Date dateLim = sdf.parse(dataLimite);
		int cont;
		boolean neutral = true;
		while (line != null) {
			neutral = true;
			try {
				stati = mt.tweetsOfUser(line, 200);
			} catch (Exception e) {
				file.close();
				reader.close();
				file2.close();
				System.out.println("ERRORE");
				System.out.println(e.getMessage());
			}
			System.out.println(line);
			cont = 0;
			for (Status stato: stati) {
				Date date = stato.getCreatedAt();
				String dateString = sdf.format(date);
				String text = stato.getText();
				if (date.before(dateLim) && (cont < 1) && (text.contains("Marino") || text.contains("marino")) ) {
					outFile.println("USER: "+line);
					outFile.println("DATE: "+dateString);
					outFile.println(text);
					outFile.println();
					cont++;
					neutral = false;
				}
			}
			if (neutral) {
				outUsersNeutral.println(line);
				System.out.println("NEUTRAL");
			}
			line = reader.readLine();
		}
		file.close();
		reader.close();
		file2.close();
	}

}
