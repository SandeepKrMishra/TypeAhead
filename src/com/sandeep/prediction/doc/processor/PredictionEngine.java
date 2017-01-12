package com.sandeep.prediction.doc.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.RAMDirectory;

import com.sandeep.prediction.core.Synonyms;
import com.sandeep.prediction.text.preprocessor.SentenceDetection;
import com.sandeep.predictionprovider.PredictionProvider;
import com.sandeep.predictionprovider.PredictionProviderFactory;



public class PredictionEngine {
	
	private static final String RELATIVE_FILE_PATH = "/resources/models/as.txt";
	private static String FILE_PATH = "";
	
	public static void index() {
        try {
        	
        	String relativePath = new File("").getAbsolutePath();
			FILE_PATH = relativePath + RELATIVE_FILE_PATH;
			
        	RAMDirectory directory = new RAMDirectory();
        	StringBuilder strFile = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
			char[] buffer = new char[512];
			int num = 0;
			while ((num = reader.read(buffer)) != -1) {
				String current = String.valueOf(buffer, 0, num);
				strFile.append(current);
				buffer = new char[512];
			}
			reader.close();
			String sents = strFile.toString();
			
			List<String> sentenceDetection = SentenceDetection.sentenceDetection(sents);
            IndexWriter indexWriter = new IndexWriter(directory, new StandardAnalyzer(), true);

            int index = 0;
            for(String sent : sentenceDetection) {
            	indexWriter.addDocument(CreateDoc.createDocument("doc~~"+index++, sent.toLowerCase()));
			}
            indexWriter.optimize();
            indexWriter.close();

            Searcher searcher = new IndexSearcher(directory);
            Scanner input = new Scanner(System.in);
            String searchTerm = "", biGramSearchTerm = "";
			while (true) {
				System.out.println("Please enter your search terms (or type :q to quit):");
				searchTerm = input.nextLine();
				if (searchTerm.equalsIgnoreCase(":q"))
					break;
				String[] terms = searchTerm.split("[\\s]");
				if (terms.length < 2) {
					System.out.println("Please enter your atleast two terms.");
					continue;
				} else {
					searchTerm = terms[terms.length - 2]+" "+terms[terms.length - 1];
					biGramSearchTerm = terms[terms.length - 2]+"~~"+terms[terms.length - 1];
				}

				List<Integer> indexes = SearchIndex.search(searcher, searchTerm.toLowerCase());
				
				System.out.println("****************************Synonyms***************************************");
				Synonyms.findSynonyms(biGramSearchTerm);
				
				/*
				 * Its a factory of diff-diff alCdocgorithm.
				*/
				System.out.println("****************************Prediction***************************************");
				PredictionProvider  predictionProvider = PredictionProviderFactory.getPrediction();
				predictionProvider.prediction(indexes, sentenceDetection,
						terms[terms.length - 2] + "~~" + terms[terms.length - 1]);
			}
            searcher.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
