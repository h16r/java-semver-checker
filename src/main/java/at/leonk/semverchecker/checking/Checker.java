package at.leonk.semverchecker.checking;

import at.leonk.semverchecker.parsing.PublicApiParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Checker {

    public static Report check(Path oldApiPath, Path newApiPath) throws IOException {
        var publicApi = PublicApiParser.parse(oldApiPath, newApiPath);

        var allChecks = Stream.of(new ClassMissingCheck());
        return Report.of(allChecks
                .flatMap(checker -> checker.check(publicApi.oldElements(), publicApi.newElements()))
                .toList());
    }
}
