package newrelic.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputReaderTest {

    InputReader reader = new InputReader();

    @Test
    public void assertInvalidArgsRemovedFromPaths() {
        var invalidArgs = new String[] { "not-a-file", "garbage" };
        var paths = reader.toPaths(invalidArgs);
        assertTrue(paths.isEmpty());
    }

    @Test
    public void assertValidArgReturnedAsPath() throws IOException {
        var path = Files.createTempFile(null, null);
        var args = new String[] { path.toString() };
        var paths = reader.toPaths(args);

        assertEquals(1, paths.size());
        assertEquals(path.toString(), paths.get(0).toString());

        Files.deleteIfExists(path);
    }


}
