package newrelic.service;

import newrelic.model.PhraseCountsResult;
import newrelic.util.InputReader;
import newrelic.util.PhraseCounter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhraseCounterServiceTest {

    @Mock
    private InputReader reader;

    @Mock
    private PhraseCounter counter;

    @InjectMocks
    private PhraseCounterService service;

    @Test
    public void textReadFromStdInWhenNoPathsFound() {
        List<Path> emptyPaths = Collections.emptyList();
        String[] args = new String[0];

        when(reader.toPaths(args)).thenReturn(emptyPaths);
        when(counter.countPhrasesFromStdIn())
            .thenReturn(new PhraseCountsResult("test", 1));

        service.countPhrases(args);

        verify(reader).toPaths(args);
        verify(counter).countPhrasesFromStdIn();
        verify(counter, Mockito.never()).countPhrasesFrom(any(Path.class));
    }

    @Test
    public void textNotReadFromStdInWhenAtLeastOnePathFound() {
        String[] args = new String[0];
        Path path = Path.of("this", "is", "a", "test");

        when(reader.toPaths(args)).thenReturn(List.of(path));
        when(counter.countPhrasesFrom(path))
            .thenReturn(new PhraseCountsResult("test", 1));

        service.countPhrases(args);

        verify(reader).toPaths(args);
        verify(counter).countPhrasesFrom(path);
        verify(counter, Mockito.never()).countPhrasesFromStdIn();
    }
}
