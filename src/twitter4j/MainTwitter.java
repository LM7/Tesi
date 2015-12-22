package twitter4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class MainTwitter {
	
	private String consumerKey = "";
    private String consumerSecret = "";
    private String accessToken = "";
    private String accessSecret = "";
    private ConfigurationBuilder cb;
    private TwitterFactory tf;
    private Twitter twitter;
    
    public MainTwitter() {
    	cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	        .setOAuthConsumerKey(this.consumerKey)
	        .setOAuthConsumerSecret(this.consumerSecret)
	        .setOAuthAccessToken(this.accessToken)
	        .setOAuthAccessTokenSecret(this.accessSecret);
	    
	    this.tf = new TwitterFactory(this.cb.build());
	    this.twitter = this.tf.getInstance(); 
    }
    
	public Twitter getTwitter() {
		return twitter;
	}
	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
	
	public ResponseList<Status> tweetsUserPagingNumber(String user, int paging, int numTweet) throws TwitterException {
		Twitter twitter = this.getTwitter();
		ResponseList<Status> stati = null;
		stati = twitter.getUserTimeline(user, new Paging(paging,numTweet));
		return stati;
	}
	
	/*
	 * Resituisce tweets associati ad un user: lingua, data e testo (numeroTweet: max 200)
	 * UTILIZZATO PER IL TWEETPAST
	 */
	public ResponseList<Status> tweetsOfUser(String user, int numeroTweet) throws TwitterException, FileNotFoundException, IOException {
		//PrintWriter outTOU = new PrintWriter("tweetsOfUser.txt", "UTF-8");
		//MainTwitter mt = new MainTwitter();
		Twitter twitter = this.getTwitter();
		ResponseList<Status> stati = null;
		stati = twitter.getUserTimeline(user, new Paging(1,numeroTweet)); //cambiando il paging posso risalire agli stati pi�� vecchi
		/*for (Status stato: stati) {
			//outTOU.println("LINGUA: "+stato.getLang());
			//outTOU.println("DATA: "+ stato.getCreatedAt());
			//outTOU.println("TWEET: "+stato.getText());
			//System.out.println(stato.getText());
		}*/
		//outTOU.close();
		//System.out.println("DONE");
		return stati;
	}


	public HashMap<String,ArrayList<String>> userFollowersOnTopic(String stato, String user, String dataStart) throws TwitterException, FileNotFoundException, UnsupportedEncodingException {
		PrintWriter outMT = new PrintWriter("mtFollowers.txt", "UTF-8");
		//MainTwitter mt = new MainTwitter();
		//Twitter twitter = this.getTwitter();
	
		//Trovo gli stati associati a una query
		System.out.println("--------------TWEET DELL'USER------------");
		outMT.println("--------------TWEET DELL'USER------------");
	    ArrayList<String> stati = new ArrayList<String>();
	    String statoFinale = stato+" from:"+user;
		stati = this.query(statoFinale, 100, dataStart); //"#Totti from:LM791"; "2015-07-20"
		System.out.println("TUTTI I TWEET ASSOCIATI A: "+stato);
		outMT.println("TUTTI I TWEET ASSOCIATI A: "+statoFinale);
		for  (int i = 0; i<stati.size(); i++) {
        	outMT.println(stati.get(i));
        }
		System.out.println("-----------------------------------------");
		outMT.println("-----------------------------------------");
		
		// Trovo i followers associati a un utente
		System.out.println("---------------FOLLOWER--------------------");
		outMT.println("---------------FOLLOWER--------------------");
		ArrayList<String> followers = new ArrayList<String>();
		followers = this.followersOfUser(user, 15); //LM791
		System.out.println("TUTTI I FOLLOWERS DI: "+user);
        for  (int i = 0; i<followers.size(); i++) {
        	outMT.println(followers.get(i));
        }
		System.out.println("-----------------------------------------");
		outMT.println("-----------------------------------------");
		
		//Trovo gli stati di tali followers con la stessa query
		ArrayList<String> appoggioStatiFollowers = new ArrayList<String>();
		HashMap<String,ArrayList<String>> followersToTweets = new HashMap<String,ArrayList<String>>();
		int lungFollowers = followers.size();
		System.out.println("(((((((TWEET FOLLOWER))))))))");
		outMT.println("(((((((TWEET FOLLOWER))))))))");
		for (int i = 0; i < lungFollowers; i++) {
			String statoFinaleFol = stato+" from:"+followers.get(i);
			appoggioStatiFollowers = this.query(statoFinaleFol, 100, dataStart);
			System.out.println("TUTTI I TWEET ASSOCIATI A: "+statoFinaleFol);
			outMT.println("TUTTI I TWEET ASSOCIATI A: "+statoFinaleFol);
			for  (int k = 0; k<appoggioStatiFollowers.size(); k++) {
	        	outMT.println(appoggioStatiFollowers.get(k));
	        }
			followersToTweets.put(followers.get(i), appoggioStatiFollowers); //mappa follower:tutti i suoi stati/tweet su quella query/topic
		}
		System.out.println("(((((((MAPPA CREATA))))))))");
		outMT.println("(((((((MAPPA CREATA))))))))");
		
		//Stampa
		System.out.println("--------------STAMPA FINALE--------------------");
		outMT.println("--------------STAMPA FINALE--------------------");
		for (String follower: followersToTweets.keySet()) {
			System.out.println("FOLLOWER: "+follower);
			outMT.println("FOLLOWER: "+follower);
			System.out.println("TWEETS:");
			outMT.println("TWEETS:");
			for (String tweet: followersToTweets.get(follower)) {
				System.out.println(tweet);
				outMT.println(tweet);
			}
		}
		
		System.out.println("-------------------THE END----------------------");
		outMT.println("-------------------THE END----------------------");
		
	    outMT.close();
		return followersToTweets;
	}
	
	public HashMap<String,ArrayList<String>> userFollowingsOnTopic(String stato, String user, String dataStart) throws TwitterException, FileNotFoundException, UnsupportedEncodingException {
		PrintWriter outMT = new PrintWriter("mtFollowings.txt", "UTF-8");
		//MainTwitter mt = new MainTwitter();
		//Twitter twitter = this.getTwitter();
	
		//Trovo gli stati associati a una query
		System.out.println("--------------TWEET DELL'USER------------");
		outMT.println("--------------TWEET DELL'USER------------");
	    ArrayList<String> stati = new ArrayList<String>();
	    String statoFinale = stato+" from:"+user;
		stati = this.query(statoFinale, 100, dataStart); //"#Totti from:user"; "2015-07-20"
		System.out.println("TUTTI I TWEET ASSOCIATI A: "+stato);
		outMT.println("TUTTI I TWEET ASSOCIATI A: "+statoFinale);
		for  (int i = 0; i<stati.size(); i++) {
        	outMT.println(stati.get(i));
        }
		System.out.println("-----------------------------------------");
		outMT.println("-----------------------------------------");
		
		// Trovo i following associati a un utente
		System.out.println("---------------FOLLOWING--------------------");
		outMT.println("---------------FOLLOWING--------------------");
		ArrayList<String> followings = new ArrayList<String>();
		followings = this.followingsOfUser(user); //LM791
		System.out.println("TUTTI I FOLLOWINGS DI: "+user);
        for  (int i = 0; i<followings.size(); i++) {
        	outMT.println(followings.get(i));
        }
		System.out.println("-----------------------------------------");
		outMT.println("-----------------------------------------");
		
		//Trovo gli stati di tali followers con la stessa query
		ArrayList<String> appoggioStatiFollowers = new ArrayList<String>();
		HashMap<String,ArrayList<String>> followingToTweets = new HashMap<String,ArrayList<String>>();
		int lungFollowing = followings.size();
		System.out.println("(((((((TWEET FOLLOWING))))))))");
		outMT.println("(((((((TWEET FOLLOWING))))))))");
		for (int i = 0; i < lungFollowing; i++) {
			String statoFinaleFol = stato+" from:"+followings.get(i);
			appoggioStatiFollowers = this.query(statoFinaleFol, 100, dataStart);
			System.out.println("TUTTI I TWEET ASSOCIATI A: "+statoFinaleFol);
			outMT.println("TUTTI I TWEET ASSOCIATI A: "+statoFinaleFol);
			for  (int k = 0; k<appoggioStatiFollowers.size(); k++) {
	        	outMT.println(appoggioStatiFollowers.get(k));
	        }
			followingToTweets.put(followings.get(i), appoggioStatiFollowers); //mappa follower:tutti i suoi stati/tweet su quella query/topic
		}
		System.out.println("(((((((MAPPA CREATA))))))))");
		outMT.println("(((((((MAPPA CREATA))))))))");
		
		//Stampa
		System.out.println("--------------STAMPA FINALE--------------------");
		outMT.println("--------------STAMPA FINALE--------------------");
		for (String following: followingToTweets.keySet()) {
			System.out.println("FOLLOWING: "+following);
			outMT.println("FOLLOWING: "+following);
			System.out.println("TWEETS:");
			outMT.println("TWEETS:");
			for (String tweet: followingToTweets.get(following)) {
				System.out.println(tweet);
				outMT.println(tweet);
			}
		}
		
		System.out.println("-------------------THE END----------------------");
		outMT.println("-------------------THE END----------------------");
		
	    outMT.close();
		return followingToTweets;
	}
	
	//i followers con FollowersList
	public ArrayList<String> followersOfUser(String user, int numFollowers) throws TwitterException {
		ArrayList<String> followers = new ArrayList<String>();
		PagableResponseList<User> followersUser = null;
		//MainTwitter mt = new MainTwitter();
		Twitter twitter = this.getTwitter();
		
		long cursor = -1;
		System.out.println("LOADING");
		int j = 0;
		do {
			System.out.println("CURSOR:"+cursor);
			followersUser = twitter.getFollowersList(user, cursor, numFollowers);
			//System.out.println("FATTO");
			j = j + 200; //j = j+200
			//System.out.println("J"+j);
			for (User utente: followersUser) {
				followers.add(utente.getScreenName()); // qui il nome con @
			}
			System.out.println("GETNEXT "+followersUser.getNextCursor()); //il prossimo cursor
		}
		while ((cursor = followersUser.getNextCursor()) != 0 && j < 3000); //j < 3000
		
        /*System.out.println("I FOLLOWERS DI: "+user);
        for  (int i = 0; i<followers.size(); i++) {
        	System.out.println(followers.get(i));
        	System.out.println(i);
        }*/
		
		return followers;
	}
	
	
	//i followings con l'id
	public ArrayList<String> followingsOfUser(String user) throws TwitterException {
		ArrayList<String> followings = new ArrayList<String>();
		//MainTwitter mt = new MainTwitter();
		Twitter twitter = this.getTwitter();
		
		long cursor = -1;
	    IDs ids;
	    System.out.println("Listing following's ids.");
	    do {
        	ids = twitter.getFriendsIDs(user, cursor); 
	        for (long id : ids.getIDs()) {
	              User utente = twitter.showUser(id);
	              //followers.add(utente.getName()); // salvo i followers in una lista, qui il nome
	              followings.add(utente.getScreenName()); // qui il nome con @
	        }
	    } 
        while ((cursor = ids.getNextCursor()) != 0);
	    
	    System.out.println("TUTTI I FOLLOWINGS DI: "+user);
        for  (int i = 0; i<followings.size(); i++) {
        	System.out.println(followings.get(i));
        }
	    
		return followings;
	}
	
    private ArrayList<String> query(String stato, int limite, String dataStart) throws TwitterException {
    	ArrayList<String> tweets = new ArrayList<String>();
		//MainTwitter mt = new MainTwitter();
		Twitter twitter = this.getTwitter();
		/*String stato1 = "#Totti"; //FRIEND is to be replaced by your choice of search key
		String stato2 = "#Roma";*/
		Query query = new Query(stato); //+" OR "+ stato2
		query.count(limite); // Limit of resultset
		query.setSince(dataStart); // Start date of search 
		//query.setUntil("2015-07-14"); //il limite temporale sembrerebbe al massimo 7-8 giorni nel passato
		QueryResult result = twitter.search(query);
		System.out.println("Count : " + result.getTweets().size()) ;
		for (Status tweet : result.getTweets()) {
		    //provare il getLang()
			//System.out.println("text : " + tweet.getText());
			tweets.add(tweet.getText());
		}
		System.out.println("TUTTI I TWEET ASSOCIATI A: "+stato);
		for  (int i = 0; i<tweets.size(); i++) {
        	System.out.println(tweets.get(i));
        }
		return tweets;
    }
	
	private void updateStatus(String latestStatus) {
		
	    try {
	       //MainTwitter esempi = new MainTwitter();
	       Twitter twitter = this.getTwitter();
	       System.out.println(twitter.getScreenName());
	       Status status = twitter.updateStatus(latestStatus);
	       System.out.println("Successfully updated the status to [" + status.getText() + "].");
	    }
	    catch (TwitterException te) {
	           te.printStackTrace();
	           System.exit(-1);
	    }
	}
	
	public Status newStatus(String text) throws TwitterException {
		Twitter twitter = this.getTwitter();
		Status status = twitter.updateStatus(text);
		return status;
	}

	
	public static void main(String[] args) throws TwitterException, IOException {
		MainTwitter mt = new MainTwitter();
		//Twitter twitter = mt.getTwitter();
		String stato = "Forza Roma #Totti #ASR";
		mt.updateStatus(stato);
		
		//String query = "#Totti";
		String user = ""; //
		//String dataStart = "2015-09-01";
		//mt.followingsOfUser(user);
		//mt.userFollowersOnTopic(query, user, dataStart);
		//mt.query(query, 100, dataStart);
		
		mt.followersOfUser(user, 50);
		
		//mt.tweetsOfUser(user, 50);
		
		
		
	}
	
}




