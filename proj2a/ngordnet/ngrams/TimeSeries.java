package ngordnet.ngrams;

import java.util.*;

/** An object for mapping a year number (e.g. 1996) to numerical data. Provides
 *  utility methods useful for data analysis.
 *  @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     *  inclusive of both end points. */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        if (ts != null)
        {
            // check all keys in tree. no iterator, sorry.
            for (int year: ts.keySet()){
                if (year >= startYear && year <= endYear) {
                    this.put(year, ts.get(year));
                }
            }
        }
    }

    /** Returns all years for this TimeSeries (in any order). */
    public List<Integer> years() {
        List<Integer> l = new ArrayList<>();
        for (int ele: this.keySet()) {
            l.add(ele);
        }
        return l;
    }

    /** Returns all data for this TimeSeries (in any order).
     *  Must be in the same order as years(). */
    public List<Double> data() {
        List<Double> l = new ArrayList<>();
        for (int ele: this.keySet()) {
            l.add(this.get(ele));
        }
        return l;
    }

    /** Returns the yearwise sum of this TimeSeries with the given TS. In other words, for
     *  each year, sum the data from this TimeSeries with the data from TS. Should return a
     *  new TimeSeries (does not modify this TimeSeries). */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = new TimeSeries();
        List<Integer> l1 = this.years();
        List<Integer> l2 = ts.years();
        for (int ele: l1) {
            if (l2.contains(ele)) {
                result.put(ele, this.get(ele) + ts.get(ele));
                for (int index = 0; index < l2.size(); index ++)
                {
                    if (l2.get(index) == ele) {
                        l2.remove(index);
                        break;
                    }
                }

            }
            else {
                result.put(ele, this.get(ele));
            }
        }
        for (int ele: l2) {
            result.put(ele, ts.get(ele));
        }
        return result;
    }

     /** Returns the quotient of the value for each year this TimeSeries divided by the
      *  value for the same year in TS. If TS is missing a year that exists in this TimeSeries,
      *  throw an IllegalArgumentException. If TS has a year that is not in this TimeSeries, ignore it.
      *  Should return a new TimeSeries (does not modify this TimeSeries). */
     public TimeSeries dividedBy(TimeSeries ts) {
         TimeSeries result = new TimeSeries();
         List<Integer> l1 = this.years();
         List<Integer> l2 = ts.years();
         for (int ele: l1) {
             if (l2.contains(ele)) {
                 result.put(ele, this.get(ele) / ts.get(ele));
             }
             else { // TS is missing a year that exists in this TimeSeries
                 throw new IllegalArgumentException();
             }
         }
         return result;
    }
}
