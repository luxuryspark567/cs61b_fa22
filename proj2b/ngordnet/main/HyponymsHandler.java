package ngordnet.main;

import edu.princeton.cs.algs4.MaxPQ;
import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import org.junit.jupiter.api.Test;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler{

    private searchHyponyms sh;
    private NGramMap ngm;

    public HyponymsHandler(searchHyponyms sh, NGramMap ngm) {
        this.sh = sh;
        this.ngm = ngm;
    }

    private class mNode {
        int sum;
        String word;

        public mNode(int sum, String word) {
            this.sum = sum;
            this.word = word;
        }
    }

    static class mCompal implements Comparator<mNode> {
        @Override
        public int compare(mNode o1, mNode o2) {
            return o1.sum - o2.sum;
        }
    }

    private ArrayList<String> myIntersection(ArrayList<String> al1, ArrayList<String> al2) {
        ArrayList<String> al = new ArrayList<>();
        for (String s: al1) {
            if (al2.contains(s)) {
                al.add(s);
            }
        }
        return al;
    }
    public static PriorityQueue<String> myPqSort(ArrayList<String> al) {
        PriorityQueue<String> pq = new PriorityQueue<>();
        pq.addAll(al);
        return pq;
    }

    public String myToString(PriorityQueue<String> pq) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        while(pq.size() > 1) {
            result.append(pq.poll());
            result.append(", ");
        }
        result.append(pq.poll());
        result.append("]");
        return result.toString();
    }
/*
    “female, animal”
    [amazon, bird, cat, chick, dam, demoiselle, female, female_mammal, filly, hag, hen, nanny, nymph, siren]

    “female, leader”
    [crown_princess, marchioness, materfamilias, matriarch, mayoress, mistress, vicereine, viscountess].

    “bowl, gallery”:
    [amphitheater, amphitheatre]
 */
    ArrayList<String> getRawHyponymForMultipleWords(NgordnetQuery q) {
        // get hyponyms from each word
        ArrayList<String>[] aal = new ArrayList[q.words().size()];
        int looper = 0;
        for (String word: q.words()) {
            aal[looper] = sh.getHyponyms(word);
            looper++;
        }

        // calc intersection for all sets
        ArrayList<String> al = aal[0];
        for (int i = 1; i < aal.length; i++) {
            al = myIntersection(al, aal[i]);
        }

        return al;
    }

    // get the most popular k words
    ArrayList<String> getMostPopularKWords(NgordnetQuery q, ArrayList<String> al) {

        PriorityQueue<mNode> pq = new PriorityQueue<>(q.k(), new mCompal());

        // if k is 0, no need to check
        if (q.k() == 0) {
            return al;
        }
        else {
            // get k most popular words in a pq
            for (String word: al) {
                TimeSeries tm = ngm.countHistory(word, q.startYear(), q.endYear());
                int total_num = 0;
                for (double d: tm.data()) {
                    total_num += (int)d;
                }
                // add to k priority queue
                mNode m = new mNode(total_num, word);
                if (pq.size() >= q.k()) {
                    if (m.sum > pq.peek().sum){
                        pq.poll(); // Delete minimum
                        pq.add(m); // Add to PQ
                    }
                }
                else {
                    pq.add(m);
                }
            }
            ArrayList<String> r = new ArrayList<>();
            while (!pq.isEmpty()) {
                r.add(pq.poll().word);
            }
            return r;
        }
    }

    @Override
    public String handle(NgordnetQuery q) {
        //return "Hello!";
        ArrayList<String> rhp = getRawHyponymForMultipleWords(q); // get raw hyponyms
        ArrayList<String> rhpK = getMostPopularKWords(q, rhp);// get the most popular k words
        //System.out.println("before sort: " + rhpK.toString());
        rhpK.sort(null);
        //System.out.println("after sort: " + rhpK.toString());
        return rhpK.toString(); // to String and return
    }

    public static void main(String[] s) {

        String wordFile = "./data/ngrams/top_49887_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";

        searchHyponyms sh = new searchHyponyms(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);

        HyponymsHandler mhh = new HyponymsHandler(sh, ngm);

        ArrayList<String> al = new ArrayList<>();
        al.add("food");
        al.add("cake");
        int startYear = 1950;
        int endYear = 1990;
        int k = 5;

        NgordnetQuery q = new NgordnetQuery(al, startYear, endYear, k);
        System.out.println(mhh.handle(q));

    }
}
