package com.sandeep.prediction.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sandeep.predictionprovider.PredictionProvider;

public class TermDocPredictionAlgorithm  implements PredictionProvider {
	private String predictedTerms = "";
	private int predictionSize = 0;
	private int[][] docTermArray;
	private Map<String, Integer> uniqueTerms;
	
	@Override
	public void prediction(List<Integer> indexesList, List<String> sentenceDetection, String searchTerm) {
		String sTerm = searchTerm.toLowerCase();
		uniqueTerms = new LinkedHashMap<String, Integer>();
    	int docCount = 1, termIndex = 0;
    	for(Integer index : indexesList) {
    		String cDoc = sentenceDetection.get(index);
			String sent = cDoc.toLowerCase();
			String[] tokensList =  sent.split("\\s+");
			for(int i = 0; i < tokensList.length-1; i++) {
				String term = tokensList[i].toLowerCase()+"~~"+tokensList[i+1];
				if(!uniqueTerms.containsKey(term)) {
					uniqueTerms.put(term, termIndex);
					termIndex++;
				} 
			}
			docCount++;		
		}
		
		docTermArray = new int[docCount][termIndex]; 
		
		int row = 0;
		List<Integer> colList = new ArrayList<Integer>();
		colList.add(uniqueTerms.get(sTerm));
		List<Integer> rowList = new ArrayList<Integer>();
		for(Integer index : indexesList) {
    		String cDoc = sentenceDetection.get(index);
			String sent = cDoc.toLowerCase();
			String[] tokensList =  sent.split("\\s+");
			for(int i = 0; i < tokensList.length-1; i++) {
				String term = tokensList[i].toLowerCase()+"~~"+tokensList[i+1];
				if(uniqueTerms.containsKey(term)) {
					docTermArray[row][uniqueTerms.get(term)] = 1;
				} 
			}
			rowList.add(row);
			row++;
		}
		
		System.out.println("total doc : "+docCount+" termCount : "+termIndex+" doc : "+docTermArray);
		String[] str = sTerm.split("~~");
		docTermProcessing(str[1], rowList);
		
		System.out.println("Predicted tokens : "+predictedTerms);
	}
	
	private void docTermProcessing(String term, List<Integer> rowList) {
		predictionSize++;
		predictedTerms = predictedTerms +" "+ term;
		if(predictionSize == 4)
			return;
		
		Map<Integer, String> newColMap = new HashMap<Integer, String>();
		
		for(Entry<String, Integer> e : uniqueTerms.entrySet()) {
			if(e.getKey().startsWith(term+"~~")) {
				newColMap.put(e.getValue(), e.getKey());
			}
		}
		
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		for(Integer key : newColMap.keySet()) {
			List<Integer> newRowList = new ArrayList<Integer>();
			for(Integer row : rowList) {
				if(docTermArray[row][key] == 1) {
					newRowList.add(row);
				}
			}
			map.put(key, newRowList);
		}
		
		int size = 0;
		List<Integer> fRowList = null;
		for(Entry<Integer, List<Integer>> e : map.entrySet()) {
			if(e.getValue().size() > size) {
				fRowList = e.getValue();
				term = newColMap.get(e.getKey()).split("~~")[1];
			}
		}
		
		docTermProcessing(term, fRowList);	
	}
	
}
