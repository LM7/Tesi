package language;

import at.knallgrau.textcat.TextCategorizer;

public class TextCatMain {
	
	public static String lang(String text) {
		String lang = "";
		TextCategorizer guesser = new TextCategorizer();
        if(text.length() > 0) {
            lang = guesser.categorize(text);
        }
        return lang;
	}

	public static void main(String[] args) {
		String text = "What does ZH mean? This page is about the various possible meanings of the";
		String language = TextCatMain.lang(text);
		System.out.println(language);
     }
	
}

