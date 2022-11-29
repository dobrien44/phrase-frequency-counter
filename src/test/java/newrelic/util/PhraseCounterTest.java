package newrelic.util;

import newrelic.model.PhraseCount;
import newrelic.model.PhraseCountsResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhraseCounterTest {

    private PhraseCounter counter = new PhraseCounter(3);

    private final String testText = "a b c. a b c.";
    private final String testTextWithNewLine = "a b c.\na b c.";
    private final List<PhraseCount> expected = List.of(
        new PhraseCount("a b c", 2),
        new PhraseCount("b c a", 1),
        new PhraseCount("c a b", 1)
    );

    @Test
    public void assertExpectedPhraseCountsFromFile() throws IOException {
        var path = Files.createTempFile(null, null);
        Files.write(path, testText.getBytes());

        var results = counter.countPhrasesFrom(path);
        assertEquals(path.getFileName().toString(), results.source);
        assertEquals(expected, results.getTopN(10));
        Files.deleteIfExists(path);
    }

    @Test
    public void assertPhrasesCountedAcrossNewLines() throws IOException {
        var path = Files.createTempFile(null, null);
        Files.write(path, testTextWithNewLine.getBytes());

        var results = counter.countPhrasesFrom(path);
        assertEquals(path.getFileName().toString(), results.source);
        assertEquals(expected, results.getTopN(10));
        Files.deleteIfExists(path);
    }

    @Test
    public void assertExpectedPhraseCountsFromStdIn() {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(testText.getBytes())) {
            System.setIn(bais);

            var results = counter.countPhrasesFromStdIn();
            assertEquals("StdIn", results.source);
            assertEquals(expected, results.getTopN(10));
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    public void assertExpectedTokensFromInput() {
        var expected = new String[] { "this", "is", "a", "test" };
        List.of(
            "this is a test", // whitespace delimiter
            "this is a \uD83C\uDF68 test", // unicode removed
            "this.is,a:test", // punctuation ignored
            "this-is-a-test" // hyphens ignored
        ).forEach(line -> assertArrayEquals(expected, counter.tokenize(line)));
    }

    @Test
    public void assertThatApostrophesAreAllowed() {
        assertArrayEquals(new String[] { "don't" }, counter.tokenize("don't"));
        assertArrayEquals(new String[] { "can’t" }, counter.tokenize("can’t"));
    }

    @Test
    public void assertFalseWhenPhraseNotAdded() {
        var result = new PhraseCountsResult("test", 3);
        assertFalse(counter.tally(new String[0],0, result));
        assertTrue(result.getTopN(10).isEmpty());
    }

    @Test
    public void assertTrueWhenPhraseAdded() {
        var result = new PhraseCountsResult("test", 4);
        assertTrue(counter.tally(new String[] { "this", "is", "a", "test"},0, result));

        var expected = new PhraseCount("this is a test", 1);
        var topPhrases = result.getTopN(10);
        assertEquals(1, topPhrases.size());
        assertEquals(expected, topPhrases.get(0));
    }
}
