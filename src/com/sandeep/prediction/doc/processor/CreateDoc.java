package com.sandeep.prediction.doc.processor;

import java.io.StringReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class CreateDoc {
    public static Document createDocument(String title, String content) {
        Document doc = new Document();
        // un-indexed title
        doc.add(new Field("title", title, Field.Store.YES, Field.Index.NO));

        /* ...and the content as an indexed field. Note that indexed
        * Text fields are constructed using a Reader. Lucene can read
        * and index very large chunks of text, without storing the
        * entire content verbatim in the index. In this example we
        * can just wrap the content string in a StringReader.
        */
        
        doc.add(new Field("content", new StringReader(content)));
        return doc;
    }
}
