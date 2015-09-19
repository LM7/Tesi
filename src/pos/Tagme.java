package pos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tagme {

	public static HashMap<String, List<String>> categories(String text) throws IOException {
		/*Richiesta verso TagMe*/
		URL url= new URL("http://tagme.di.unipi.it/tag");
		HttpURLConnection con=(HttpURLConnection) url.openConnection(); 

		//add request header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "key=8020b57e2d41b6041c4fd06937acbec7&text="+text+"&include_categories=true";

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
		for (String parola: tagMeResult.keySet()) {
			System.out.println("CHIAVE: "+ parola);
			
			for (String categoria: tagMeResult.get(parola) ) {
				System.out.println("CATEGORIA: "+ categoria);
			}
		}
		return tagMeResult;
	}
	
	public static void main(String[] args) throws IOException {
		HashMap<String, List<String>> mappa = Tagme.categories("Totti Roma");
	}
}

