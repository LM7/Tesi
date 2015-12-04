package sentiment;

import java.util.ArrayList;
import uk.ac.wlv.sentistrength.SentiStrength;

public class SentiStrengthMain {
	private SentiStrength sentistrength;
	
	public SentiStrengthMain() {
		this.sentistrength = new SentiStrength();
	}
	
	/*calcola il sentiment del tweet in base all'ultimo valore di "scale" */
	
	public ArrayList<String> calculator(ArrayList<Integer> result) {
		String mood = "";
		ArrayList<String> moodTotal = new ArrayList<String>();
		int lung = result.size();
		int i = 2;
		int j = 0;
		
		while (i < lung) {
			if (result.get(i) > 0) {
				mood = j + ": positive";
				moodTotal.add(mood);
			}
			else if (result.get(i) < 0) {
				mood = j + ": negative";
				moodTotal.add(mood);
			}
			else {
				mood = j + ": neutral";
				moodTotal.add(mood);
			}
			i = i + 3; //perchè voglio solo l'ultimo numero "scale"
			j++;
		}
	
		return moodTotal;
	}
	
	public String calculatorOneTweet(int[] result) {
		String mood = "";
		if (result[2] > 0) {
			mood = "positive";
		}
		else if (result[2] < 0) {
			mood = "negative";
		}
		else {
			mood = "neutral";
		}
		return mood;
	}
	
	/*stampa tutti  e 3 i valori dei tweet*/
	
	public ArrayList<Integer> sentiment(ArrayList<String> tweets) {
		String ssthInitialisation[] = {"sentidata", "SentStrength_Data/", "scale"}; //explain
		this.sentistrength.initialise(ssthInitialisation);
		ArrayList<Integer> valori = new ArrayList<Integer>();
		for (String tweet: tweets) {
			System.out.println(this.sentistrength.computeSentimentScores(tweet)); //Stampo i risultati (con l'explain se volessi)
			String sent = this.sentistrength.computeSentimentScores(tweet);
			String[] splits = sent.split(" "); //Ho i 3 valori del tweet
			for (int j = 0; j < splits.length; j++) {
				int valore = Integer.parseInt(splits[j]);
				valori.add(valore);
			}
		}
		return valori;
	}
	public int[] sentimentOneTweet(String tweet) {
		String ssthInitialisation[] = {"sentidata", "SentStrength_Data/", "scale"}; //explain
		this.sentistrength.initialise(ssthInitialisation);
		int[] valori = new int[3];
		System.out.println(this.sentistrength.computeSentimentScores(tweet)); //Stampo i risultati (con l'explain se volessi)
		String sent = this.sentistrength.computeSentimentScores(tweet);
		String[] splits = sent.split(" "); //Ho i 3 valori del tweet
		for (int j = 0; j < splits.length; j++) {
			int valore = Integer.parseInt(splits[j]);
			valori[j] = valore;
		}
		return valori;
	}
	
	public static void main(String[] args) {
		SentiStrengthMain ss = new SentiStrengthMain();
		ArrayList<String> prove = new ArrayList<String>();
		ArrayList<Integer> valoriProve = new ArrayList<Integer>();
		ArrayList<String> proveMood = new ArrayList<String>();
		prove.add("I hate frogs.");
		prove.add("I love dogs.");
		prove.add("I'm happy with the environment");
		prove.add("The current program is boring");
		prove.add("I wonder when this is going to end!");
		valoriProve = ss.sentiment(prove);
		proveMood = ss.calculator(valoriProve);
		/*prove.add("Io sono contento");
		prove.add("Odio tutti");
		prove.add("Amo la Roma");
		prove.add("Sono felice anche se annoiato");
		prove.add("Siamo tutti molto tristi");*/
		int j = 0;
		for (String stringa: proveMood) {
			//System.out.println(valoriProve.get(j));
			System.out.println(stringa);
			j++;
		}
		
		
		System.out.println();
		System.out.println("METODI SU UN SOLO TWEET");
		String oneTweet = "We are very sad";
		String result = "";
		int[] valoriProveOneTweet = new int[3];
		valoriProveOneTweet = ss.sentimentOneTweet(oneTweet);
		result = ss.calculatorOneTweet(valoriProveOneTweet);
		System.out.println("RISULTATO: "+result);
	}

}
