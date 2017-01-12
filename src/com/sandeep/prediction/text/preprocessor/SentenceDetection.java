package com.sandeep.prediction.text.preprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetection {
	
	private static final String TOKEN_MODEL_FILE = "/resources/models/en-token.bin";
	private static final String POS_TAG_PERCEPTRON_MODEL_FILE = "/resources/models/en-pos-perceptron.bin";
	private static final String SENT_MODEL_FILE = "/resources/models/en-sent.bin";
	private static String FILE_PATH = "";
	
	private static SentenceModel model;
	private static SentenceDetectorME sentencedetector;
	
	/*static {
		InputStream tokenModelIn = null;
		InputStream posModelIn = null;
		InputStream sentModelIn = null;

		try {
			sentModelIn = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(SENT_MODEL_FILE);
			SentenceModel model = new SentenceModel(sentModelIn);
			sentencedetector = new SentenceDetectorME(model);
		} catch (Exception e) {
			throw new IllegalStateException("Sentence Model Initialization failed", e);
		} finally {
			if (sentModelIn != null) {
				try {
					sentModelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}*/
	
	public static List<String> sentenceDetection(String content) throws IOException {
		
		String relativePath = new File("").getAbsolutePath();
		FILE_PATH = relativePath + SENT_MODEL_FILE;
		
	    InputStream modelIn = new FileInputStream(FILE_PATH);
	    List<String> sentList = new ArrayList<String>();
	    model = new SentenceModel(modelIn);
		sentencedetector = new SentenceDetectorME(model);
		String sentences[] = sentencedetector.sentDetect(content);
		for(String strSen : sentences){
		  sentList.add(strSen);
		}
		return sentList;
  }
}
