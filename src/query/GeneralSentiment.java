package query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pos.TweetTextMain;
import pos.Extractor.Entity;
import sentiment.SentiStrengthMain;

public class GeneralSentiment {
	
	public static String getRTfromString(String tweet) {
		String rt = "";
		String[] splits = tweet.split(" ");
		rt = splits[1];
		int lung = rt.length();
		rt  = rt.substring(1, lung-1 );
		return rt;
	}
	
	public static boolean isInFile(File file, String word) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		while (line != null) {
			if (line.equalsIgnoreCase(word)) {
				reader.close();
				return true;
			}
			line = reader.readLine();
		}
		reader.close();
		return false;
	}

	public static void main(String[] args) throws IOException {
		//a parte
		String mentionPos = "iostoconmarino";
		List<String> wordsNeg = new ArrayList<String>(Arrays.asList("Messaggero", "Quotidiano", "Ilfattoquotidiano.it", "Ilfattoquotidiano", "ilmessaggero.it", "ilmessaggero"));
		List<String> wordsNeutral = new ArrayList<String>(Arrays.asList("YouTube","YouTube.", "@YouTube:", "focus", "intervista", "@ArsenaleKappa:", "ultimenotizie", "commenta con noi", "sondaggi", "condividi", "clicca", "SkyTG24", "Renzi", "Crozza", "#Crozza:", "Crozza:", "#Crozza", "#Renzi", "#Renzi:", "Renzi:", "GoogleNewsItalia", "BreakingNews", "Solo News"));
		// -----
		TweetTextMain tweetext = new TweetTextMain();
		ArrayList<String> entities = new ArrayList<String>();
		PrintWriter outFile = new PrintWriter("Marino/SentimentAllTweets.txt");
		BufferedReader reader = new BufferedReader(new FileReader("Marino/AllTweetsMarino.txt"));
		File hashtagneg = new File("HelpSentiment/#Neg.txt");
		File hashtagpos = new File("HelpSentiment/#Pos.txt");
		File rtneg = new File("HelpSentiment/RTNeg.txt");
		File rtpos = new File("HelpSentiment/RTPos.txt");
		File mentionNeg = new File("HelpSentiment/@Neg.txt");
		String line = reader.readLine();
		String result = "";
		SentiStrengthMain ss = new SentiStrengthMain();
		int[] valoriProveOneTweet = new int[3];
		String[] splits;
		boolean fuori;
		int neuV = 0;
		int posV = 0;
		int negV = 0;
		int pos = 0;
		int neg = 0;
		int neu = 0;
		int cont = 0;
		while (line != null) {
			cont++;
			System.out.println(cont);
			if ((line.startsWith("USER:"))) {
				outFile.println(line);
			}
			else if ((line.startsWith("DATE:"))) {
				outFile.println(line);
			}
			else if ( !(line.startsWith("USER:")) && !(line.startsWith("DATE:")) && !(line.equals("")) ) {
				fuori = false;
				outFile.println(line);
				valoriProveOneTweet = ss.sentimentOneTweet(line);
				result = ss.calculatorOneTweet(valoriProveOneTweet);
				//operazioni su result in base a line RICORDARSI DI USCIRE DA QUESTO ELSE IF QUANDO CAMBIO RESULT
				/* NEUTRAL */
				splits = line.split(" ");
				for (int i = 0; i < splits.length && !fuori; i++) {
					if (wordsNeutral.contains(splits[i])) {
						result = "neutralVero";
						neuV++;
						fuori = true;
					}
					else if (wordsNeg.contains(splits[i])) {
						result = "negativeVero";
						negV++;
						fuori = true;
					}
				}
				if (line.startsWith("RT") && !fuori) {
					String rt = getRTfromString(line);
					if ( !(rt.equals("")) ) {
						if (isInFile(rtneg, rt) || rt.contains("M5S") || rt.contains("m5s") || rt.contains("M5s")) {
							result = "negativeVero";
							negV++;
							fuori = true;
						}
						else if (isInFile(rtpos, rt)) {
							result = "positiveVero";
							posV++;
							fuori = true;
						}
					}
				}
				if (!fuori) {
					entities = tweetext.tweetTextEntities(line);
					for (String entita: entities) {
						if (isInFile(hashtagneg, entita)) {
							result = "negativeVero";
							negV++;
						}
						else  {
							if (isInFile(hashtagpos, entita)) {
								result = "positiveVero";
								posV++;
							}
							else {
								if (isInFile(mentionNeg, entita)) {
									result = "negativeVero";
									negV++;
								}
								else if (mentionPos.equalsIgnoreCase(entita)){
									result = "positiveVero";
									posV++;
								}
							}
						}
					}
				}
				if (result.equals("positive")) {
					pos++;
				}
				else {
					if (result.equalsIgnoreCase("negative")) {
						neg++;
					}
					else if (result.equalsIgnoreCase("neutral")) {
						neu++;
					}
				}
				outFile.println(result); // positive, negative, neutral
				outFile.println();
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
		System.out.println("POSITIVE_VERO: "+posV);
		System.out.println("NEGATIVE_VERO: "+negV);
		System.out.println("NEUTRAL_VERO: "+neuV);
		System.out.println();
		System.out.println("POSITIVE: "+pos);
		System.out.println("NEGATIVE: "+neg);
		System.out.println("NEUTRAL: "+neu);
	}

}
