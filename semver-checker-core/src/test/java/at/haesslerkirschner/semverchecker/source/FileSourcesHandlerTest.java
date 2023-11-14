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

        FileSource baseline = new FileSource("src/test/resources/test-projects/class-missing/baseline");
        FileSource current = new FileSource("src/test/resources/test-projects/class-missing/current");

        FileSourcesHandler underTest = new FileSourcesHandler(baseline, current);

        Path expectedBaseline = Paths.get("src/test/resources/test-projects/class-missing/baseline");
        Path actualBaseline = underTest.resolveBaseline();

        Path expectedCurrent = Paths.get("src/test/resources/test-projects/class-missing/current");
        Path actualCurrent = underTest.resolveCurrent();

        assertEquals(expectedBaseline, actualBaseline);

        assertEquals(expectedCurrent, actualCurrent);
        assertTrue(expectedCurrent.toFile().exists());
        assertTrue(expectedCurrent.toFile().isDirectory());

    }

}