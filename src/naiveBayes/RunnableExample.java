package naiveBayes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import naiveBayes.BayesClassifier;
import naiveBayes.Classifier;

public class RunnableExample {

    public static void main(String[] args) throws Exception {

        /*
         * Create a new classifier instance. The context features are
         * Strings and the context will be classified with a String according
         * to the featureset of the context.
         */
        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        /*
         * The classifier can learn from classifications that are handed over
         * to the learn methods. Imagin a tokenized text as follows. The tokens
         * are the text's features. The category of the text will either be
         * positive or negative.
         */
        // TRAINING
        /*final String[] positiveText = "I love sunny days".split("\\s");
        bayes.learn("positive", Arrays.asList(positiveText));

        final String[] negativeText = "I hate rain days".split("\\s");
        bayes.learn("negative", Arrays.asList(negativeText));
        
        final String[] neutralText = "Forza grande Youtube".split("\\s");
        bayes.learn("neutral", Arrays.asList(neutralText));*/
        
        BufferedReader reader = new BufferedReader(new FileReader("")); //insieme di training
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			splits = line.split("\\.");
			if (splits[1].equals("positive")) {
				final String[] positiveText = splits[0].split("\\s");
				bayes.learn("positive", Arrays.asList(positiveText));
			}
			else {
				if (splits[1].equals("negative")) {
					final String[] negativeText = splits[0].split("\\s");
					bayes.learn("negative", Arrays.asList(negativeText));
				}
				else if (splits[1].equals("neutral")) {
					final String[] neutralText = splits[0].split("\\s");
					bayes.learn("neutral", Arrays.asList(neutralText));
				}
			}
			line = reader.readLine();
		}
		reader.close();

        /*
         * Now that the classifier has "learned" two classifications, it will
         * be able to classify similar sentences. The classify method returns
         * a Classification Object, that contains the given featureset,
         * classification probability and resulting category.
         */
		// TESTING
		
        /*final String[] unknownText1 = "today is a sunny days".split("\\s");
        final String[] unknownText2 = "there will be rain days".split("\\s");
        final String[] unknownText3 = "Quanto �� bello Youtube".split("\\s");

        System.out.println( // will output "positive"
                bayes.classify(Arrays.asList(unknownText1)).getCategory());
        System.out.println( // will output "negative"
                bayes.classify(Arrays.asList(unknownText2)).getCategory());
        System.out.println( 
                bayes.classify(Arrays.asList(unknownText3)).getCategory());*/
		
		BufferedReader reader2 = new BufferedReader(new FileReader("")); //insieme di testing
		String line2 = reader2.readLine();
		String sentiment;
		int pos = 0;
		int neg = 0;
		int neu = 0;
		/*int quindiciPos = 0;
		int quattordiciPos = 0;
		int trediciPos = 0;
		int quindiciNeg = 0;
		int quattordiciNeg = 0;
		int trediciNeg = 0;
		int quindiciNeu = 0;
		int quattordiciNeu = 0;
		int trediciNeu = 0;*/
		while (line2 != null) {
			final String[] unknownText = line2.split("\\s"); //senza sentiment
			sentiment = bayes.classify(Arrays.asList(unknownText)).getCategory();
			if (sentiment.equals("positive")) {
				pos++;
				/*if (line2.contains("2015")) {
					quindiciPos++;
				}
				if (line2.contains("2014")) {
					quattordiciPos++;
				}
				if (line2.contains("2013")) {
					trediciPos++;
				}*/
			}
			if (sentiment.equals("negative")) {
				neg++;
				/*if (line2.contains("2015")) {
					quindiciNeg++;
				}
				if (line2.contains("2014")) {
					quattordiciNeg++;
				}
				if (line2.contains("2013")) {
					trediciNeg++;
				}*/
			}
			if (sentiment.equals("neutral")) {
				neu++;
				/*if (line2.contains("2015")) {
					quindiciNeu++;
				}
				if (line2.contains("2014")) {
					quattordiciNeu++;
				}
				if (line2.contains("2013")) {
					trediciNeu++;
				}*/
			}
			line2 = reader2.readLine();
		}
		reader2.close();
		System.out.println("POSITIVE: "+pos);
		System.out.println("NEGATIVE: "+neg);
		System.out.println("NEUTRAL: "+neu);
		
		/*System.out.println("2015 positive: "+quindiciPos);
		System.out.println("2014 positive: "+quattordiciPos);
		System.out.println("2013 positive: "+trediciPos);
		System.out.println("2015 negative: "+quindiciNeg);
		System.out.println("2014 negative: "+quattordiciNeg);
		System.out.println("2013 negative: "+trediciNeg);
		System.out.println("2015 neutral: "+quindiciNeu);
		System.out.println("2014 neutral: "+quattordiciNeu);
		System.out.println("2013 neutral: "+trediciNeu);*/
		
		
		

        /*
         * The BayesClassifier extends the abstract Classifier and provides
         * detailed classification results that can be retrieved by calling
         * the classifyDetailed Method.
         *
         * The classification with the highest probability is the resulting
         * classification. The returned List will look like this.
         * [
         *   Classification [
         *     category=negative,
         *     probability=0.0078125,
         *     featureset=[today, is, a, sunny, day]
         *   ],
         *   Classification [
         *     category=positive,
         *     probability=0.0234375,
         *     featureset=[today, is, a, sunny, day]
         *   ]
         * ]
         */
        /*((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1));*/

        /*
         * Please note, that this particular classifier implementation will
         * "forget" learned classifications after a few learning sessions. The
         * number of learning sessions it will record can be set as follows:
         */
        bayes.setMemoryCapacity(1000); // remember the last 500 learned classifications
    }

}
