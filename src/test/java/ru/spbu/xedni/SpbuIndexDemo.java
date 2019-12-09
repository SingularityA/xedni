package ru.spbu.xedni;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static ru.spbu.xedni.Helpers.utf8;

public class SpbuIndexDemo {
    private final String indexPath = System.getProperty("user.dir") + "/src/main/resources/index";
    private final Directory directory;
    private final Analyzer analyzer;
    private final StoredIndex index;

    private final List<String> queries = Arrays.asList(
            "декан Петросян",
            "спбгу 290",
            "спортивный клуб",
            "балтийские орланы",
            "переезд в гатчину",
            "средний балл егэ",
            "скандалы и расследования",
            "пунк мероприятия",
            "учеба по обмену",
            "иностранные студенты"
    );

    public SpbuIndexDemo() throws IOException {
        directory = FSDirectory.open(Paths.get(indexPath));
        analyzer = new RussianAnalyzer();
        index = new StoredIndex(directory, analyzer);
    }

    @Test
    public void demo() {
        for (String query : queries) {
            System.out.println("\nFor query: " + utf8(query));
            showResults(index.search(query));
        }
    }

    private void showResults(List<Document> documents) {
        if (documents.isEmpty()) {
            System.out.println("Nothing found :(");
            return;
        }
        String url;
        for (Document doc : documents) {
            url = "https://spbu.ru/" + doc.get(Fields.URL.getName());
            System.out.println(url);
        }
    }
}
