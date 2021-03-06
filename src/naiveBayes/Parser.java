package naiveBayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import cleanForFile.StopwordsPoints;

public class Parser {
	
	
	
	public static void trainingForClassifier(File file) throws Exception {
		PrintWriter outFile = new PrintWriter("NaiveBayesWindows10/trainingReady.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				outFile.print(splits[1]+": ");
			}
			else {
				if ( !(line.startsWith("DATE: ")) && !(line.matches("\\d{4}-\\d{2}-\\d{2}")) && !(line.equals("positive")) && !(line.equals("negative")) && !(line.equals("neutral")) ) {
					line = StopwordsPoints.removeStopWord(line);
					line = StopwordsPoints.removePoints(line);
					outFile.print(line);
				}
				else if  ( (line.equals("positive")) || (line.equals("negative")) || (line.equals("neutral")) ) {
					outFile.println("."+line);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	public static void testingForClassifier(File file) throws Exception {
		PrintWriter outFile = new PrintWriter("NaiveBayesWindows10/testingPastReady.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				outFile.print(splits[1]+": ");
			}
			else {
				if ( !(line.startsWith("DATE: ")) && !(line.equals("")) && !(line.matches("\\d{4}-\\d{2}-\\d{2}")) ) { // && !(line.matches("\\d{4}-\\d{2}-\\d{2}"))
					line = StopwordsPoints.removeStopWord(line);
					line = StopwordsPoints.removePoints(line);
					outFile.println(line);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	/*public static void testingPastYearMonthForClassifier(File file) throws Exception {
		PrintWriter outFile = new PrintWriter("NaiveBayes/testingPastDate.txt");
		FileForExcel ff = new FileForExcel();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		String data = "";
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				outFile.print(splits[1]+": ");
			}
			else {
				if ( !(line.equals("")) && !(line.matches("\\d{4}-\\d{2}-\\d{2}")) ) { // && !(line.matches("\\d{4}-\\d{2}-\\d{2}"))
					if (line.startsWith("DATE: ")) {
						splits = line.split(" ");
						data = splits[1];
					}
					else {
						line = ff.removeStopWord(line);
						line = ff.removePoints(line);
						outFile.print(line);
						outFile.println(data);
					}
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}*/
	
	public static String deleteURL(String stringa) {
		if (stringa.contains("http")) {
			String[] splits;
			splits = stringa.split(" ");
			for (int i = 0; i < splits.length; i++) {
				if (splits[i].startsWith("http")) {
					splits[i] = "";
				}
			}
			stringa = "";
			for (int j = 0; j <splits.length; j++) {
				if (j == 0) {
					stringa = splits[j];
				}
				else {
					stringa = stringa + " " +splits[j];
				}
			}
		}
		return stringa;
	}
	
	public static void cleanFile() throws Exception {
		PrintWriter outFile = new PrintWriter("DatiWindows10/pastClean.txt");
		BufferedReader reader = new BufferedReader(new FileReader("DatiWindows10/past.txt"));
		String line = reader.readLine();
		int cont = 0;
		while (line != null) {
			line = deleteURL(line);
			if (line.startsWith("USER:")) {
				if (cont == 1) {
					outFile.println();
				}
				outFile.println(line);
				cont = 0;
			}
			else {
				if ( (line.startsWith("DATE: ")) ) {
					outFile.println(line);
				}
				else if ( !(line.equals("")) ) {
					outFile.print(line+ " ");
					cont = 1;
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	
	
	public static void main(String[] args) throws Exception {
		//cleanFile(); //prima pulizia da url e a capo
		//File filetraining = new File("DatiWindows10/trainingWindows10.txt");
		//File filetesting = new File("DatiWindows10/tweetClean.txt");
		File filetesting = new File("DatiWindows10/pastClean.txt");
		//trainingForClassifier(filetraining);
		testingForClassifier(filetesting);
		//testingPastYearMonthForClassifier(filetesting);
	}

}
