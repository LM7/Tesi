package query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.TwitterNeo4j;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MoreSearchQuery {

	public static void main(String[] args) throws IOException {
		
		/*BufferedReader readerPrincipal = new BufferedReader(new FileReader("topic/query.txt"));
		String linePrincipal = readerPrincipal.readLine();
		String query = "";
		while (linePrincipal != null) {
			query = linePrincipal;
			linePrincipal = readerPrincipal.readLine();
		}
		readerPrincipal.close();*/
		
		TwitterNeo4j neo = new TwitterNeo4j();
		FileWriter file = new FileWriter("", true);
		PrintWriter outFile = new PrintWriter(file);
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
		Query query = new Query("");
		int numberOfTweets = 700; //512
		//-----
		String dataStart = "2015-11-11";
	    String dataEnd = "2015-11-18";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    query.setSince(dataStart); // Start date of search
 		query.until(dataEnd);
 		query.setLang("en");
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
					//neo.newStatus(t);
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
		
		/* Per non avere stati con user già analizzati */
		BufferedReader reader2 = new BufferedReader(new FileReader(""));
		String line2 = reader2.readLine();
		ArrayList<String> users = new ArrayList<String>();
		while (line2 != null) {
			users.add(line2);
			line2 = reader2.readLine();
		}
		reader2.close();

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
			if ( !(users.contains(user)) ) {
				neo.newStatus(t);
				outFile.println("USER: "+user);
				outFile.println("DATE: "+dateString);
				outFile.println(msg);
				outFile.println();
				System.out.println(i + " USER: " + user + " wrote: " + msg+", Date: "+dateString);
			}
			
		}
		outFile.close();
	}

}
