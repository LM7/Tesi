package weka;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSVtoARFF {

	public static void main(String[] args) throws IOException {
		    // load CSV

		    CSVLoader loader = new CSVLoader();
		    loader.setSource(new File("WekaMarino/training_Moncler.csv"));
		    Instances data = loader.getDataSet();
		    
		    
		 
		    // save ARFF
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(data);
		    saver.setFile(new File("WekaMarino/"));
		    saver.setDestination(new File("training.arff"));
		    saver.writeBatch();

	}

}
