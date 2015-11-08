package naiveBayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import weka.FileForExcel;

public class Parser {
	
	
	//Eliminare lo spazio dopo RT a mano...
	public static void trainingForClassifier(File file) throws Exception {
		PrintWriter outFile = new PrintWriter("NaiveBayes/trainingReady.txt");
		FileForExcel ff = new FileForExcel();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				outFile.print(splits[1]+": ");
			}
			else {
				if ( !(line.startsWith("DATE: ")) && !(line.equals("positive")) && !(line.equals("negative")) && !(line.equals("neutral")) ) {
					line = ff.removeStopWord(line);
					line = ff.removePoints(line);
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
	
	public static void main(String[] args) throws Exception {
		File file = new File("NaiveBayes/training.txt");
		trainingForClassifier(file);
	}

}
