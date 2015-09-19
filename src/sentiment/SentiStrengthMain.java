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
		
		for (String stringa: proveMood) {
			System.out.println(stringa);
		}
	}

}
