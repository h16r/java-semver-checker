package at.leonk.semverchecker.checking;

import at.leonk.semverchecker.checking.rules.ClassMissingCheck;
import at.leonk.semverchecker.checking.rules.RecordMissingCheck;
import at.leonk.semverchecker.parsing.PublicApiParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Checker {

    public static Report check(Path baselineApiPath, Path currentApiPath) throws IOException {

        var publicApi = PublicApiParser.parse(baselineApiPath, currentApiPath);

        var allChecks = Stream.of(new ClassMissingCheck(), new RecordMissingCheck());

        return Report.of(allChecks
                .flatMap(checker -> checker.check(publicApi.baselineElements(), publicApi.currentElements()))
                .toList());
    }
}
