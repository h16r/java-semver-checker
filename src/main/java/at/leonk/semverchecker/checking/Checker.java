package at.leonk.semverchecker.checking;

import at.leonk.semverchecker.checking.rules.ClassMissingCheck;
import at.leonk.semverchecker.checking.rules.EnumMissingCheck;
import at.leonk.semverchecker.checking.rules.EnumValueAddedCheck;
import at.leonk.semverchecker.checking.rules.RecordMissingCheck;
import at.leonk.semverchecker.parsing.PublicApiParser;

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
