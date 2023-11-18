package at.haesslerkirschner.semverchecker.source;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

public class FileSource{

    public static final String TMP_DIR = "/tmp/semver";

    private final Path path;

    private final Optional<String> commit;

    private Optional<Path> resolved = Optional.empty();

    public FileSource(Path path) {
        this.path = path;
        this.commit = Optional.empty();
        this.resolved = Optional.empty();
    }

    public FileSource(Path path, String commit) {
        this.path = path;
        this.commit = Optional.ofNullable(commit);
    }

    private Path copyAndCheckout(GitRepository sourceRepo, Path sourcePath, Path destination, String commitId) throws IOException {

        GitRepository destinationRepo = sourceRepo
                .copyTo(destination)
                .checkout(commitId);

        return destinationRepo.getRootDir().resolve(sourceRepo.getRootDir().relativize(sourcePath));
    }

    public Path resolve() {

        if (commit.isEmpty()) {
            return path;
        }

        Path tempPath = Paths.get(TMP_DIR, path.getFileName().toString(), commit.get());

        try {
            GitRepository gitRepository = toGitRepository();
            Path resolvedPath = copyAndCheckout(gitRepository, path, tempPath, commit.get());
            this.resolved = Optional.of(resolvedPath);
            return resolvedPath;
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't checkout git repository " + e.getMessage());
        }
    }

    private GitRepository toGitRepository() throws IOException {
        return new GitRepository(path);
    }
}
