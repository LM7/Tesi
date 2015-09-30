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
		TWITT,
		HASHTAG,
		URL,
		MEDIA,
		RT
	}

	private static String DB_PATH = "/Users/lorenzomartucci/Desktop/UniRomaTre/Tesi/neo4j-community-2.3.0-M01/data/graph.db";
	private Index _entitiesIndex ;

	private Index _UserIndex ;
	private Index _TwittIndex ;
	private Index _URLIndex ;
	private Index _HashtagIndex ;
	private Index _MediaIndex ;
	private Index _relMENTION;
	private Index _relTWITT;
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
		_TwittIndex= _graphDB.index().forNodes( "Twitt" ); 
		_URLIndex = _graphDB.index().forNodes( "URL" );
		_HashtagIndex= _graphDB.index().forNodes( "Hashtag" ); 
		_MediaIndex= _graphDB.index().forNodes( "Media" ); 

		_relMENTION = _graphDB.index().forRelationships("Mention");
		_relTWITT = _graphDB.index().forRelationships("Twitt");
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
			_UserIndex.add(statusNode, "twitt_id", id);
		}
		if(type.equals("Hashtag"))
		{
			_HashtagIndex.add(statusNode, "twitt_id", id);
		}
		if(type.equals("URL"))
		{
			_URLIndex.add(statusNode, "twitt_id", id);
		}
		if(type.equals("Media"))
		{
			_MediaIndex.add(statusNode, "twitt_id", id);
		}
		if(type.equals("Twitt"))
		{
			_TwittIndex.add(statusNode, "twitt_id", id);
		}
		return statusNode;
	}
	private Node createNode(Object id)
	{
		Node statusNode;
		statusNode = (Node) _entitiesIndex.get( "twitt_id", id).getSingle(); //CAST!!!
		if(statusNode!=null)
		{
			return statusNode;
		}

		statusNode = _graphDB.createNode();
		statusNode.setProperty("twitt_id",id);
		_entitiesIndex.add( statusNode, "twitt_id", id );
		return statusNode;
	}
	public void newStatus(Status s)
	{
		Transaction tx = _graphDB.beginTx();
		try
		{
			Node statusNode = createNodeType(s.getId(),"Twitt");
			statusNode.setProperty("text", s.getText());
			if(s.getGeoLocation()!=null)
			{
				statusNode.setProperty("Geo_Lat", s.getGeoLocation().getLatitude());
				statusNode.setProperty("Geo_Long", s.getGeoLocation().getLongitude());
			}

			Node user = createNodeType("@"+s.getUser().getScreenName().toLowerCase(),"User");
			user.setProperty("name", s.getUser().getName());

			_relTWITT.add(user.createRelationshipTo(statusNode, RelType.TWITT),"rel_id",_relcounter++);

			if(s.isRetweet())
			{
				Status rts =s.getRetweetedStatus();
				Node retweetedUser = createNodeType("@"+rts.getUser().getScreenName(),"User");
				retweetedUser.setProperty("name", rts.getUser().getName());

				Node retweetedTwitt = createNodeType(rts.getId(),"Twitt");
				retweetedTwitt.setProperty("text", rts.getText());
				if(rts.getGeoLocation()!=null)
				{
					retweetedTwitt.setProperty("Geo_Lat", rts.getGeoLocation().getLatitude());
					retweetedTwitt.setProperty("Geo_Long", rts.getGeoLocation().getLongitude());
				}
				_relTWITT.add(retweetedUser.createRelationshipTo(retweetedTwitt, RelType.TWITT),"rel_id",_relcounter++);
				_relRT.add(user.createRelationshipTo(retweetedUser, RelType.RT),"rel_id",_relcounter++);
				_relRT.add(user.createRelationshipTo(retweetedTwitt, RelType.RT),"rel_id",_relcounter++);

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
