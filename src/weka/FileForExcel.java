package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class FileForExcel {
	
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
			if(word.equalsIgnoreCase(text2))
				return true;
		}
		stopWordReader.close();
		return false;
	}
	
	public static String removeStopWord(String text) throws IOException    {
		StringTokenizer tokens = new StringTokenizer(text, " ");
		String newText = "";
		File stopWordFile = new File("WekaMarino/stopwordsIta.txt");
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
	
	
	
	public static void rewriteTXT(File file) throws IOException {
		PrintWriter outFile = new PrintWriter(file);
		BufferedReader reader = new BufferedReader(new FileReader("WekaMarino/training.txt")); //da dove leggere
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			if (line.startsWith("USER: ")) {
				splits = line.split(" ");
				outFile.print(splits[1]+" $ ");
			}
			else {
				if ( !(line.startsWith("DATE: ")) && !(line.equals("positive")) && !(line.equals("negative")) && !(line.equals("neutral")) ) {
					line = removeStopWord(line);
					line = removePoints(line);
					outFile.print(line);
				}
				else if  ( (line.equals("positive")) || (line.equals("negative")) || (line.equals("neutral")) ) {
					outFile.println(" $  "+line);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	/*
	 * Pulisce il file di da stopword, punteggiatura e lo riscrive per il file excel (unica cosa fatta a mano ?? levare la parola Vero vicino al sentiment
	 */
	public static void main(String[] args) throws IOException {
		//File file = new File("WekaMarino/trainingForExcel.txt");
		//rewriteTXT(file);
		//Per eliminare stopword e punteggiatura
	}

}
