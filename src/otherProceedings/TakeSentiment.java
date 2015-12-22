package otherProceedings;

/*
 * Questa classe ?? utile sia per il processo "folloers" sia per quello "query dirette"
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sentiment.SentiStrengthMain;
import sentiment.SentiWordNetDemoCode;

public class TakeSentiment {

	public static void main(String[] args) throws IOException, ParseException {
		ArrayList<String> listaTweet = new ArrayList<String>(); //inserire i tweet in questa lista
		ArrayList<String> afterBefore = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		Date dateScandal;
		dateScandal = sdf.parse("2015-09-18");
		String fraseLinea;
		BufferedReader reader = new BufferedReader(new FileReader("RightTweets.txt")); //RightTweets.txt
		String line = reader.readLine();
		int i = 0;
		while (line != null) {
			fraseLinea = line.toString();
			if (line.matches("\\d{4}-\\d{2}-\\d{2}")) { //da controllare sta regex.....
				String data = fraseLinea; //prendo la data
				date = sdf.parse(data); //la trasformo per paragonarla alla DataPrincipale
				if (date.after(dateScandal) || data.equals("2015-09-18")) { //alla fine avr?? una lista di after e before da associare alla lista di sentiment...tutto in ordine
					afterBefore.add("after");
				}
				else {
					afterBefore.add("before");
				}
			}
			else if ( !(fraseLinea.equals("")) && !(fraseLinea.startsWith("USER:"))  ) { //&& !(fraseLinea.startsWith("POSIZIONE GEOGRAFICA:")) && !(fraseLinea.startsWith("LINGUA:"))
				listaTweet.add(fraseLinea);
				i++;
				System.out.println(i);
			}
			line = reader.readLine();
			
		}
		
		System.out.println("SIZE"+afterBefore.size());
		/*int kappa=0;
		for (String stringa :afterBefore) {
			System.out.println("STRINGA "+stringa);
			System.out.println("AB "+afterBefore.get(kappa));
			kappa++;
		}*/
		
		/*Annalizzo i risultati con SentiStrength*/
		int posAfter, negAfter, neutAfter, posBefore, negBefore, neutBefore;
		posAfter = 0;
		negAfter = 0;
		neutAfter = 0;
		posBefore = 0;
		negBefore = 0;
		neutBefore = 0;
		
		SentiStrengthMain ss = new SentiStrengthMain();
		ArrayList<Integer> valoriProve = new ArrayList<Integer>();
		ArrayList<String> moodSentiStrength = new ArrayList<String>();
		valoriProve = ss.sentiment(listaTweet);
		moodSentiStrength = ss.calculator(valoriProve);
		
		int posizione = 0;
		for (String mood: moodSentiStrength) {
			if (mood.contains("positive") && afterBefore.get(posizione).equals("after")) {
				posAfter++;
			}
			if (mood.contains("negative") && afterBefore.get(posizione).equals("after")) {
				negAfter++;
			}
			if (mood.contains("neutral") && afterBefore.get(posizione).equals("after")) {
				neutAfter++;
			}
			if (mood.contains("positive") && afterBefore.get(posizione).equals("before")) {
				posBefore++;
			}
			if (mood.contains("negative") && afterBefore.get(posizione).equals("before")) {
				negBefore++;
			}
			if (mood.contains("neutral") && afterBefore.get(posizione).equals("before")) {
				neutBefore++;
			}
			posizione++;
		}
		
		System.out.println("SENTISTRENGTH");
		System.out.println("POSAFTER: "+posAfter);
		System.out.println("NEGAFTER: "+negAfter);
		System.out.println("NEUTAFTER: "+neutAfter);
		System.out.println("POSBEFORE: "+posBefore);
		System.out.println("NEGBEFORE: "+negBefore);
		System.out.println("NEUTBEFORE: "+neutBefore);
		
		System.out.println();
		/*Analizzo i risultati con SentiWordNet*/
		int posAfterSWN, negAfterSWN, neutAfterSWN, posBeforeSWN, negBeforeSWN, neutBeforeSWN, veryNegAfter, veryNegBefore, veryPosAfter, veryPosBefore;
		posAfterSWN = 0;
		negAfterSWN = 0;
		neutAfterSWN = 0;
		posBeforeSWN = 0;
		negBeforeSWN = 0;
		neutBeforeSWN = 0;
		veryNegAfter = 0;
		veryNegBefore = 0;
		veryPosAfter = 0;
		veryPosBefore = 0;
		
		
		String pathToSWN = "SentiWordNet.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		ArrayList<String> moodSentiWordNet = new ArrayList<String>();
		moodSentiWordNet = sentiwordnet.scoreTweets(listaTweet);
		
		
		
		
		int posizioneSWN = 0;
		for (String mood: moodSentiWordNet) {
			if (mood.contains("positive") && afterBefore.get(posizioneSWN).equals("after")) {
				posAfterSWN++;
			}
			if (mood.contains("negative") && afterBefore.get(posizioneSWN).equals("after")) {
				negAfterSWN++;
			}
			if (mood.contains("neutral") && afterBefore.get(posizioneSWN).equals("after")) {
				neutAfterSWN++;
			}
			if (mood.contains("positive") && afterBefore.get(posizioneSWN).equals("before")) {
				posBeforeSWN++;
			}
			if (mood.contains("negative") && afterBefore.get(posizioneSWN).equals("before")) {
				negBeforeSWN++;
			}
			if (mood.contains("neutral") && afterBefore.get(posizioneSWN).equals("before")) {
				neutBeforeSWN++;
			}
			if (mood.contains("very pos") && afterBefore.get(posizioneSWN).equals("after")) {
				veryPosAfter++;
			}
			if (mood.contains("very neg") && afterBefore.get(posizioneSWN).equals("after")) {
				veryNegAfter++;
			}
			if (mood.contains("very pos") && afterBefore.get(posizioneSWN).equals("before")) {
				veryPosBefore++;
			}
			if (mood.contains("very neg") && afterBefore.get(posizioneSWN).equals("before")) {
				veryNegBefore++;
			}
			posizioneSWN++;
		}
		
		System.out.println("SENTIWORDNET");
		System.out.println("POSAFTER: "+posAfterSWN);
		System.out.println("NEGAFTER: "+negAfterSWN);
		System.out.println("NEUTAFTER: "+neutAfterSWN);
		System.out.println("POSBEFORE: "+posBeforeSWN);
		System.out.println("NEGBEFORE: "+negBeforeSWN);
		System.out.println("NEUTBEFORE: "+neutBeforeSWN);
		System.out.println("VERYPOSAFTER: "+veryPosAfter);
		System.out.println("VERYNEGAFTER: "+veryNegAfter);
		System.out.println("VERYPOSBEFORE: "+veryPosBefore);
		System.out.println("VERYNEGBEFORE: "+veryNegBefore);
	}

}
