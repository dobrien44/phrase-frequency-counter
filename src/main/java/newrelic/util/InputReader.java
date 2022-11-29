package newrelic.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InputReader {

    public List<Path> toPaths(String[] args) {
        return Arrays.stream(args)
            .map(this::toPath)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private Path toPath(String arg) {
        var path = Path.of(arg);
        if (Files.isRegularFile(path)) {
            return path;
        } else {
            System.out.printf("%s is not a valid input%n", arg);
            return null;
        }
    }
}
