package at.haesslerkirschner.semverchecker.source;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSourcesHandler {

    private final String tmpPrefix = "/tmp/semver";
    private final FileSource baseline;
    private final FileSource current;

    public FileSourcesHandler(FileSource baseline, FileSource current) {
        this.baseline = baseline;
        this.current = current;
    }

    private Path copyAndCheckout(GitRepository sourceRepo, Path sourcePath, Path destination, String commitId) throws IOException {

        GitRepository destinationRepo = sourceRepo
                .copyTo(destination)
                .checkout(commitId);

        return destinationRepo.getRootDir().resolve(sourceRepo.getRootDir().relativize(sourcePath));
    }

    private Path resolve(FileSource source) {

        Path sourcePath = Paths.get(source.path());

        if (source.commit().isEmpty()) {
            return sourcePath;
        }

        Path tempPath = Paths.get(tmpPrefix, sourcePath.getFileName().toString(), source.commit().get());

        try {
            GitRepository gitRepository = source.toGitRepository();
            return copyAndCheckout(gitRepository, Paths.get(source.path()), tempPath, source.commit().get());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempPath;
    }

    public Path resolveCurrent() {
        return resolve(current);
    }

    public Path resolveBaseline() {
        return resolve(baseline);
    }

}
