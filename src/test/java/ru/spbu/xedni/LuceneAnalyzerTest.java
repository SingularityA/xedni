package ru.spbu.xedni;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class LuceneAnalyzerTest {
    private static final String TEXT = "Почему с кодировкой такая беда??";
    private static final String FIELD_NAME = "content";

    @Test
    public void testRussianAnalyzer() throws IOException {
        List<String> result = analyze(TEXT, new RussianAnalyzer());

        System.out.println(result);
        // assertThat(result, contains());
    }

    @Test
    public void testCustomRussianAnalyzer() throws IOException {
        List<String> result = analyze(TEXT, new CustomRussianAnalyzer());

        System.out.println(result);
        // assertThat(result, contains());
    }

    public List<String> analyze(String text, Analyzer analyzer) throws IOException {
        final List<String> result = new ArrayList<>();
        final TokenStream tokenStream = analyzer.tokenStream(FIELD_NAME, text);
        CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            result.add(attr.toString());
        }
        return result;
    }
}
