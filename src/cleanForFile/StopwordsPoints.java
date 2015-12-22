package cleanForFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class StopwordsPoints {
	
	public static String removePoints(String text) {
		text = text.replaceAll ("\\.", "");
		text = text.replaceAll ("\\,", "");
		text = text.replaceAll ("\\;", "");
		text = text.replaceAll ("\\:", "");
		text = text.replaceAll ("\\|", "");
		text = text.replaceAll ("\\-", "");
		text = text.replaceAll ("\\/", "");
		text = text.replaceAll ("\\\\", "");
		text = text.replaceAll ("\\?", "");
		text = text.replaceAll ("\\!", "");
		text = text.replaceAll ("\\\'", "");
		text = text.replaceAll ("\\\"", "");
		text = text.replaceAll ("\\_", "");
		text = text.replaceAll ("\\$", "");
		return text;
	}
	
	public static boolean checkStopWord(String word, File stopWordFile) throws IOException {
		BufferedReader stopWordReader = new BufferedReader(new FileReader(stopWordFile));
		String text2;
		while ((text2 = stopWordReader.readLine()) != null) {
			if(word.equalsIgnoreCase(text2)) {
				stopWordReader.close();
				return true;
			}
		}
		stopWordReader.close();
		return false;
	}
	
	public static String removeStopWord(String text) throws IOException    {
		StringTokenizer tokens = new StringTokenizer(text, " ");
		String newText = "";
		File stopWordFile = new File("stopwordsFile/stopwordsEng.txt");
		while (tokens.hasMoreTokens()) {
			String temp = tokens.nextToken();
			if (!checkStopWord(temp,stopWordFile)) {
				newText += temp + " ";
			}
		}
		text = "";
		text = newText;
		return text;
	}

}
