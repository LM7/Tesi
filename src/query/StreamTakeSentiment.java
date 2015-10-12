package query;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import sentiment.SentiStrengthMain;
import sentiment.SentiWordNetDemoCode;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class StreamTakeSentiment {

	public static void main(String[] args) throws IOException {
		//FileWriter file = new FileWriter("DatabaseStream.txt");
		//PrintWriter outStream = new PrintWriter(file);
		MongoClient mongo = null;
		try {
			mongo = new MongoClient("localhost", 27017);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		DB db = mongo.getDB("db");
		DBCollection collection = db.getCollection("streamCollection");
		//System.out.println(collection.count());
		DBCursor cursor = collection.find();
		/*String s;
		while (cursor.hasNext()) {
			s = cursor.next().toString();
			outStream.println(s);
		}
		outStream.close();*/
		ArrayList<String> listaTweet = new ArrayList<String>();
		String text;
		while (cursor.hasNext()) {
			text = cursor.next().get("text").toString();
			listaTweet.add(text);
		}
		
		//SentiStrength
		SentiStrengthMain ss = new SentiStrengthMain();
		ArrayList<Integer> valoriProve = new ArrayList<Integer>();
		ArrayList<String> moodSentiStrength = new ArrayList<String>();
		valoriProve = ss.sentiment(listaTweet);
		moodSentiStrength = ss.calculator(valoriProve);
		int pos = 0;
		int neg = 0;
		int neu = 0;
		
		for (String stringa: moodSentiStrength) {
			if (stringa.contains("negative")) {
				neg++;
			}
			if (stringa.contains("positive")) {
				pos++;
			}
			if (stringa.contains("neutral")) {
				neu++;
			}
		}
		System.out.println("SENTISTRENGTH");
		System.out.println("POSITIVE: "+pos);
		System.out.println("NEGATIVE: "+neg);
		System.out.println("NEUTRAL: "+neu);
		
		//SentiWordNet
		String pathToSWN = "SentiWordNet.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		ArrayList<String> moodSentiWordNet = new ArrayList<String>();
		moodSentiWordNet = sentiwordnet.scoreTweets(listaTweet);
		int posSWN = 0;
		int negSWN = 0;
		int neuSWN = 0;
		int vpos = 0;
		int vneg = 0;
		
		for (String stringa: moodSentiWordNet) {
			if (stringa.contains("negative")) {
				negSWN++;
			}
			if (stringa.contains("positive")) {
				posSWN++;
			}
			if (stringa.contains("neutral")) {
				neuSWN++;
			}
			if (stringa.contains("very neg")) {
				vneg++;
			}
			if (stringa.contains("very pos")) {
				vpos++;
			}
		}
		System.out.println();
		
		
		System.out.println("POSITIVE: "+posSWN);
		System.out.println("NEGATIVE: "+negSWN);
		System.out.println("NEUTRAL: "+neuSWN);
		System.out.println("VERY POSITIVE: "+vpos);
		System.out.println("VERY NEGATIVE: "+vneg);
		
	}

}
