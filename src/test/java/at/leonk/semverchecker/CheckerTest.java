package at.leonk.semverchecker;

import at.leonk.semverchecker.checking.Checker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckerTest {
    @ParameterizedTest
    @MethodSource("getTestProjects")
    void testAllRules(Path oldProject, Path newProject) throws IOException {
        var report = Checker.check(oldProject, newProject);
        assertTrue(report.breaking());
    }

    public static Stream<Arguments> getTestProjects() throws URISyntaxException, IOException {
        var testProjectsRoot = CheckerTest.class.getResource("/test-projects");
        return Files.find(Path.of(testProjectsRoot.toURI()), 1, (path, attrs) -> attrs.isDirectory() && !path.endsWith("test-projects"))
                .map(testProject -> Arguments.of(
                        testProject.resolve("old"),
                        testProject.resolve("new")
                ));
    }
}