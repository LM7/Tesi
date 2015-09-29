package main;

import java.io.IOException;
import java.util.ArrayList;

import sentiment.SentiStrengthMain;
import sentiment.SentiWordNetDemoCode;

public class TakeSentiment {

	public static void main(String[] args) throws IOException {
		/*Probabilmente dovrò accedere al database per recuperare i tweet di cui bisogna analizzare il sentiment*/
		SentiStrengthMain ss = new SentiStrengthMain();
		ArrayList<String> listaTweet = new ArrayList<String>(); //inserire i tweet in questa lista
		
		ArrayList<Integer> valoriProve = new ArrayList<Integer>();
		ArrayList<String> moodSentiStrength = new ArrayList<String>();
		valoriProve = ss.sentiment(listaTweet);
		moodSentiStrength = ss.calculator(valoriProve);
		
		String pathToSWN = "SentiWordNet.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		ArrayList<String> moodSentiWordNet = new ArrayList<String>();
		moodSentiWordNet = sentiwordnet.scoreTweets(listaTweet);
		
		/*fare una sorta di combinazione dei risultati...*/
	}

}
