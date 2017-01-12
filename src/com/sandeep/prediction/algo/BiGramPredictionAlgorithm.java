package com.sandeep.prediction.algo;

import java.util.HashMap;
import java.util.List;

import com.sandeep.predictionprovider.PredictionProvider;

public class BiGramPredictionAlgorithm implements PredictionProvider {
	
	@Override
	public void prediction(List<Integer> indexesList,List<String> sentenceDetection, String searchTerm) {
	    	
			String sTerm = searchTerm.toLowerCase();
	    	HashMap<String, HashMap<String, Double>> bigramMap = new HashMap<String, HashMap<String, Double>>();
		    /* 
		     * Split sentences(doc) into terms 
		     */
	    	for(Integer index : indexesList) {
	    		String cDoc = sentenceDetection.get(index);
				String doc = cDoc.toLowerCase();
				String[] terms =  doc.split("\\s+");
				
				if(terms.length > 3) {
					 for(int i = 0; i < terms.length-3; i++) {
				            if(bigramMap.containsKey(terms[i]+"~~"+terms[i+1])) {
				                if(bigramMap.get(terms[i]+"~~"+terms[i+1]).containsKey(terms[i+2]+"~~"+terms[i+3])) {
				                    double frequency = bigramMap.get(terms[i]+"~~"+terms[i+1]).get(terms[i+2]+"~~"+terms[i+3]);
				                    frequency++;
				                    bigramMap.get(terms[i]+"~~"+terms[i+1]).put(terms[i+2]+"~~"+terms[i+3], frequency);
				                } else {
				                	bigramMap.get(terms[i]+"~~"+terms[i+1]).put(terms[i+2]+"~~"+terms[i+3], 1.0);
				                }
				            } else {
				            	bigramMap.put(terms[i]+"~~"+terms[i+1], createResult(terms[i+2]+"~~"+terms[i+3]));
				            }
				      }
				}	
			}
		
	        double sum = 0;
	        try {
	            for(String s : bigramMap.get(sTerm).keySet()) {
	                sum += bigramMap.get(sTerm).get(s);
	            }
	            for(String s : bigramMap.get(sTerm).keySet()) {
	            	bigramMap.get(sTerm).put(s, bigramMap.get(sTerm).get(s)/sum);
	            }
	        } catch (Exception NullPointerException) {
	            System.out.println("Search query not found in any document.");
	        }
	        try {
	        	String predictedTerms = predectedTerms(bigramMap.get(sTerm));
	        	double predictedProbability = predValue(bigramMap.get(sTerm));
	            System.out.println("Predicted terms are: \"" + predictedTerms + "\" \nWith probability of: " + predictedProbability + "("+ Math.round(predictedProbability*100) +"%)");
	        } catch (Exception e) {
	            System.out.println("Prediction failure.");
	        }
		}
		
		final HashMap<String, Double> createResult(String s) {
		    HashMap<String, Double> valueFrequencyMap = new HashMap<String, Double>();
		    valueFrequencyMap.put(s, 1.0);
		    return valueFrequencyMap;
		}
		
		final String predectedTerms(HashMap<String, Double> hashMap) {
		    String key = "";
		    double max = 0;
		    for(String s : hashMap.keySet()) {
		        if(hashMap.get(s) > max) {
		            max = hashMap.get(s);
		            key = s;
		        }
		    }
		    String[] predictedTokens = key.split("~~");
		    return predictedTokens[0]+" "+predictedTokens[1];
		}   
		
		final double predValue(HashMap<String, Double> hashMap) {
		    double max = 0;
		    for(String s : hashMap.keySet()) {
		        if(hashMap.get(s) > max) {
		            max = hashMap.get(s);
		        }
		    }
		    return max;
		}
}
