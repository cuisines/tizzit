package de.juwimm.cms.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
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
	private IndexingMode indexingMode=IndexingMode.BOTH;
	SpellChecker spellChecker=null;
	
	private LuceneService(TizzitPropertiesBeanSpring tizzitPropertiesBeanSpring){
		this.tizzitPropertiesBeanSpring = tizzitPropertiesBeanSpring;
		String luceneStore = tizzitPropertiesBeanSpring.getSearch().getLuceneStore();
		if ("luceneFile".equalsIgnoreCase(luceneStore)) {
			indexLocation = tizzitPropertiesBeanSpring.getDatadir();
			indexLocation += "/" + "searchTest";
		}
		String indexmode = tizzitPropertiesBeanSpring.getSearch().getIndexMode();
		if(indexmode.equalsIgnoreCase("live"))
			indexingMode=IndexingMode.LIVE;
		else if(indexmode.equalsIgnoreCase("work"))
			indexingMode=IndexingMode.WORK;
		else indexingMode=IndexingMode.BOTH;
		initializeIndex();
	}
	
	public void addToIndex(Document document){
		try {
			writer.addDocument(document);
			writer.commit();
		} catch (CorruptIndexException e) {
			throw new IndexingException("Error while adding document to index", e);
		} catch (LockObtainFailedException e) {
			throw new IndexingException("Error while adding document to index", e);
		} catch (IOException e) {
			throw new IndexingException("Error while adding document to index", e);
		}
	}
	
	public void removeDocument(Term term){
		try {
			writer.deleteDocuments(term);
			writer.commit();
		} catch (CorruptIndexException e) {
			throw new IndexingException("Error while removing document from index", e);
		} catch (IOException e) {
			throw new IndexingException("Error while removing document from index", e);
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
			reader = IndexReader.open(index);
			IndexWriterConfig indexWriterConfig=new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			writer = new IndexWriter(index,indexWriterConfig);
			searcher = new IndexSearcher(reader);
			spellChecker= new SpellChecker(index);
			Dictionary dictionary= new LuceneDictionary(reader, "contents");
			IndexWriterConfig indexWriterSpellcheck=new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			spellChecker.indexDictionary(dictionary,indexWriterSpellcheck, true);
		} catch (CorruptIndexException e) {
			throw new IndexingException("Error while initializing index", e);
		} catch (LockObtainFailedException e) {
			throw new IndexingException("Error while initializing index", e);
		} catch (IOException e) {
			throw new IndexingException("Error while initializing index", e);
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
			throw new IndexingException("Error while fetching document from index", e);
		} catch (IOException e) {
			throw new IndexingException("Error while fetching document from index", e);
		}
	}
	
	public SpellChecker getSpellChecker(){
		return spellChecker;
	}

public IndexReader getIndexReader() {
	return reader;
}

public IndexingMode getIndexingMode() {
	return indexingMode;
}

}
