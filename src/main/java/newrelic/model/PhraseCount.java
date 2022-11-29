package newrelic.model;

import java.util.Objects;

public class PhraseCount {
    public final String phrase;
    public final int count;

    public PhraseCount(String phrase, int count) {
        this.phrase = phrase;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("%4d - %s", count, phrase);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PhraseCount that = (PhraseCount) o;
        return count == that.count && phrase.equals(that.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phrase, count);
    }
}
