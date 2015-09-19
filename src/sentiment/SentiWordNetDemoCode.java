package sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SentiWordNetDemoCode {

	private Map<String, Double> dictionary;

	public SentiWordNetDemoCode(String pathToSWN) throws IOException {
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}

	public double extract(String word, String pos) {
		return dictionary.get(word + "#" + pos);
	}
	
	/*
	 * Somma i vari valori.....
	 */
	public double extractNew(String word) {
		double total = 0;
		if(dictionary.get(word+"#n") != null) {
			total = dictionary.get(word+"#n") + total;
		}	
		if(dictionary.get(word+"#a") != null) {
			total = dictionary.get(word+"#a") + total;
		}	
		if(dictionary.get(word+"#r") != null) {
			total = dictionary.get(word+"#r") + total;
		}
		if(dictionary.get(word+"#v") != null) {
			total = dictionary.get(word+"#v") + total;
		}	
		return total;
	}
	
	public String score(String tweet) {
		String[] words = tweet.split("\\s+"); 
		double totalScore = 0, averageScore;
		for(String word : words) {
		    word = word.replaceAll("([^a-zA-Z\\s])", "");
		    if (this.extractNew(word) == 0) {
		    	continue;
		    }
		    totalScore += this.extractNew(word);
		}
		averageScore = totalScore;
		System.out.println(averageScore);

		if(averageScore>=0.75)
		    return "very positive";
		else if(averageScore > 0.25 && averageScore<0.5)
		    return  "positive";
		else if(averageScore>=0.5)
		    return  "positive";
		else if(averageScore < 0 && averageScore>=-0.25)
		    return "negative";
		else if(averageScore < -0.25 && averageScore>=-0.5)
		    return "negative";
		else if(averageScore<=-0.75)
		    return "very negative";
		return "neutral";
	}
	
	/*
	 * Score su una lista di tweet
	 */
	public ArrayList<String> scoreTweets(ArrayList<String> tweets) {
		ArrayList<String> scores = new ArrayList<String>();
		int j = 0;
		for (String tweet: tweets) {
			String result = this.score(tweet);
			result = j +": "+result;
			scores.add(result);
			j++;
		}
		return scores;
	}
	

	public static void main(String [] args) throws IOException {
		/*if(args.length<1) {
		System.err.println("Usage: java SentiWordNetDemoCode <pathToSentiWordNetFile>");
		return;
		}*/

		String pathToSWN = "SentiWordNet.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		
		/*double result = sentiwordnet.extract("good", "a");
		double resultNew = sentiwordnet.extractNew("good");
		System.out.println("RISULTATO: "+ resultNew);*/
		ArrayList<String> prove = new ArrayList<String>();
		ArrayList<String> proveMood = new ArrayList<String>();
		prove.add("I hate frogs.");
		prove.add("I love dogs.");
		prove.add("I'm happy with the environment");
		prove.add("The current program is boring");
		prove.add("I wonder when this is going to end!");
		proveMood = sentiwordnet.scoreTweets(prove);
		
		for (String stringa: proveMood) {
			System.out.println(stringa);
		}
		
		
		/*String sentimentScore = sentiwordnet.score("I wonder when this is going to end!");
		System.out.println(sentimentScore);*/
		
		/*System.out.println("good#a "+sentiwordnet.extract("good", "a"));
		System.out.println("bad#a "+sentiwordnet.extract("bad", "a"));
		System.out.println("blue#a "+sentiwordnet.extract("blue", "a"));
		System.out.println("blue#n "+sentiwordnet.extract("blue", "n"));*/
	}
}
