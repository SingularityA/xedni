package ru.spbu.xedni;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class SpbuIndexPopulator {

    public static void main(String[] args) throws IOException {
        final String indexPath = System.getProperty("user.dir") + "/src/main/resources/index";
        final String docPath = System.getProperty("user.dir") + "/src/main/resources/spbu_texts_clean.csv";

        final Directory directory = FSDirectory.open(Paths.get(indexPath));;
        final Analyzer analyzer = new RussianAnalyzer();
        final StoredIndex index = new StoredIndex(directory, analyzer);

        index.addAll(docPath);
    }
}
