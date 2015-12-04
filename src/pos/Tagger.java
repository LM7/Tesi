package pos;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cmu.arktweetnlp.Twokenize;
import cmu.arktweetnlp.impl.Model;
import cmu.arktweetnlp.impl.ModelSentence;
import cmu.arktweetnlp.impl.Sentence;
import cmu.arktweetnlp.impl.features.FeatureExtractor;


/**
 * Tagger object -- wraps up the entire tagger for easy usage from Java.
 * 
 * To use:
 * 
 * (1) call loadModel().
 * 
 * (2) call tokenizeAndTag() for every tweet.
 *  
 * See main() for example code.
 * 
 * (Note RunTagger.java has a more sophisticated runner.
 * This class is intended to be easiest to use in other applications.)
 */
public class Tagger {
	public Model model;
	public FeatureExtractor featureExtractor;

	/**
	 * Loads a model from a file.  The tagger should be ready to tag after calling this.
	 * 
	 * @param modelFilename
	 * @throws IOException
	 */
	public void loadModel(String modelFilename) throws IOException {
		model = Model.loadModelFromText(modelFilename);
		featureExtractor = new FeatureExtractor(model, false);
	}

	/**
	 * One token and its tag.
	 **/
	public static class TaggedToken {
		public String token;
		public String tag;
	}


	/**
	 * Run the tokenizer and tagger on one tweet's text.
	 **/
	public List<TaggedToken> tokenizeAndTag(String text) {
		if (model == null) throw new RuntimeException("Must loadModel() first before tagging anything");
		List<String> tokens = Twokenize.tokenizeRawTweetText(text);

		Sentence sentence = new Sentence();
		sentence.tokens = tokens;
		ModelSentence ms = new ModelSentence(sentence.T());
		featureExtractor.computeFeatures(sentence, ms);
		model.greedyDecode(ms, false);

		ArrayList<TaggedToken> taggedTokens = new ArrayList<TaggedToken>();

		for (int t=0; t < sentence.T(); t++) {
			TaggedToken tt = new TaggedToken();
			tt.token = tokens.get(t);
			tt.tag = model.labelVocab.name( ms.labels[t] );
			taggedTokens.add(tt);
		}

		return taggedTokens;
	}
	
	public void nlpString(ArrayList<String> stringhe) throws IOException {
		PrintWriter outNLP = new PrintWriter("nlp.txt", "UTF-8");
		String tagApp = "";
		String tokenApp = "";
		int i = 0;
		//String modelFilename = "model.20120919";
		//String modelFilename = "model.irc.20121211";
		String modelFilename = "model.ritter_ptb_alldata_fixed.20130723";
		Tagger tagger = new Tagger();
		tagger.loadModel(modelFilename);
		for (String stringa: stringhe) {
			i++;
			List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(stringa);
			outNLP.println("Stringa numero "+ i);
			for (TaggedToken token : taggedTokens) { //token.tag è il tag; token.token è la parola
				tagApp = token.tag;
				tokenApp = token.token;
				outNLP.println(tagApp+"   "+tokenApp);
			}
			outNLP.println();
		}
		outNLP.close();
	}
	
	public ArrayList<String> taggerNlp(String tweet) throws IOException {
		ArrayList<String> tagToken = new ArrayList<String>();
		String elemento = "";
		String tagApp = "";
		String tokenApp = "";
		//String modelFilename = "model.20120919";
		//String modelFilename = "model.irc.20121211";
		String modelFilename = "model.ritter_ptb_alldata_fixed.20130723";
		Tagger tagger = new Tagger();
		tagger.loadModel(modelFilename);
		List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(tweet);
		for (TaggedToken token : taggedTokens) { //token.tag è il tag; token.token è la parola
			tagApp = token.tag;
			tokenApp = token.token;
			elemento = tagApp+":"+tokenApp;
			tagToken.add(elemento);
		}
		return tagToken;
	}
	
	/*public static void main(String[] args) throws IOException {
		ArrayList<String> lista = new ArrayList<String>();
		Tagger tagger = new Tagger();
		String text = "@LM7 ^Volkswagen!!^_ FIFA _Volkswagen_ ^Volkswagen^ Ciao mi chiamo fwdwdcVolkswagenfefff Lorenzo I like football and Cristiano is the best player!!! #ForzaRoma #ASRTiAmo #player #is #the #Cristiano";
		lista = tagger.taggerNlp(text);
		for (String stringa: lista) {
			//System.out.println(stringa);
			if (stringa.contains("NNP:") && stringa.contains("Volkswagen")) {
				System.out.println("DAJE");
			}
		}
	}*/

	/**
	 * Illustrate how to load and call the POS tagger.
	 * This main() is not intended for serious use; see RunTagger.java for that.
	 **/
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Supply the model filename as first argument.");
		}
		//String modelFilename = "model.20120919";
		//String modelFilename = "model.irc.20121211";
		String modelFilename = "model.ritter_ptb_alldata_fixed.20130723";
		

		Tagger tagger = new Tagger();
		tagger.loadModel(modelFilename);

		//String text = "RT @DjBlack_Pearl: wat muhfuckaz wearin 4 the lingerie party?????";
		//String text = "Texas Rangers are in the World Series!  Go Rangers!!!!!!!!! http://fb.me/D2LsXBJx";
		String text = "@LM7 FIFA Ciao mi chiamo Lorenzo I like football and Cristiano is the best player!!! #ForzaRoma #ASRTiAmo #player #is #the #Cristiano";
		List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(text);

		for (TaggedToken token : taggedTokens) {
			System.out.printf("%s\t%s\n", token.tag, token.token);
		}
	}

}

