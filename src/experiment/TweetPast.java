package experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.MainTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TweetPast {

	public static void main(String[] args) throws IOException, TwitterException, ParseException {
		MainTwitter mt = new MainTwitter();
		FileWriter file = new FileWriter("appoggio/AncoraProva.txt");
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("appoggio/AltraProva.txt"));
		String line = reader.readLine();
		ResponseList<Status> stati = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dataLimite = "2015-10-01";
		Date dateLim = sdf.parse(dataLimite);
		int cont;
		while (line != null) {
			if (line.contains("_Neutral")) {
				int indice = line.indexOf("_Neutral");
				line = line.substring(0, indice);
				try {
					stati = mt.tweetsOfUser(line, 200);
				} catch (Exception e) {
					file.close();
					reader.close();
					System.out.println("ERRORE");
					System.out.println(e.getMessage());
				}
				System.out.println(line);
				outFile.println("USER: "+line);
				cont = 0;
				for (Status stato: stati) {
					Date date = stato.getCreatedAt();
					String dateString = sdf.format(date);
					String text = stato.getText();
					if (date.before(dateLim) && (text.contains("Marino") || text.contains("marino")) ) {
						outFile.println("........... USER: "+line+"............");
						outFile.println(dateString);
						outFile.println(text);
						outFile.println();
						cont++;
					}
				}
				outFile.println("NUMERO DEI TWEET: "+cont);
			}
			line = reader.readLine();
		}
		file.close();
		reader.close();
	}

}
