package newrelic.service;

import newrelic.model.PhraseCountsResult;
import newrelic.util.InputReader;
import newrelic.util.PhraseCounter;

import java.util.List;
import java.util.stream.Collectors;

public class PhraseCounterService {
    private final InputReader reader;
    private final PhraseCounter counter;

    PhraseCounterService(final InputReader reader, final PhraseCounter counter) {
        this.reader = reader;
        this.counter = counter;
    }

    public static PhraseCounterService create() {
        return new PhraseCounterService(new InputReader(), new PhraseCounter(3));
    }

    public List<PhraseCountsResult> countPhrases(final String[] args) {
        var paths = reader.toPaths(args);
        if (paths.isEmpty()) {
            System.out.println("No args provided, checking StdIn");
            return List.of(counter.countPhrasesFromStdIn());
        } else {
            return paths.stream()
                .map(counter::countPhrasesFrom)
                .collect(Collectors.toList());
        }
    }
}
