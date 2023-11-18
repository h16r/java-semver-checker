package at.haesslerkirschner.semverchecker;

import at.haesslerkirschner.semverchecker.checking.Report;
import at.haesslerkirschner.semverchecker.checking.Checker;
import at.haesslerkirschner.semverchecker.source.FileSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckerTest {
    @ParameterizedTest(name = "{index} {2}")
    @MethodSource("getTestProjects")
    void testAllRules(Path baselineProject, Path currentProject, @SuppressWarnings("unused") String testDisplayName) throws IOException {

        var baseline = new FileSource(baselineProject);
        var current = new FileSource(currentProject);

        // No changes when checking against itself, so report should be non-breaking.
        var nonBreakingReport = Checker.check(baseline, baseline);
        assertNonBreaking(nonBreakingReport);

        var report = Checker.check(baseline, current);
        assertBreaking(report);
    }

    private static void assertBreaking(Report report) {
        assertTrue(report.breaking(), "Expected report to be breaking, but was non-breaking: %s".formatted(report));
    }

    private static void assertNonBreaking(Report report) {
        assertFalse(report.breaking(), "Expected report to be non-breaking, but found differences: %s".formatted(report));
    }

    public static Stream<Arguments> getTestProjects() throws URISyntaxException, IOException {
        var testProjectsRoot = CheckerTest.class.getResource("/test-projects");
        return Files.find(Path.of(testProjectsRoot.toURI()), 1, (path, attrs) -> attrs.isDirectory() && !path.endsWith("test-projects"))
                .map(testProject -> Arguments.of(
                        testProject.resolve("baseline"),
                        testProject.resolve("current"),
                        testProject.getFileName().toString()
                ));
    }
}