package ru.spbu.xedni;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ru.spbu.xedni.Helpers.utf8;

public class StoredIndex {

    private final Directory indexDirectory;
    private final Analyzer analyzer;

    public StoredIndex(Directory indexDirectory, Analyzer analyzer) {
        this.indexDirectory = indexDirectory;
        this.analyzer = analyzer;
    }

    public void addAll(String path) {
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> {
                String[] parts = line.split(",", 2);
                add(parts[0], parts[1]);
                System.out.println("Added doc: " + parts[0]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String name, String contents) {
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        final IndexWriter indexWriter;
        final Document document = new Document();

        try {
            indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);

            document.add(new StringField(Fields.URL.getName(), name, Field.Store.YES));
            document.add(new TextField(Fields.TEXT.getName(), contents, Field.Store.NO));

            indexWriter.addDocument(document);
            indexWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Search by document url (name)
     *
     * @return document or null if not found
     */
    public Document searchByUrl(String url) {
        final Query query = new TermQuery(new Term(Fields.URL.getName(), url));
        final List<Document> documents = search(query, 1);
        if (documents.isEmpty())
            return null;
        else
            return documents.get(0);
    }

    /**
     * Standard full-text search over document contents
     *
     * @return top relevant documents
     */
    public List<Document> search(String queryString, Integer top) {
        final List<Document> documents = new ArrayList<>();
        try {
            final Query query = new QueryParser(Fields.TEXT.getName(), analyzer).parse(utf8(queryString));
            documents.addAll(search(query, top));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public List<Document> search(String queryString) {
        return search(queryString, 10);
    }

    private List<Document> search(Query query, Integer top) {
        final List<Document> documents = new ArrayList<>();
        try {
            final IndexReader indexReader = DirectoryReader.open(indexDirectory);
            final IndexSearcher searcher = new IndexSearcher(indexReader);
            final TopDocs topDocs = searcher.search(query, top);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public void clear() {
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        final IndexWriter indexWriter;
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try {
            indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
