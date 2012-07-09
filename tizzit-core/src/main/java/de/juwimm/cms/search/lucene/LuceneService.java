package de.juwimm.cms.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.search.spans.SpanScorer;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import de.juwimm.cms.beans.foreign.TizzitPropertiesBeanSpring;

public class LuceneService {
	
	String indexLocation="D:/searchTest"; 
	Directory index=null;
	IndexWriter writer=null;
	IndexReader reader=null;
	IndexSearcher searcher=null;
	private TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring;
	SpellChecker spellChecker=null;
	
	private LuceneService(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring){
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
		String luceneStore = tizzitPropertiesBeanSpring.getSearch().getLuceneStore();
		if ("luceneFile".equalsIgnoreCase(luceneStore)) {
			indexLocation = tizzitPropertiesBeanSpring.getDatadir();
			indexLocation += "/" + "searchTest";
//			if (!filePath.startsWith("file://")) {
//				filePath = "file://" + filePath;
//			}
		}
		initializeIndex();
	}
	
	public void addToIndex(Document document){
		try {
			writer.addDocument(document);
			writer.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeDocument(Term term){
		try {
			writer.deleteDocuments(term);
			writer.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Directory getDirectory() {
		if(index==null){
			initializeIndex();
		}
		return index;
	}
	
	private void initializeIndex(){
		StandardAnalyzer analyzer=new StandardAnalyzer(Version.LUCENE_36);
		try {
			index=FSDirectory.open(new File(indexLocation));
			IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			writer = new IndexWriter(index,indexWriterConfig);
			reader = IndexReader.open(index);
			searcher = new IndexSearcher(reader);
			spellChecker= new SpellChecker(index);
			Dictionary dictionary= new LuceneDictionary(reader, "contents");
			IndexWriterConfig indexWriterSpellcheck=new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			spellChecker.indexDictionary(dictionary,indexWriterSpellcheck, true);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TopScoreDocCollector search(Query query){
		return search(query, 20);
	}
	
	public TopScoreDocCollector search(Query query, int hitsPerPage){
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		try {
			searcher.search(query, collector);
		} catch (IOException e) {
			return null;
		}
		return collector;
	}
	
	public Document getDocument(int docId){
	    try {
			return searcher.doc(docId);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
	
	public SpellChecker getSpellChecker(){
		return spellChecker;
	}

public IndexReader getIndexReader() {
	// TODO Auto-generated method stub
	return reader;
}

}
