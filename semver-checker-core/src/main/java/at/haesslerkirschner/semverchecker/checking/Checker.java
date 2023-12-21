package at.haesslerkirschner.semverchecker.checking;

import at.haesslerkirschner.semverchecker.Configuration;
import at.haesslerkirschner.semverchecker.checking.rules.EnumValueAddedCheck;
import at.haesslerkirschner.semverchecker.checking.rules.PublicMethodMissingCheck;
import at.haesslerkirschner.semverchecker.checking.rules.PublicMethodSignatureChangedCheck;
import at.haesslerkirschner.semverchecker.checking.rules.PublicTypeMissingCheck;
import at.haesslerkirschner.semverchecker.parsing.PublicApiParser;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Checker {

    private static final Logger LOGGER = Logger.getLogger(PublicApiParser.class.getSimpleName());

    public static Report check(Configuration configuration) throws IOException {

        try (var baseline = configuration.baseline().resolve();
             var current = configuration.current().resolve()) {

            LOGGER.info(() -> "Checking baseline: '%s' against current '%s'".formatted(baseline.path(), current.path()));

            var publicApi = PublicApiParser.parse(baseline.path(), current.path());

            var allChecks = Stream.of(
                    new PublicTypeMissingCheck(),
                    new PublicMethodSignatureChangedCheck(),
                    new PublicMethodMissingCheck(),
                    new EnumValueAddedCheck()
            );


            return new Report(allChecks
                    .flatMap(checker -> checker.checkForViolations(publicApi.baselineElements(), publicApi.currentElements(), configuration.bump()).stream())
                    .toList());
        }
    }
}
