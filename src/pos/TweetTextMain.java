package pos;

import java.util.ArrayList;
import java.util.List;

import pos.Extractor.Entity;

public class TweetTextMain {
	private Extractor extractor;
	
	public TweetTextMain() {
		this.extractor = new Extractor();
	}
	
	public List<String> tweetTextHashTags(String tweet) {
		List<String> hashtags = new ArrayList<String>();
		hashtags = this.extractor.extractHashtags(tweet);
		return hashtags;
	}
	
	public ArrayList<String> tweetTextEntities(String tweet) {
		List<Entity> entities = new ArrayList<Entity>();
		entities = this.extractor.extractEntitiesWithIndices(tweet);
		ArrayList<String> stringhe = new ArrayList<String>();
		for (Entity ent: entities) {
			stringhe.add(ent.getValue());
		}
		return stringhe;
	}
	
	public static void main(String[] args) {
		TweetTextMain tweetext = new TweetTextMain();
		List<String> entities = tweetext.tweetTextEntities("Turbina non si tocca #saveiturbe #Number7 @Juan_iturbe93");
		for (String stringa: entities) {
			System.out.println(stringa);
		}
	}

	/*public static void main(String[] args) {
		String tweet = "Turbina non si tocca #saveiturbe @Juan_iturbe93";
		String tweet2 = "De Rossi mostra il dito medio ai laziali alla fine del #derby #LazioRoma... http://www.panorama.it/sport/calcio/la-pallonata-insulti-pioli-garcia-maglietta-totti-sfotto-derby-grande-bruttezza/ ��� ";
		String tweet3 = "$An historic night for @OfficialASRoma's talisman Francesco Totti. Now the oldest #UCL scorer: http://uefa.to/1rDS8mH  ";
		Extractor eee = new Extractor();
		List<Entity> entita = new ArrayList<Entity>();
		List<String> screenames = new ArrayList<String>();
		List<String> urls = new ArrayList<String>();
		List<String> hashtags = new ArrayList<String>();
		List<String> cashtags = new ArrayList<String>();
		
		System.out.println("ENTITA'");
		entita = eee.extractEntitiesWithIndices(tweet3);
		for (Entity ent: entita) {
			System.out.println(ent);
		}
		
		System.out.println("SCREENNAMES");
		screenames = eee.extractMentionedScreennames(tweet3);
		for (String stringa: screenames) {
			System.out.println(stringa);
		}
		
		System.out.println("URLS");
		urls = eee.extractURLs(tweet3);
		for (String stringa: urls) {
			System.out.println(stringa);
		}
		
		System.out.println("#TAGS");
		hashtags = eee.extractHashtags(tweet);
		for (String stringa: hashtags) {
			System.out.println(stringa);
			System.out.println(stringa.equalsIgnoreCase("saveIturbe"));
		}
		
		System.out.println("$TAGS");
		cashtags = eee.extractCashtags(tweet3);
		for (String stringa: cashtags) {
			System.out.println(stringa);
		}
		
		System.out.println("FINE");
		
	}*/

}

