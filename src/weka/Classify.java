package weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.trees.J48;
import weka.core.Instances;


public class Classify {
	
	public static void main(String[] args) throws Exception {
		BufferedReader breader = null;
		breader = new BufferedReader(new FileReader("training.arff"));
		Instances train = new Instances(breader);
		train.setClassIndex(train.numAttributes() -1);
		
		breader = new BufferedReader(new FileReader("test.arff"));
		Instances test = new Instances(breader);
		test.setClassIndex(train.numAttributes() -1);
		
		breader.close();
		
		NaiveBayesMultinomial tree = new NaiveBayesMultinomial();
		//J48 tree = new J48();
		tree.buildClassifier(train);
		Instances labeled = new Instances(test);
		
		for (int i = 0; i < test.numInstances(); i++) {
			double clsLabel = tree.classifyInstance(test.instance(i));
			labeled.instance(i).setClassValue(clsLabel);
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("labeled.arff"));
		writer.write(labeled.toString());
		writer.close();
	}
}
