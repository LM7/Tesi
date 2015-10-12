package query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SaveOnDB {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("Prova.txt"));
		String line = reader.readLine();
		int cont = 0;
		while (line != null) {
			if (line.startsWith("USER:")) {
				cont++;
				System.out.println(cont);
			}
			line = reader.readLine();
		}
		reader.close();
	}

}
