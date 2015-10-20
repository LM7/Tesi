package experiment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.MainTwitter;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import database.TwitterNeo4j;

public class InformationLinkUser {
	public static void main(String[] args) throws FileNotFoundException, TwitterException, IOException {
		MainTwitter mt = new MainTwitter();
		Twitter twitter = mt.getTwitter();
		ResponseList<Status> stati = null;
		stati = mt.tweetsOfUser("OfficialASRoma", 1000);
		int cont = 1;
		for (Status stato: stati) {
			System.out.println(cont);
			System.out.println("DATA: "+stato.getCreatedAt()+" e TESTO: "+stato.getText());
			cont++;
		}
		/*long id = (long) (624481934.631170048 * 1000000000);
		RelatedResults results = twitter.getRelatedResults(id);
		List<Status> conversations = results.getTweetsWithConversation();
		/////////
		Status originalStatus = twitter.showStatus(id);
		if (conversations.isEmpty()) {
		    conversations = results.getTweetsWithReply();
		}

		if (conversations.isEmpty()) {
		    conversations = new ArrayList<Status>();
		    Status status = originalStatus;
		    while (status.getInReplyToStatusId() > 0) {
		        status = twitter.showStatus(status.getInReplyToStatusId());
		        conversations.add(status);
		    }
		}
		// show the current message in the conversation, if there's such
		if (!conversations.isEmpty()) {
		    conversations.add(originalStatus);
		}*/
	}
}

