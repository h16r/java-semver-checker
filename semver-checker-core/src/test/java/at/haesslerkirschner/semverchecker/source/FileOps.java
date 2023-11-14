package at.haesslerkirschner.semverchecker.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileOps {

    private static final Path tmpDir = Paths.get("/tmp", "semver");

    static Path tmpDir() {
        return tmpDir;
    }

    static void deleteTmpDir() {
        try {
            if (Files.exists(tmpDir)) {
                Files.walk(tmpDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
