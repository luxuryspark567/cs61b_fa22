package ngordnet.main;

import org.junit.Test;

public class TestSearchHyponyms {

    searchHyponyms sh;

    public TestSearchHyponyms() {
        String synsetFile = "./data/wordnet/synsets16.txt";
        String hyponymFile = "./data/wordnet/hyponyms16.txt";
        //String synsetFile = "./data/wordnet/synsets1000-subgraph.txt";
        //String hyponymFile = "./data/wordnet/hyponyms1000-subgraph.txt";

        //String synsetFile = "./data/wordnet/synsets.txt";
        //String hyponymFile = "./data/wordnet/hyponyms.txt";

        sh = new searchHyponyms(synsetFile, hyponymFile);
    }

    @Test
    public void TestSearch() {

        System.out.println(HyponymsHandler.myPqSort(sh.getHyponyms("increase")));
        System.out.println(HyponymsHandler.myPqSort(sh.getHyponyms("change")));
        System.out.println(HyponymsHandler.myPqSort(sh.getHyponyms("centrum")));
        System.out.println(HyponymsHandler.myPqSort(sh.getHyponyms("cyano_group")));
    }
}
