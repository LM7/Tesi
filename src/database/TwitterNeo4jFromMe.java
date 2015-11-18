package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import pos.TweetTextMain;
import twitter4j.ExtendedMediaEntity;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MainTwitter;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

public class TwitterNeo4jFromMe {
	
	private static enum RelType implements RelationshipType
	{
		MENTION,
		TWEET,
		HASHTAG,
		RT
	}

	private static String DB_PATH = "/Users/lorenzomartucci/Desktop/UniRomaTre/Tesi/neo4jVolkswagen/data/graph.db";
	private Index _entitiesIndex ;

	private Index _UserIndex ;
	private Index _TweetIndex ;
	private Index _HashtagIndex ;
	private Index _relMENTION;
	private Index _relTWEET;
	private Index _relHASHTAG;
	private Index _relRT;

	GraphDatabaseService _graphDB;

	static int _relcounter = 0;
	public TwitterNeo4jFromMe()
	{
		createDB();
	}
	private void createDB()
	
	{
		
		_graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		try ( Transaction tx = _graphDB.beginTx() )
        {
		_entitiesIndex= _graphDB.index().forNodes( "twitterEntities" );
		_UserIndex= _graphDB.index().forNodes( "User" ); 
		_TweetIndex= _graphDB.index().forNodes( "Tweet" ); 
		_HashtagIndex= _graphDB.index().forNodes( "Hashtag" ); 

		_relMENTION = _graphDB.index().forRelationships("Mention");
		_relTWEET = _graphDB.index().forRelationships("Tweet");
		_relHASHTAG = _graphDB.index().forRelationships("Hashtag");
		_relRT = _graphDB.index().forRelationships("RT");
		
		

		/*WrappingNeoServerBootstrapper srv;
		srv = new WrappingNeoServerBootstrapper((GraphDatabaseAPI)  _graphDB );
		srv.start();*/
		tx.success();
        }

	}
	private Node createNodeType(Object id,String type)
	{
		Node statusNode = createNode(id);
		statusNode.setProperty("type", type);
		if(type.equals("User"))
		{
			_UserIndex.add(statusNode, "tweet_id", id);
		}
		if(type.equals("Hashtag"))
		{
			_HashtagIndex.add(statusNode, "tweet_id", id);
		}
		if(type.equals("Tweet"))
		{
			_TweetIndex.add(statusNode, "tweet_id", id);
		}
		return statusNode;
	}
	private Node createNode(Object id)
	{
		Node statusNode;
		statusNode = (Node) _entitiesIndex.get( "tweet_id", id).getSingle(); //CAST!!!
		if(statusNode!=null)
		{
			return statusNode;
		}

		statusNode = _graphDB.createNode();
		statusNode.setProperty("tweet_id",id);
		_entitiesIndex.add( statusNode, "tweet_id", id );
		return statusNode;
	}
	public void newStatus(String stringa)
	{
		SupportTwitterNeo4jFromMe support = new SupportTwitterNeo4jFromMe();
		Transaction tx = _graphDB.beginTx();
		TweetTextMain tweettext = new TweetTextMain();
		try
		{
			Node statusNode = createNodeType(support.getID(stringa),"Tweet");
			statusNode.setProperty("text", support.getText(stringa)); //DA FARE
			

			Node user = createNodeType("@"+support.getUser(stringa).toLowerCase(),"User");
			user.setProperty("name", support.getUser(stringa));

			_relTWEET.add(user.createRelationshipTo(statusNode, RelType.TWEET),"rel_id",_relcounter++);

			if(support.isRT(stringa))
			{
				//Status rts =s.getRetweetedStatus();
				String[] rts = support.getRT(stringa);
				Node retweetedUser = createNodeType("@"+rts[0],"User");
				retweetedUser.setProperty("name", rts[0]);

				Node retweetedTweet = createNodeType(support.getID(stringa)+rts[0],"Tweet");
				retweetedTweet.setProperty("text", rts[1]);
				_relTWEET.add(retweetedUser.createRelationshipTo(retweetedTweet, RelType.TWEET),"rel_id",_relcounter++);
				_relRT.add(user.createRelationshipTo(retweetedUser, RelType.RT),"rel_id",_relcounter++);
				_relRT.add(user.createRelationshipTo(retweetedTweet, RelType.RT),"rel_id",_relcounter++);

			}
			
			List<String> entities = tweettext.tweetTextScreenName(stringa);

			for(String me:entities)
			{
				/*if(!s.isRetweet() || (!me.getScreenName().equals(s.getRetweetedStatus().getUser().getScreenName())) )
				{*/
					Node mention = createNodeType("@"+me.toLowerCase(),"User");

					_relMENTION.add(statusNode.createRelationshipTo(mention, RelType.MENTION),"rel_id",_relcounter++);
				//}
			}
			
			List<String> hashtags = tweettext.tweetTextHashTags(stringa);

			for(String hs:hashtags)
			{

				Node mention = createNodeType("#"+hs.toLowerCase(),"Hashtag");
				_relHASHTAG.add(statusNode.createRelationshipTo(mention, RelType.HASHTAG),"rel_id",_relcounter++);

			}

			tx.success();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			tx.finish();
		}

	}
	public void closeDB()
	{
		_graphDB.shutdown();
	}
	
	/*Twitterj4neo neo = new Twitterj4neo();

	@Override
	public void onStatus(Status arg0) {

	  neo.newStatus(arg0);
	}*/
	
	public static void main(String[] args) throws TwitterException, Exception {
		TwitterNeo4jFromMe neo = new TwitterNeo4jFromMe();
		BufferedReader reader = new BufferedReader(new FileReader(".txt"));
		String line = reader.readLine();
		int contline = 0;
		while (line != null) {
			neo.newStatus(line);
			line = reader.readLine();
			contline++;
			System.out.println(contline);
		}
		reader.close();
	}

}
