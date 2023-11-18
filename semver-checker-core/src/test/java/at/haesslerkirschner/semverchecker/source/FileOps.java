package at.haesslerkirschner.semverchecker.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

public class FileOps {

    private static final Path tmpDir = Paths.get(FileSource.TMP_DIR, "tests");

    public static Path tmpDir(String subDirectory) {
        return tmpDir.resolve(subDirectory);
    }

    public static void copyDir(Path from, Path to) throws IOException {
        Files.walkFileTree(from, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Files.createDirectories(to.resolve(from.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, to.resolve(from.relativize(file).toString()));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void deleteTmpDir() {
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
