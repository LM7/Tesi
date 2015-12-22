package experiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cleanForFile.StopwordsPoints;
import twitter4j.MainTwitter;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import database.TwitterNeo4j;

public class ExperimentTraining {
	public static void main(String[] args) throws Exception  {
		PrintWriter outFile = new PrintWriter("");
		BufferedReader reader = new BufferedReader(new FileReader(""));
		String line = reader.readLine();
		String user = "";
		String data = "";
		String testo = "";
		while(line!=null) {
			if (line.startsWith("USER:")) {
				user = line;
			}
			else {
				if (line.startsWith("DATE:")) {
					data = line;
				}
				else {
					if ( !(line.equals("")) && !(line.contains("positive")) && !(line.contains("negative")) && !(line.contains("neutral")) ) {
						testo = line;
					}
					else {
						if ( line.equals("positive") || line.equals("negative") || line.equals("neutral")  ) {
							outFile.println(user);
							outFile.println(data);
							outFile.println(testo);
							//outFile.println(line);
							user = "";
							data = "";
							testo = "";
						}
						else if ( line.equals("positiveVero") || line.equals("negativeVero") || line.equals("neutralVerp") ) {
							System.out.println("VERO");
						}
					}
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
}

