package query;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SearchQuery {
	
	public static void main(String[] args) throws TwitterException {
		String consumerKey = "LhwkJs69gcmOYpLM2Vg6iHjQh";
	    String consumerSecret = "Y6G4m97iutw8SWCuz0ut4qGdvhTBMavqB95I4JaFv43AaPZ0TR";
	    String accessToken = "462812178-D0BD0F6UySOfmioGexeNCEQhAxm1kH85foQXJJ2N";
	    String accessSecret = "6AzKzxCck3G0hIpYdV3DExjslRWoAZ2CUyCmaVlrGUu78";
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
	    ArrayList<String> tweets = new ArrayList<String>();
	    //String stato = "Windows10 better OR Windows10 worse";
	    String stato = "marino";
	    int limite = 10; 
	    String dataStart = "2015-09-01";
	    String dataEnd = "2015-10-04";
	    Query query = new Query(stato); //+" OR "+ stato2
		//query.count(limite); // al massimo 100 per pagina
	    query.setCount(1000);
		query.setSince(dataStart); // Start date of search
		query.until(dataEnd);
		QueryResult result = twitter.search(query);
		int cont = 0;
		for (Status tweet : result.getTweets()) {
			cont++;
		    System.out.println("DATA:"+tweet.getCreatedAt()+"; "+tweet.getText());
			//tweets.add(tweet.getText());
		}
		System.out.println(cont);
	}

}
