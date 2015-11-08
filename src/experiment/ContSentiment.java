package experiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ContSentiment {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("WekaMarino/training.txt"));
		String line = reader.readLine();
		int neg = 0;
		int pos = 0;
		int neu = 0;
		int negV = 0;
		int posV = 0;
		int neuV = 0;
		while (line != null) {
			if (line.equals("negative")) {
				neg++;
			}
			if (line.equals("positive")) {
				pos++;
			}
			if (line.equals("neutral")) {
				neu++;
			}
			if (line.equals("negativeVero")) {
				negV++;
			}
			if (line.equals("positiveVero")) {
				posV++;
			}
			if (line.equals("neutralVero")) {
				neuV++;
			}
			line = reader.readLine();
		}
		System.out.println("NEGATIVE: "+neg);
		System.out.println("POSITIVE: "+pos);
		System.out.println("NEUTRAL: "+neu);
		System.out.println();
		System.out.println("NEGATIVEVERO: "+negV);
		System.out.println("POSITIVEVERO: "+posV);
		System.out.println("NEUTRALVERO: "+neuV);
	}

}
