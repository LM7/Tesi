package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ParserArff {
	
	public static void createTraining(File file) throws Exception {
		FileForExcel ff = new FileForExcel();
		PrintWriter outFile = new PrintWriter("WekaMarino/trainingMarino.arff");
		outFile.println("@relation training");
		outFile.println();
		outFile.println("@attribute User string");
		outFile.println("@attribute Text string");
		outFile.println("@attribute Sentiment {positive,negative,neutral}");
		outFile.println();
		outFile.println();
		outFile.println("@data");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				outFile.print("'"+splits[1]+"','");
			}
			else {
				if ( !(line.startsWith("DATE: ")) && !(line.equals("positive")) && !(line.equals("negative")) && !(line.equals("neutral")) ) {
					line = ff.removeStopWord(line);
					line = ff.removePoints(line);
					outFile.print(line);
				}
				else if  ( (line.equals("positive")) || (line.equals("negative")) || (line.equals("neutral")) ) {
					outFile.println("',"+line);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}
	
	public static void createTesting(File file) throws Exception {
		FileForExcel ff = new FileForExcel();
		PrintWriter outFile = new PrintWriter("WekaMarino/testingMarino.arff");
		outFile.println("@relation testing");
		outFile.println();
		outFile.println("@attribute User string");
		outFile.println("@attribute Text string");
		outFile.println("@attribute Sentiment {positive,negative,neutral}");
		outFile.println();
		outFile.println();
		outFile.println("@data");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String[] splits;
		while (line != null) {
			if (line.startsWith("USER:")) {
				splits = line.split(" ");
				outFile.print("'"+splits[1]+"','");
			}
			else {
				if ( !(line.startsWith("DATE: ")) && !(line.equals("positive")) && !(line.equals("negative")) && !(line.equals("neutral")) ) {
					line = ff.removeStopWord(line);
					line = ff.removePoints(line);
					outFile.print(line);
				}
				else if  ( (line.equals("positive")) || (line.equals("negative")) || (line.equals("neutral")) ) {
					outFile.println("',?");
				}
			}
			line = reader.readLine();
		}
		reader.close();
		outFile.close();
	}

	public static void main(String[] args) throws Exception {
		File file = new File("appoggio/AltraProva.txt");
		createTraining(file);
		createTesting(file);
	}

}
