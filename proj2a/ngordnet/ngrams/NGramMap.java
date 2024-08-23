package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.*;

/** An object that provides utility methods for making queries on the
 *  Google NGrams dataset (or a subset thereof).
 *
 *  An NGramMap stores pertinent data from a "words file" and a "counts
 *  file". It is not a map in the strict sense, but it does provide additional
 *  functionality.
 *
 *  @author Josh Hug
 */
public class NGramMap {

    private TimeSeries ts;
    private HashMap<String, TimeSeries> hm = new HashMap<>();
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {


        //1, Get words
        In in = new In(wordsFilename);
        TimeSeries tsTmp = new TimeSeries();
        // Create a TimeSeries for a specific word;
        String lastWord = null;
        String word = null;
        int year = 0;
        double times = 0;
        int pages = 0;

        try {
            while(true) {
                //get from file
                word = in.readString();
                year = in.readInt();
                times = (double)in.readInt();
                pages = in.readInt();

                if (!word.equals(lastWord)) {

                    if (lastWord != null) {
                        //save to hashmap
                        hm.put(lastWord, tsTmp);
                    }
                    //Create a new ts
                    tsTmp = new TimeSeries(); // clean ts
                }
                //save data to ts
                tsTmp.put(year, (double)times);
                lastWord = word;
            }

        }
        catch(Exception e) {
            System.out.println("End of file" + wordsFilename);
            hm.put(lastWord, tsTmp);
        }

        //2, Get counts
        ts = new TimeSeries();
        in = new In(countsFilename);

        // Create a TimeSeries for a specific word;
        String line;

        try {
            while(true) {
                //get from file
                line = in.readString();
                String[] split = line.split(",");
                long[] ids = Arrays.asList(split).stream().mapToLong(Long::parseLong).toArray();

                year = (int)ids[0];
                times = (double)ids[1];

                ts.put(year,(double)times);
            }
        }
        catch(Exception e) {
            System.out.println("End of file" + countsFilename);
        }
    }

    /** Provides the history of WORD. The returned TimeSeries should be a copy,
     *  not a link to this NGramMap's TimeSeries. In other words, changes made
     *  to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word) {
        return new TimeSeries(hm.get(word), 1400, 2100);
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     *  returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     *  changes made to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(hm.get(word), startYear, endYear);
    }

    /** Returns a defensive copy of the total number of words recorded per year in all volumes. */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(ts, 1400, 2100);
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD compared to
     *  all words recorded in that year. */
    public TimeSeries weightHistory(String word) {
        TimeSeries result = new TimeSeries(hm.get(word), 1400, 2100);
        return result.dividedBy(ts);
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     *  and ENDYEAR, inclusive of both ends. */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries result = new TimeSeries(hm.get(word), startYear, endYear);
        return result.dividedBy(ts);
    }

    /** Returns the summed relative frequency per year of all words in WORDS. */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word:words) {
            result = result.plus(weightHistory(word));
        }
        return result;
    }

    /** Provides the summed relative frequency per year of all words in WORDS
     *  between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     *  this time frame, ignore it rather than throwing an exception. */
    public TimeSeries summedWeightHistory(Collection<String> words,
                              int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word:words) {
            result = result.plus(weightHistory(word, startYear, endYear));
        }
        return result;
    }

    public static void main(String[] args) {
        NGramMap ngm=new NGramMap("./data/ngrams/words_that_start_with_q.csv",
                "./data/ngrams/total_counts.csv");
    }

}
