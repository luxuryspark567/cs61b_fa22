package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HypohistHandler extends HyponymsHandler {
    private searchHyponyms sh;
    private NGramMap ngm;

    public HypohistHandler(searchHyponyms sh, NGramMap ngm) {
        super(sh, ngm);
        this.sh = sh;
        this.ngm = ngm;
    }
    // Getting a list of hyponyms is cool, but what can sometimes be even cooler is
    // plotting their relative frequencies.
    // For example, if the user enters the words "food, cake", sets startYear=1900,
    // endYear=2020 and k=8, and clicks the “Hypohist” button, they’d be able to see
    // the relative frequency of the 8 most popular words which were hyponyms of food
    // and cake over the time period between 1900 and 2020.

    // Note: Behavior is pretty straightforward if k > 0 for Hypohist. If k = 0,
    // it’s not clear what should happen. Maybe come up with a cool idea.
    @Override
    public String handle(NgordnetQuery q) {
        //return "Hello i am hypohist!";

        ArrayList<String> rhp = getRawHyponymForMultipleWords(q); // get raw hyponyms
        ArrayList<String> rhpK = getMostPopularKWords(q, rhp);// get the most popular k words
        PriorityQueue<String> pq = myPqSort(rhpK);//Sort alpha-beta

        // move filtered result to words array.
        List<String> words = new ArrayList<>();
        while(!pq.isEmpty()) {
            words.add(pq.poll());
        }

        // Debug code
        System.out.println(words.toString());

        int startYear = q.startYear();
        int endYear = q.endYear();

        ArrayList<TimeSeries> lts = new ArrayList<>();
        for (String word : words) {
            lts.add(ngm.weightHistory(word, startYear, endYear));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, lts);

        return Plotter.encodeChartAsString(chart);
    }
}
