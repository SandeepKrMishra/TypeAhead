package com.sandeep.prediction.doc.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

public class SearchIndex {
	public static List<Integer>  search(Searcher searcher, String queryString) 
	        throws ParseException, IOException {
			// List to store hit doc's indexes. 
	    	List<Integer> indexesList = new ArrayList<Integer>();
	    	QueryParser queryParser = new QueryParser("content", new StandardAnalyzer());
	    	Query query = queryParser.parse(queryString);
	        Hits hits = searcher.search(query);
	        int hitCount = hits.length();
	        if (hitCount == 0) {
	            System.out.println( "No matches were found for \"" + queryString + "\"");
	        }
	        else {
	            //System.out.println("Hits for \"" + queryString + "\" were found in quotes by:");
	            for (int i = 0; i < hitCount; i++) {
	                Document doc = hits.doc(i);
	                // creating list of indexes of hit doc's.
	                indexesList.add(Integer.parseInt(doc.get("title").split("~~")[1]));
	                //System.out.println("  " + (i + 1) + ". " + doc.get("title"));
	            }
	        }
	        System.out.println();
	        return indexesList;
	    }
}
