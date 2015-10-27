package query;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.TwitterNeo4j;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MoreSearchQuery {

	public static void main(String[] args) throws IOException {
		TwitterNeo4j neo = new TwitterNeo4j();
		FileWriter file = new FileWriter("Garcia.txt");
		PrintWriter outFile = new PrintWriter(file);
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
		Query query = new Query("Garcia Roma");
		int numberOfTweets = 20; //512
		//-----
		String dataStart = "2015-10-20";
	    String dataEnd = "2015-10-27";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    query.setSince(dataStart); // Start date of search
 		query.until(dataEnd);
 		query.setLang("it");
 		//-----
		long lastID = Long.MAX_VALUE;
		List<Status> tweets = new ArrayList<Status>();
		while (tweets.size () < numberOfTweets) {
			if (numberOfTweets - tweets.size() > 100)
				query.setCount(100);
			else 
				query.setCount(numberOfTweets - tweets.size());
			try {
				QueryResult result = twitter.search(query);
				tweets.addAll(result.getTweets());
				System.out.println("Ottenuti " + tweets.size() + " tweets");
				for (Status t: tweets) {
					neo.newStatus(t);
					if(t.getId() < lastID) {
						lastID = t.getId();
					}
				}
			}

			catch (TwitterException te) {
				System.out.println("Couldn't connect: " + te);
			}; 
			query.setMaxId(lastID-1);
		}

		for (int i = 0; i < tweets.size(); i++) {
			Status t = (Status) tweets.get(i);

			//GeoLocation loc = t.getGeoLocation();

			String user = t.getUser().getScreenName();
			String msg = t.getText();
			Date date = t.getCreatedAt();
			String dateString = sdf.format(date);
			/*if (loc!=null) {
				Double lat = t.getGeoLocation().getLatitude();
				Double lon = t.getGeoLocation().getLongitude();
				System.out.println(i + " USER: " + user + " wrote: " + msg + " located at " + lat + ", " + lon+", Date"+date.toString());
			} */
			outFile.println("USER: "+user);
			outFile.println("DATE: "+dateString);
			outFile.println("TEXT: "+msg);
			outFile.println();
			System.out.println(i + " USER: " + user + " wrote: " + msg+", Date: "+dateString);
		}
		outFile.close();
	}

}
