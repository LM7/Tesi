package language;

import at.knallgrau.textcat.TextCategorizer;

public class TextCatMain {

	public static void main(String[] args) {
		//String category = "A fine decennio la società viene rilevata da Dino Viola, il quale la trasforma completamente, affidandone la guida tecnica a Nils Liedholm,[3] che ottiene immediatamente buoni risultati, vincendo due Coppe Italia consecutive, superando in entrambe le finali il Torino ai rigori. A ciò segue la vittoria del secondo scudetto, avvenuta nella stagione 1982-83, sempre sotto la guida di Liedholm: il titolo viene conquistato l'8 maggio 1983 allo stadio Luigi Ferraris, dopo un pareggio con il Genoa.[3]";
		String category = "What does ZH mean? This page is about the various possible meanings of the ";
        TextCategorizer guesser = new TextCategorizer();
        if(category.length() > 0) {
           category = guesser.categorize(category);
        }
        System.out.println(category);
     }
}

