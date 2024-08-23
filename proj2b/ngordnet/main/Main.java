package ngordnet.main;

import edu.princeton.cs.algs4.In;
import ngordnet.hugbrowsermagic.HugNgordnetServer;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        HugNgordnetServer hns = new HugNgordnetServer();
        String wordFile = "./data/ngrams/top_49887_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";

        searchHyponyms sh = new searchHyponyms(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymsHandler(sh, ngm));
        hns.register("hypohist", new HypohistHandler(sh, ngm));
        hns.register("hypohisttext", new HypohistTextHandler(sh, ngm));
    }
}
