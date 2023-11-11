package at.haesslerkirschner.semverchecker.source;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileSourcesHandlerTest {

    @Test
    public void testResolving() throws IOException {

        FileSource baseline = new FileSource("src/test/resources/test-projects/class-missing");
        FileSource current = FileSource.viaGitDir("src/test/resources/git-projects/example/.git", Optional.of("27504898efe5d583f573c923d754d0bcae61d20c"));

        FileSourcesHandler underTest = new FileSourcesHandler(baseline, current);

        Path expectedBaseline = Paths.get("src/test/resources/test-projects/class-missing");
        Path actualBaseline = underTest.resolveBaseline();

        Path expectedCurrent = Paths.get("/tmp/semver/example/27504898efe5d583f573c923d754d0bcae61d20c");
        Path actualCurrent = underTest.resolveCurrent();

        assertEquals(expectedBaseline, actualBaseline);

        assertEquals(expectedCurrent, actualCurrent);
        assertTrue(expectedCurrent.toFile().exists());
        assertTrue(expectedCurrent.toFile().isDirectory());

        Files.walk(Paths.get("/tmp/semver"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        assertFalse(Files.exists(Paths.get("/tmp/semver")));

    }

}