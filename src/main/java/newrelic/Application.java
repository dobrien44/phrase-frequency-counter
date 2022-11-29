package newrelic;

import newrelic.service.PhraseCounterService;

public class Application {

    /**
     * Args passed to this method are expected to be space-separated file names
     * @param args
     */
    public static void main(String[] args) {
        var start = System.currentTimeMillis();

        try {
            var results = PhraseCounterService.create().countPhrases(args);
            results.forEach(r -> r.printTopN(100));
        } catch (Throwable t) {
            System.out.println("Something went wrong... " + t);
        }

        var runtime = System.currentTimeMillis() - start;
        System.out.printf("Executed in %d ms%n", runtime);
    }
}
