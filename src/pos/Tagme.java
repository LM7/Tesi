package pos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Tagme {

	public static HashMap<String, List<String>> categories(String text) throws IOException {
		/*Richiesta verso TagMe*/
		URL url= new URL("http://tagme.di.unipi.it/tag");
		HttpURLConnection con=(HttpURLConnection) url.openConnection(); 

		//add request header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5"); //"en-US,en;q=0.5"
		
		String lang = "it"; //en
		String urlParameters = "key=8020b57e2d41b6041c4fd06937acbec7&text="+text+"&include_categories=true&lang="+lang;

		//send post request
		try {
		con.setDoOutput(true);
		}catch(Exception e) {}
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(urlParameters.getBytes("UTF-8"));
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		/*System.out.println("--------------------------------------------------------");
		System.out.println("Sending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		System.out.println("--------------------------------------------------------");*/
		
		if(responseCode!=200){
			urlParameters = "key=41480047b3428dcfe6a5c1bba1f0a93e&text="+text+"&include_categories=true";

			//send post request
			try{
			con.setDoOutput(true);
			}catch(Exception e) {
				System.out.println(e.getMessage());
				System.out.println("NEL CATCH");
			}
			wr = new DataOutputStream(con.getOutputStream());
			wr.write(urlParameters.getBytes("UTF-8"));
			wr.flush();
			wr.close();

			responseCode = con.getResponseCode();
			/*System.out.println("--------------------------------------------------------");
			System.out.println("Sending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			System.out.println("--------------------------------------------------------");*/
			
			if(responseCode!=200){
				System.out.println("MALE");
			}
		}
		

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		//System.out.println(response.toString());
		//System.out.println("--------------------------------------------------------");

		/* Elaborazione dei risultati*/
		Parser p = new Parser(response.toString());
		HashMap<String, List<String>> tagMeResult = p.processingReply();
		
		
		//provo a stampare
		/*for (String parola: tagMeResult.keySet()) {
			System.out.println("CHIAVE: "+ parola);
			
			for (String categoria: tagMeResult.get(parola) ) {
				System.out.println("CATEGORIA: "+ categoria);
			}
		}*/
		return tagMeResult;
	}
	
	public static String sortABC(String stringa) {
		String[] splits = stringa.split(" ");
		Arrays.sort(splits);
		String ordinata = "";
		for (int i = 0; i < splits.length; i++) {
			ordinata = ordinata + " " + splits[i];
		}
		ordinata = ordinata.trim();
		return ordinata;
	}
	
	public static String joinChiavi(Set<String> chiavi) {
		String sommaChiavi = "";
		for (String chiave: chiavi) {
			sommaChiavi = chiave + " " + sommaChiavi;
		}
		sommaChiavi = sommaChiavi.trim();
		return sommaChiavi;
	}
	
	public static HashMap<String, Integer>  updateMap(HashMap<String, Integer> mappa, Set<String> chiavi) {
		int valore = 0;
		for (String chiave: chiavi) {
			if ( !(mappa.containsKey(chiave)) ) {
				mappa.put(chiave, 1);
			}
			else {
				valore = mappa.get(chiave);
				valore++;
				mappa.put(chiave, valore);
			}
		}
		return mappa;
	}
	
	public static void main(String[] args) throws IOException {
		FileWriter file = new FileWriter("topic/ListaTopic.txt");
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("topic/ListaTitoli.txt"));
		String line = reader.readLine();
		HashMap<String, List<String>> mappa = new HashMap<String, List<String>>();
		HashMap<String, Integer> keyToNumber = new HashMap<String, Integer>();
		String sommaChiavi = "";
		//String finale = "";
		while (line != null) {
			System.out.println(line);
			line = line.replaceAll("%", "percent");
			mappa = Tagme.categories(line);
			keyToNumber = updateMap(keyToNumber, mappa.keySet());
			sommaChiavi = joinChiavi(mappa.keySet());
			//finale = sortABC(sommaChiavi); 
			outFile.println(sommaChiavi); //finale
			mappa.clear();
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
		
		FileWriter file2 = new FileWriter("topic/query.txt");
		PrintWriter outFile2 = new PrintWriter(file2);
		for (String chiave : keyToNumber.keySet()) {
			if (keyToNumber.get(chiave) >= 3) {
				outFile2.println(chiave);
				System.out.println(chiave+ ":"+keyToNumber.get(chiave));
			}
		}
		outFile2.close();
	}
}

