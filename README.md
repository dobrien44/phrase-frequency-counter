## Phrase Frequency Counter

This application processes text files and identifies the most common 'n-sized' phrases.
Input may be provided as follows:

 1) space-separated file names via command line args
 2) catenated txt files piped in to StdIn

Option (1) counts phrases per file whereas option (2) counts phrases across all text

### How to test/build

```
./gradlew clean build
```

Tests will be executed as part of the build task.

### How to run

```
./gradlew run --args "src/main/java/resources/moby-dick.txt src/main/java/resources/brother-karamazov.txt"

cat src/main/java/resources/*.txt ./gradlew run
```

Alternatively, once the project has been built

```
java -jar build/libs/newrelic.jar src/main/java/resources/moby-dick.txt src/main/java/resources/brothers-karamazov.txt

cat src/main/java/resources/*.txt | java -jar build/libs/newrelic.jar
```

### Implementation Details

The use of the custom `MutableInt` came about in an effort to improve processing time. The initial approach used a
`Map<String, Integer>` to track frequency of phrases, which required many Integer objects to be created whenever a
phrase count was incremented.

The use of the custom `MutableInt` with a primitive int counter greatly improved performance by reducing object
instantiation. For example, processing moby-dick.txt took between 4-5 seconds with the initial approach and now
takes ~500ms.

### Next steps

With more time, I'd do the following:

 - More robust unit testing for tokenization edge cases (see known issues #1 below)
 - If this were a real application, I'd use a proper logger (instead of printing to std out) and have more contextual 
error messages/exceptions
 - Hyphens are presently treated as punctuation (i.e., ignored and replaced with whitespace), but given more time, it
would be nice to treat words separated by a hyphen as a single word, which would require special handling at line
breaks.
 - Better sanitization of input texts to remove additional metadata text provided by project gutenberg (and any other 
text not actually part of the book itself.

### Known issues

 1) handling of apostrophes could be improved. There are cases where single quotes are used for something other than
contractions, which results in unexpected phrases like `15 - ’ he said`

