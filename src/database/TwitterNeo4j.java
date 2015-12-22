package database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.GraphDatabaseAPI;

import twitter4j.HashtagEntity;
import twitter4j.MainTwitter;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

public class TwitterNeo4j {

	private static enum RelType implements RelationshipType
	{
		MENTION,
		TWEET,
		HASHTAG,
		URL,
		MEDIA,
		RT
	}

	
	private static String DB_PATH = "";
	
	private Index _entitiesIndex ;

	private Index _UserIndex ;
	private Index _TweetIndex ;
	private Index _URLIndex ;
	private Index _HashtagIndex ;
	private Index _MediaIndex ;
	private Index _relMENTION;
	private Index _relTWEET;
	private Index _relHASHTAG;
	private Index _relURL;
	private Index _relMEDIA;
	private Index _relRT;

	GraphDatabaseService _graphDB;

	static int _relcounter = 0;
	public TwitterNeo4j()
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
		_URLIndex = _graphDB.index().forNodes( "URL" );
		_HashtagIndex= _graphDB.index().forNodes( "Hashtag" ); 
		_MediaIndex= _graphDB.index().forNodes( "Media" ); 

		_relMENTION = _graphDB.index().forRelationships("Mention");
		_relTWEET = _graphDB.index().forRelationships("Tweet");
		_relHASHTAG = _graphDB.index().forRelationships("Hashtag");
		_relURL = _graphDB.index().forRelationships("URL");
		_relMEDIA = _graphDB.index().forRelationships("Media");
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
		if(type.equals("URL"))
		{
			_URLIndex.add(statusNode, "tweet_id", id);
		}
		if(type.equals("Media"))
		{
			_MediaIndex.add(statusNode, "tweet_id", id);
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
	public void newStatus(Status s)
	{
		Transaction tx = _graphDB.beginTx();
		try
		{
			Node statusNode = createNodeType(s.getId(),"Tweet");
			statusNode.setProperty("text", s.getText());
			if(s.getGeoLocation()!=null)
			{
				statusNode.setProperty("Geo_Lat", s.getGeoLocation().getLatitude());
				statusNode.setProperty("Geo_Long", s.getGeoLocation().getLongitude());
			}

			Node user = createNodeType("@"+s.getUser().getScreenName().toLowerCase(),"User");
			user.setProperty("name", s.getUser().getName());

			_relTWEET.add(user.createRelationshipTo(statusNode, RelType.TWEET),"rel_id",_relcounter++);

			if(s.isRetweet())
			{
				Status rts =s.getRetweetedStatus();
				Node retweetedUser = createNodeType("@"+rts.getUser().getScreenName(),"User");
				retweetedUser.setProperty("name", rts.getUser().getName());

				Node retweetedTweet = createNodeType(rts.getId(),"Tweet");
				retweetedTweet.setProperty("text", rts.getText());
				if(rts.getGeoLocation()!=null)
				{
					retweetedTweet.setProperty("Geo_Lat", rts.getGeoLocation().getLatitude());
					retweetedTweet.setProperty("Geo_Long", rts.getGeoLocation().getLongitude());
				}
				_relTWEET.add(retweetedUser.createRelationshipTo(retweetedTweet, RelType.TWEET),"rel_id",_relcounter++);
				_relRT.add(user.createRelationshipTo(retweetedUser, RelType.RT),"rel_id",_relcounter++);
				_relRT.add(user.createRelationshipTo(retweetedTweet, RelType.RT),"rel_id",_relcounter++);

			}

			for(UserMentionEntity me:s.getUserMentionEntities())
			{
				if(!s.isRetweet() || (!me.getScreenName().equals(s.getRetweetedStatus().getUser().getScreenName())) )
				{
					Node mention = createNodeType("@"+me.getScreenName().toLowerCase(),"User");

					_relMENTION.add(statusNode.createRelationshipTo(mention, RelType.MENTION),"rel_id",_relcounter++);
				}
			}

			for(HashtagEntity hs:s.getHashtagEntities())
			{

				Node mention = createNodeType("#"+hs.getText().toLowerCase(),"Hashtag");
				_relHASHTAG.add(statusNode.createRelationshipTo(mention, RelType.HASHTAG),"rel_id",_relcounter++);

			}

			for(URLEntity url:s.getURLEntities())
			{
				Node mention = createNodeType(url.getExpandedURL(),"URL");
				_relURL.add(statusNode.createRelationshipTo(mention, RelType.URL),"rel_id",_relcounter++);
			}

			for(MediaEntity media:s.getMediaEntities())
			{
				Node mention = createNodeType(media.getExpandedURL(),"Media");

				_relMEDIA.add(statusNode.createRelationshipTo(mention, RelType.MEDIA),"rel_id",_relcounter++);
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
	
	public static void main(String[] args) throws TwitterException {
		TwitterNeo4j neo = new TwitterNeo4j();
		MainTwitter mt = new MainTwitter();
		Status status = mt.newStatus("TwitterNeo4j #Java #Twitter4j");
		neo.newStatus(status);
	}
	
	
	

}
