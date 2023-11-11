package at.haesslerkirschner.semverchecker.checking;

import at.haesslerkirschner.semverchecker.checking.rules.ClassMissingCheck;
import at.haesslerkirschner.semverchecker.checking.rules.EnumMissingCheck;
import at.haesslerkirschner.semverchecker.checking.rules.EnumValueAddedCheck;
import at.haesslerkirschner.semverchecker.checking.rules.RecordMissingCheck;
import at.haesslerkirschner.semverchecker.parsing.PublicApiParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Checker {

    public static Report check(Path baselineApiPath, Path currentApiPath) throws IOException {

        var publicApi = PublicApiParser.parse(baselineApiPath, currentApiPath);

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
