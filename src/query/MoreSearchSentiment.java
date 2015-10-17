package query;

/*
 * Per ora solo SetniWordNet; prima analizzare i tweet, eliminare text, a capo e url
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sentiment.SentiWordNetDemoCode;

public class MoreSearchSentiment {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("NewBearArms.txt"));
		String line = reader.readLine();
		String pathToSWN = "SentiWordNet.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
		ArrayList<String> tweets = new ArrayList<String>();
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> sentiments = new ArrayList<String>();
		while (line != null) {
			String[] splits;
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				users.add(splits[1]);
			}
			else if ( !(line.startsWith("DATE:")) && !(line.equals("")) ) {
				tweets.add(line);
			}
			line = reader.readLine();
		}
		sentiments = sentiwordnet.scoreTweets(tweets);
		reader.close();

		int posSWN, negSWN, neutSWN, veryNeg, veryPos;
		posSWN = 0;
		negSWN = 0;
		neutSWN = 0;
		veryNeg = 0;
		veryPos = 0;

		int posizioneSWN = 0;
		for (String mood: sentiments) {
			if (mood.contains("positive")) {
				posSWN++;
			}
			if (mood.contains("negative")) {
				negSWN++;
			}
			if (mood.contains("neutral")) {
				neutSWN++;
			}
			if (mood.contains("very pos")) {
				veryPos++;
			}
			if (mood.contains("very neg")) {
				veryNeg++;
			}
			posizioneSWN++;
		}

		System.out.println("SENTIWORDNET");
		System.out.println("POS: "+posSWN);
		System.out.println("NEG: "+negSWN);
		System.out.println("NEUT: "+neutSWN);
		System.out.println("VERYPOS: "+veryPos);
		System.out.println("VERYNEG: "+veryNeg);


		//PARTE SECONDA per vedere quali sono i tweet negativi e positivi
		//-------------------------------
		int j = 0;
		boolean avanti = false;
		FileInputStream fstream = null;  
		DataInputStream in = null;
		BufferedWriter out = null;

		try {
			// apro il file
			fstream = new FileInputStream("NewBearArms.txt");

			// prendo l'inputStream
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			StringBuilder fileContent = new StringBuilder();

			// Leggo il file riga per riga
			while ((strLine = br.readLine()) != null) {
				//System.out.println(strLine); // stampo sulla console la riga corrispondente
				if(strLine.equals("")){
					// se la riga è uguale a quella ricercata
					String valoreSent = sentiments.get(j); //scrivere sul file questo
					fileContent.append(valoreSent+System.getProperty("line.separator"));
					avanti = true;
				}
				if (avanti) {
					j++;
					avanti = false;
				}
				else {
					// ... altrimenti la trascrivo così com'è
					fileContent.append(strLine);
					fileContent.append(System.getProperty("line.separator"));
				}
			}

			// Sovrascrivo il file con il nuovo contenuto (aggiornato)
			FileWriter fstreamWrite = new FileWriter("NewBearArms.txt");
			out = new BufferedWriter(fstreamWrite);
			out.write(fileContent.toString());

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// chiusura dell'output e dell'input
			try {
				fstream.close();
				out.flush();
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("DONE");
	}

}
