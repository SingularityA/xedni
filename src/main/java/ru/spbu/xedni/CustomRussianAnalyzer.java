package ru.spbu.xedni;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.ru.RussianLightStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class CustomRussianAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer src = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(src);
        result = new StopFilter(result, RussianAnalyzer.getDefaultStopSet());
        result = new RussianLightStemFilter(result);
        return new TokenStreamComponents(src, result);
    }
}
