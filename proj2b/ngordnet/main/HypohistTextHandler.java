package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HypohistTextHandler extends HyponymsHandler {
    private searchHyponyms sh;
    private NGramMap ngm;

    public HypohistTextHandler(searchHyponyms sh, NGramMap ngm) {
        super(sh, ngm);
        this.sh = sh;
        this.ngm = ngm;
    }
    @Override
    public String handle(NgordnetQuery q) {
        //return "Hello i am hypohist text!";

        ArrayList<String> rhp = getRawHyponymForMultipleWords(q); // get raw hyponyms
        ArrayList<String> rhpK = getMostPopularKWords(q, rhp);// get the most popular k words
        //System.out.println("before sort: " + rhpK.toString());
        rhpK.sort(null);
        //reverse alphabetically: rhpK.sort( (a, b) -> { return -1 * a.compareTo(b); } );
        //System.out.println("after sort: " + rhpK.toString());
        //return rhpK.toString(); // to String and return
        // move filtered result to words array.
        List<String> words = new ArrayList<>();
        while(!rhpK.isEmpty()) {
            words.add(rhpK.getFirst());
            rhpK.removeFirst();
        }

        // Debug code
        //System.out.println(words.toString());

        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder response = new StringBuilder();
        for (String word:words) {
            response.append(word);
            response.append(ngm.countHistory(word, startYear, endYear).toString());
            response.append("\n");
        }
        return response.toString();
    }
}
