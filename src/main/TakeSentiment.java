package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pos.Extractor;
import sentiment.SentiStrengthMain;
import twitter4j.MainTwitter;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;


public class TakeSentiment {
	
	public static void main(String[] args) throws TwitterException, FileNotFoundException, IOException {
		PrintWriter outSouthManUtd = new PrintWriter("TweetGiusti.txt", "UTF-8");
		MainTwitter mt = new MainTwitter();
		Extractor eee = new Extractor();
		List<String> hashtags = new ArrayList<String>();
		boolean tagRight = false;
		SentiStrengthMain ss = new SentiStrengthMain();
		ArrayList<String> prove = new ArrayList<String>();
		ArrayList<Integer> valoriProve = new ArrayList<Integer>();
		ArrayList<String> proveMood = new ArrayList<String>();
		String user = "ManUtd";
		ArrayList<String> followersUTD = new ArrayList<String>();
		//followersUTD = mt.followersOfUser(user,50); //dovrò mettere un limite al numero di utenti presi
		followersUTD.add("Gm4nHere");
		followersUTD.add("Ahmad99MUFC");
		followersUTD.add("DigitalDaggers");
		followersUTD.add("ryantosh14");
		HashMap<String, ArrayList<String>> utenteToStati = new HashMap<String, ArrayList<String>>();
		ResponseList<Status> statiTemp = null;
		ArrayList<String> statiGiusti = new ArrayList<String>();
		//Ho per ogni utente una lista di stati
		for (String utente: followersUTD) {
			try {
				statiTemp = mt.tweetsOfUser(utente, 100);
			} catch (Exception e) {
				System.out.println("errorino");
			}
			//ma voglio quelli di una particolare data, con un particolare hashtag
			for (Status stato: statiTemp) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date data = stato.getCreatedAt();
				String dataStringa = sdf.format(data);
				hashtags = eee.extractHashtags(stato.getText());
				for (String stringa: hashtags) {
					tagRight = stringa.equalsIgnoreCase("mufc");
					if (dataStringa.equals("2015-09-15") && tagRight  ) { //data non uguale oppure #non uguale->rimuovo
						statiGiusti.add(stato.getText());
						outSouthManUtd.println("UTENTE: "+utente+"; stato: "+stato.getText());
						prove.add(stato.getText());
					}
				}
				
			}
			utenteToStati.put(utente, statiGiusti);
			
			if ( !(statiTemp.isEmpty()) || statiTemp != null ) {
				statiTemp.clear();
			}
			if ( !(statiGiusti.isEmpty()) || statiGiusti != null ) {
				statiGiusti.clear();
			}
		}
		System.out.println("SIZE: "+utenteToStati.size());
		
		valoriProve = ss.sentiment(prove);
		proveMood = ss.calculator(valoriProve);
		for (String stringa: proveMood) {
			System.out.println(stringa);
		}
		
		outSouthManUtd.close();
	}
	

}
