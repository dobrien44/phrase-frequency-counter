package newrelic.util;

import newrelic.model.PhraseCountsResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Counts frequency of all phrases of given 'phraseSize'
 */
public class PhraseCounter {

    private static final String REGEX_DELIMITER = "[^A-Za-z0-9+'’]";
    private final int phraseSize;

    public PhraseCounter(final int phraseSize) {
        if (phraseSize <= 0) {
            throw new IllegalArgumentException("Must provide phraseSize > 0");
        }
        this.phraseSize = phraseSize;
    }

    public PhraseCountsResult countPhrasesFrom(final Path path) {
        try(FileReader fr = new FileReader(path.toFile());
            BufferedReader br = new BufferedReader(fr)) {
            return countPhrases(br, path.getFileName().toString());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read from path " + path.getFileName().toString(), ex);
        }
    }

    public PhraseCountsResult countPhrasesFromStdIn() {
        try(InputStreamReader ir = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(ir)) {
            return countPhrases(br, "StdIn");
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read from StdIn", ex);
        }
    }

    private PhraseCountsResult countPhrases(final BufferedReader reader, final String source) throws IOException {
        PhraseCountsResult result = new PhraseCountsResult(source, phraseSize);
        String[] prev = null;
        String[] cur;
        String line;

        while (reader.ready() && (line = reader.readLine()) != null) {
            cur = tokenize(line);

            if (prev != null) {
                cur = concat(prev, cur);
            }

            var i = 0;
            while (tally(cur, i, result)) i++;
            prev = Arrays.copyOfRange(cur, i, cur.length);
        }
        return result;
    }

    private String[] concat(String[] a, String[] b) {
        return Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray(String[]::new);
    }

    /**
     * Split line by non-word characters (except apostrophes), remove empty strings, and then return
     * a String array with all entries lowercased.
     * @param line
     * @return
     */ // Visible for testing
    String[] tokenize(String line) {
        return Arrays.stream(line.split(REGEX_DELIMITER))
            .filter(s -> !s.isBlank())
            .map(String::toLowerCase)
            .toArray(String[]::new);
    }

    /**
     * Extract a phrase of size 'result.phraseSize' from 'parts' as of the 'start' index and increment the count
     * for that phrase in the provided 'result'.
     * @param parts
     * @param start
     * @param result
     * @return true if a phrase of the expected size could be found; otherwise false
     */ // Visible for testing
    boolean tally(final String[] parts, final int start, final PhraseCountsResult result) {
        var end = start + result.phraseSize;
        if (end <= parts.length) {
            var phrase = String.join(" ", Arrays.copyOfRange(parts, start, end));
            result.increment(phrase);
            return true;
        } else {
            // can not form new phrase of size numWords from start position
            return false;
        }
    }
}
