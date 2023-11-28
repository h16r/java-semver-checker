package at.haesslerkirschner.semverchecker.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public record ResolvedFileSource(Path path, boolean sameAsSource) implements AutoCloseable {
    @Override
    public void close() {
            try {
                if (!sameAsSource && Files.exists(path)) {
                    Files.walk(path, 1000)
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
