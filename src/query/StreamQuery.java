package query;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class StreamQuery {

	private static void GetTweetStreamForKeywords() {
		String consumerKey = "LhwkJs69gcmOYpLM2Vg6iHjQh";
	    String consumerSecret = "Y6G4m97iutw8SWCuz0ut4qGdvhTBMavqB95I4JaFv43AaPZ0TR";
	    String accessToken = "462812178-D0BD0F6UySOfmioGexeNCEQhAxm1kH85foQXJJ2N";
	    String accessSecret = "6AzKzxCck3G0hIpYdV3DExjslRWoAZ2CUyCmaVlrGUu78";
	    ConfigurationBuilder confBuilder = new ConfigurationBuilder();
	    Configuration config;
	    
	    confBuilder = new ConfigurationBuilder();
	    confBuilder.setDebugEnabled(true)
	        .setOAuthConsumerKey(consumerKey)
	        .setOAuthConsumerSecret(consumerSecret)
	        .setOAuthAccessToken(accessToken)
	        .setOAuthAccessTokenSecret(accessSecret);
	    
	    config = confBuilder.build();
		

		TwitterStream twitterStream = new TwitterStreamFactory(config).getInstance();
	    //TwitterStream twitterStream = new TwitterStreamFactory();
		StatusListener statusListener = new StatusListener() {
			
			@Override
			public void onStatus(Status status) {
				System.out.println("...LOADING STATUS..."); //forse sarebbe meglio salvare gli status
				FileWriter file;
				Date date;
				String data;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String text;
				try {
					file = new FileWriter("StreamWindows10ElCapitan.txt");
					PrintWriter outStream = new PrintWriter(file);
					System.out.println("YES");
					outStream.println("USER: "+status.getUser().getScreenName());
					outStream.println("LINGUA: "+status.getLang());
					date = status.getCreatedAt();
					data = sdf.format(date);
					outStream.println(data);
					text = status.getText();
					text = text.replaceAll("\n", " ");
					System.out.println(text);
					outStream.println(text);
					outStream.println();
					outStream.close();
				} catch (IOException e) {
					System.out.println("ERRORE IN ONSTATUS");
					e.printStackTrace();
				}
				
			}


			@Override
			public void onDeletionNotice(StatusDeletionNotice sdn) {
				throw new UnsupportedOperationException("Not supported yet."); 
			}

			@Override
			public void onTrackLimitationNotice(int i) {
				throw new UnsupportedOperationException("Not supported yet."); 
			}

			@Override
			public void onScrubGeo(long l, long l1) {
				throw new UnsupportedOperationException("Not supported yet."); 
			}

			@Override
			public void onStallWarning(StallWarning sw) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		FilterQuery fq = new FilterQuery();        
		
		String language[] = {"en"};
		//gli spazi sono end, tra loro tutti or
		String keywords[] = {"sport"};
		//String keywords[] = {"El Capitan", "Windows10", "El Capitan better", "Windows10 better", "El Capitan worse", "Windows10 worse"};
		//{"El Capitan is better than", "Windows10 is better than", "El Capitan is worse than", "Windows10 is worse than"};
		fq.track(keywords); 
		//fq.language(language);

		twitterStream.addListener(statusListener);
		twitterStream.filter(fq); 
	}  

	public static void main(String[] args) {
		try {
			StreamQuery.GetTweetStreamForKeywords();
		} catch (Exception e) {
			System.out.println("ERRORE NEL MAIN");
			e.printStackTrace();
		}
		
	}

}
