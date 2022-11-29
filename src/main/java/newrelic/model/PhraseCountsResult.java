package newrelic.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhraseCountsResult {

    public final String source;
    public final int phraseSize;
    private final Map<String, MutableInt> counts;

    public PhraseCountsResult(final String source, final int phraseSize) {
        this.source = source;
        this.phraseSize = phraseSize;
        this.counts = new HashMap<>();
    }

    public void increment(final String phrase) {
        if (counts.containsKey(phrase)) {
            counts.get(phrase).increment();
        } else {
            counts.put(phrase, new MutableInt());
        }
    }

    public void printTopN(final int n) {
        if (counts.isEmpty()) {
            System.out.println("Failed to count any phrases");
            return;
        }
        System.out.printf("Top %d %d-word phrases in %s%n", n, phraseSize, source);
        getTopN(n).forEach(System.out::println);
    }

    public List<PhraseCount> getTopN(final int n) {
        return counts.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(n)
            .map(e -> new PhraseCount(e.getKey(), e.getValue().get()))
            .collect(Collectors.toList());
    }
}
