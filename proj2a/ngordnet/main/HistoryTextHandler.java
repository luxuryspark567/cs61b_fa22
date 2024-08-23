package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    NGramMap ngm;
    public HistoryTextHandler(NGramMap map) {
        super();
        ngm = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
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
