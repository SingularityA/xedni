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
import static org.hamcrest.Matchers.hasItems;
import static ru.spbu.xedni.Helpers.utf8;

public class LuceneAnalyzerTest {
    private static final String TEXT =
            "100 2.2 Товарищи! дальнейшее развитие различных форм деятельности влечет за собой процесс внедрения и " +
                    "модернизации системы обучения кадров, соответствует насущным потребностям. Задача организации, " +
                    "в особенности же сложившаяся структура организации в значительной степени обуславливает " +
                    "создание соответствующий условий активизации. Не следует, однако забывать, что консультация " +
                    "с широким активом способствует подготовки и реализации форм развития. Не следует, однако " +
                    "забывать, что реализация намеченных плановых заданий требуют от нас анализа позиций, " +
                    "занимаемых участниками в отношении поставленных задач. Повседневная практика показывает, " +
                    "что рамки и место обучения кадров требуют от нас анализа существенных финансовых и " +
                    "административных условий.";
    private static final String FIELD_NAME = "content";

    @Test
    public void testRussianAnalyzer() throws IOException {
        List<String> result = analyze(TEXT, new RussianAnalyzer());

        System.out.println(result);
        assertThat(result, hasItems(
                utf8("дальн"),
                utf8("деятельн"),
                utf8("соответств")
        ));
    }

    @Test
    public void testCustomRussianAnalyzer() throws IOException {
        List<String> result = analyze(TEXT, new CustomRussianAnalyzer());

        System.out.println(result);
        assertThat(result, hasItems(
                utf8("дальнейше"),
                utf8("деятельност"),
                utf8("соответствует")
        ));
    }

    private List<String> analyze(String text, Analyzer analyzer) throws IOException {
        final List<String> result = new ArrayList<>();
        final TokenStream tokenStream = analyzer.tokenStream(FIELD_NAME, utf8(text));
        final CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            result.add(attr.toString());
        }

        return result;
    }
}
