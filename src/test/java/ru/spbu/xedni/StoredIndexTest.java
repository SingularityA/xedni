package ru.spbu.xedni;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class StoredIndexTest {

    final String indexPath = System.getProperty("user.dir") + "/src/test/resources/index";
    final String docPath = System.getProperty("user.dir") + "/src/test/resources/test_texts.csv";
    private final Directory directory;
    private final Analyzer analyzer;
    private final StoredIndex index;

    public StoredIndexTest() throws IOException {
        directory = FSDirectory.open(Paths.get(indexPath));
        analyzer = new RussianAnalyzer();
        index = new StoredIndex(directory, analyzer);
    }

    @After
    public void clean() {
        index.clear();
    }

    @Test
    public void testAdd() {
        final String name = "doc/bla/1";
        final String contents = "some long bla bla";

        Document document = index.searchByUrl(name);
        Assert.assertNull(document);

        index.add(name, contents);
        document = index.searchByUrl(name);

        Assert.assertNotNull(document);
        Assert.assertEquals(name, document.get(Fields.URL.getName()));
    }

    @Test
    public void testAddAll() {
        final String name = "4";
        final String query = "фантазия";

        Document document = index.searchByUrl(name);
        Assert.assertNull(document);

        index.addAll(docPath);

        document = index.searchByUrl(name);
        Assert.assertNotNull(document);
        Assert.assertEquals("4", document.get(Fields.URL.getName()));

        document = index.search(query).get(0);
        Assert.assertEquals("4", document.get(Fields.URL.getName()));
    }

    @Test
    public void testSearch() {
        index.add("1", "My first document");
        index.add("2", "This is the second document");
        index.add("3", "And the third document");
        index.add("4", "This is just some dummy");
        index.add("5", "Cats cats bats");

        List<Document> documents = index.search("document");

        Assert.assertEquals(3, documents.size());

        documents = index.search("dummy");

        Assert.assertEquals(1, documents.size());
        Assert.assertEquals("4", documents.get(0).get(Fields.URL.getName()));
    }

    @Test
    public void testSearchByName() {
        index.add("1", "My first document");
        index.add("2", "This is the second document");
        index.add("3", "And the third document");
        index.add("4", "This is just some dummy");
        index.add("5", "Cats cats bats");

        Document document = index.searchByUrl("3");

        Assert.assertEquals("3", document.get(Fields.URL.getName()));
    }
}
