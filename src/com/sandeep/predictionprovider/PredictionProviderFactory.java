package com.sandeep.predictionprovider;

import com.sandeep.prediction.algo.BiGramPredictionAlgorithm;
import com.sandeep.prediction.algo.TermDocPredictionAlgorithm;

public class PredictionProviderFactory {
	
	private static boolean flag = true;
	public static PredictionProvider getPrediction() {
		if(flag) {
			return new BiGramPredictionAlgorithm();
		} else {
			return new TermDocPredictionAlgorithm();
		}
	}
}
