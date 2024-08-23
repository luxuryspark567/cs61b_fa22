package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.ngrams.NGramMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestHyponymsHandler {
    HyponymsHandler hh;

    public TestHyponymsHandler() {
        String wordFile = "./data/ngrams/top_49887_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";

        searchHyponyms sh = new searchHyponyms(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);

        hh = new HyponymsHandler(sh, ngm);
    }
    @Test
    public void TestHandle() {

        ArrayList<String> al = new ArrayList<>();
        al.add("food");
        al.add("cake");
        int startYear = 1950;
        int endYear = 1990;
        int k = 5;

        NgordnetQuery q = new NgordnetQuery(al, startYear, endYear, k);
        System.out.println(hh.handle(q));

    }
}
