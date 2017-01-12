package com.sandeep.prediction.core;

import java.io.File;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class Synonyms {
	private static final String RELATIVE_FILE_PATH = "/resources/dict/";
	private static String FILE_PATH = "";
	
	public static void findSynonyms(String terms) {
		String relativePath = new File("").getAbsolutePath();
		FILE_PATH = relativePath + RELATIVE_FILE_PATH;
	    System.setProperty("wordnet.database.dir", FILE_PATH);
		
		String[] wordFormList = terms.split("~~");
		for(String wordForm : wordFormList) {
			System.out.println("------------------------------------------------");
			WordNetDatabase database = WordNetDatabase.getFileInstance();
			Synset[] synsets = database.getSynsets(wordForm);
			
			if (synsets.length > 0) {
				System.out.println("The following synsets contain '" +
						wordForm + "' or a possible base form " +
						"of that text:");
				for (int i = 0; i < synsets.length; i++) {
					System.out.println("");
					String[] wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++){
						System.out.print((j > 0 ? ", " : "") + wordForms[j]);
					}
					//System.out.println(": " + synsets[i].getDefinition());
				}
			}
			else {
				System.err.println("No synsets exist that contain " +
						"the word form '" + wordForm + "'");
			}
			System.out.println();
		}
	}
}
