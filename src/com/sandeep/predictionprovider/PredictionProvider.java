package com.sandeep.predictionprovider;

import java.util.List;

public interface PredictionProvider {
	public void prediction(List<Integer> indexesList, List<String> sentenceDetection, String sTerm);
}
