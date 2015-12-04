package query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sentiment.SentiStrengthMain;

public class Sentiment {

	public static void main(String[] args) throws Exception {
		SentiStrengthMain ss = new SentiStrengthMain();
		int[] valoriProveOneTweet = new int[3];
		PrintWriter outFile = new PrintWriter("DatiWindows10/sentiment.txt");
		BufferedReader reader = new BufferedReader(new FileReader("DatiWindows10/tweetClean.txt"));
		String line = reader.readLine();
		String result = "";
		while (line != null) {
			if (line.startsWith("USER:")) {
				outFile.println(line);
			}
			else if (line.startsWith("DATE:")) {
				outFile.println(line);
			}
			else {
				result = "";
				valoriProveOneTweet = ss.sentimentOneTweet(line);
				result = ss.calculatorOneTweet(valoriProveOneTweet);
				outFile.println(line);
				outFile.println(result);
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();

	}

}
