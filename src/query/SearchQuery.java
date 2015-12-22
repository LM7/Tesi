package query;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SearchQuery {
	
	public static void main(String[] args) throws TwitterException, IOException {
		FileWriter file = new FileWriter("SearchProva.txt", true);
		PrintWriter outSearch = new PrintWriter(file);
		String consumerKey = "";
	    String consumerSecret = "";
	    String accessToken = "";
	    String accessSecret = "";
	    ConfigurationBuilder cb;
	    TwitterFactory tf;
	    Twitter twitter;
	    cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	        .setOAuthConsumerKey(consumerKey)
	        .setOAuthConsumerSecret(consumerSecret)
	        .setOAuthAccessToken(accessToken)
	        .setOAuthAccessTokenSecret(accessSecret);
	    
	    tf = new TwitterFactory(cb.build());
	    twitter = tf.getInstance();
	    //ArrayList<String> tweets = new ArrayList<String>();
	    //String stato = "Windows10 better OR Windows10 worse";
	    String stato = "";
	    //int limite = 10; 
	    String dataStart = "2015-10-01";
	    String dataEnd = "2015-10-13";
	    int cont = 0;
	    ArrayList<String> test = new ArrayList<String>();
    	Query query = new Query(stato); //+" OR "+ stato2
 		//query.count(limite); // al massimo 100 per pagina, di default 15
 	    //query.setCount(limite); // al massimo 100 per pagina, di default 15
 		query.setSince(dataStart); // Start date of search
 		query.until(dataEnd);
 		QueryResult result = twitter.search(query);
 		System.out.println("PRIMA FOR");
 		for (Status tweet : result.getTweets()) {
 			if ( !(test.contains(tweet.getText())) ) {
 				test.add(tweet.getText());
 				cont++;
	 			outSearch.println("DATA: "+tweet.getCreatedAt()+"; "+tweet.getText());
	 		    System.out.println("DATA: "+tweet.getCreatedAt()+"; "+tweet.getText());
 			}
 		}
 		outSearch.println("FINAL_CONT: "+cont);

		outSearch.close();
	}

}
