package at.haesslerkirschner.semverchecker.checking;

import at.haesslerkirschner.semverchecker.checking.rules.ClassMissingCheck;
import at.haesslerkirschner.semverchecker.checking.rules.EnumMissingCheck;
import at.haesslerkirschner.semverchecker.checking.rules.EnumValueAddedCheck;
import at.haesslerkirschner.semverchecker.checking.rules.RecordMissingCheck;
import at.haesslerkirschner.semverchecker.parsing.PublicApiParser;
import at.haesslerkirschner.semverchecker.source.FileSource;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Checker {

    private static final Logger LOGGER = Logger.getLogger(PublicApiParser.class.getSimpleName());

    public static Report check(FileSource baselineApiPath, FileSource currentApiPath) throws IOException {

        var baseline = baselineApiPath.resolve();
        var current = currentApiPath.resolve();

        LOGGER.info(() -> "Checking baseline: '%s' against current '%s'".formatted(baseline, current));

        var publicApi = PublicApiParser.parse(baseline, current);

        var allChecks = Stream.of(
                new ClassMissingCheck(),
                new RecordMissingCheck(),
                new EnumValueAddedCheck(),
                new EnumMissingCheck()
        );

        return new Report(allChecks
                .flatMap(checker -> checker.checkForViolations(publicApi.baselineElements(), publicApi.currentElements()).stream())
                .toList());
    }
}
