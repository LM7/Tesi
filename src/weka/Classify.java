package weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.SMO;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class Classify {
	
	public static void main(String[] args) throws Exception {
		BufferedReader breader = null;
		breader = new BufferedReader(new FileReader("WekaMarino/trainingMarino.arff"));
		Instances train = new Instances(breader);
		train.setClassIndex(train.numAttributes() -1);
		
		breader = new BufferedReader(new FileReader("WekaMarino/testingMarino.arff"));
		Instances test = new Instances(breader);
		test.setClassIndex(train.numAttributes() -1);
		
		breader.close();
		
		//J48 tree = new J48();
		//NaiveBayesMultinomial tree = new NaiveBayesMultinomial();
		//NaiveBayes tree = new NaiveBayes();
		//NaiveBayesSimple tree = new NaiveBayesSimple();
		NaiveBayesUpdateable tree = new NaiveBayesUpdateable();
		//SMO tree = new SMO();
		
		/*provo a trasformare le stringhe*/
		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(train);
		Instances dataFiltered = Filter.useFilter(train, filter);
		
		
		tree.buildClassifier(dataFiltered); //train-dataFiltered
		Instances labeled = new Instances(test);
		
		int pos = 0;
		int neg = 0;
		int neu = 0;
		for (int i = 0; i < test.numInstances(); i++) {
			double clsLabel = tree.classifyInstance(test.instance(i));
			labeled.instance(i).setClassValue(clsLabel);
			if (labeled.instance(i).toString().contains("positive")) {
				pos++;
			}
			if (labeled.instance(i).toString().contains("negative")) {
				neg++;
			}
			if (labeled.instance(i).toString().contains("neutral")) {
				neu++;
			}
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("labeled.arff"));
		writer.write(labeled.toString());
		writer.close();
		
		System.out.println("POSITIVE: "+pos);
		System.out.println("NEGATIVE: "+neg);
		System.out.println("NEUTRAL: "+neu);
	}
}
