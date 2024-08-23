package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.PriorityQueue;

import static java.lang.Integer.parseInt;

/**
 * One which reads in the WordNet dataset and constructs an instance of the directed graph class.
 * This second class should also be able to take a word and return its hyponyms.
 * */
public class searchHyponyms {
    private myGraph mg;
    private myTrie mt;

    private ArrayList<String> ml;

    //private LinkedList<String> hypos;

    public searchHyponyms(String synsetsFilename, String hyponymsFilename) {
        // create hyponyms graph;
        mg = new myGraph(new In(hyponymsFilename));
        ml = new ArrayList<>();
        mt = new myTrie();
        // create 2 look up tables: index to word and word to index
        In in = new In(synsetsFilename);

        int hyper;
        while(in.hasNextLine()) {
            //get id and words from file
            String line = in.readLine();
            //String line = in.readString();
            String[] split = line.split(",");
            int id = parseInt(split[0]);
            String words = split[1];
            String[] wordsArray = split[1].split(" ");
            //create index to words
            ml.add(id, words);
            //create words to index
            for (String word: wordsArray) {
                mt.add(word, id);
            }
        }
    }

    // get the hyponyms of word.
    public ArrayList<String> getHyponyms(String word) {
        // get the list, which contains the indexes of the words that is word or contains word.
        LinkedList<Integer> l = mt.get(word);
        myDfsSearch mds;
        ArrayList<String> al = new ArrayList<>();
        //PriorityQueue<String> pq = new PriorityQueue<>();
        for (int indexKey: l) {
            //perform dfs search, and get all vertexes
            mds = new myDfsSearch(mg, indexKey);

            // translate index (vertexes) to words
            for (int indexValue : mds.vertexes()) {
                String[] wordKeys = ml.get(indexValue).split(" ");
                for (String wordKey: wordKeys) {
                    if (!al.contains(wordKey)) {
                        al.add(wordKey);
                    }
                    /*
                    if (!pq.contains(wordKey)) {
                        pq.add(wordKey);
                    }
                     */
                }
            }
        }
        return al;
/*
        StringBuilder result = new StringBuilder();
        result.append("[");
        while(pq.size() > 1) {
            result.append(pq.poll());
            result.append(", ");
        }
        result.append(pq.poll());
        result.append("]");
        return result.toString();
 */
    }
}
