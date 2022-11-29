package newrelic.model;

/**
 * Custom Integer Model to efficiently increment counts in a frequency map.
 */
public class MutableInt implements Comparable<MutableInt> {

    private int value = 1;

    public void increment() {
        ++value;
    }

    public int get() {
        return value;
    }

    @Override
    public int compareTo(final MutableInt other) {
        return this.get() - other.get();
    }
}
