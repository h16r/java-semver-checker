package at.haesslerkirschner.semverchecker.source;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

public class FileSource {

    public static final String TMP_DIR = "/tmp/semver";

    private final Path path;

    private String commit;

    private String branch;

    public FileSource(Path path) {
        this.path = path;
    }

    public FileSource(Path path, String commit, String branch) {
        this.path = path;
        this.commit = commit;
        this.branch = branch;
    }

    private Path copyAndCheckout(GitRepository sourceRepo, Path sourcePath, Path destination, String commitId, String branch) throws IOException {

        GitRepository destinationRepo = sourceRepo
                .copyTo(destination);

        Optional.ofNullable(branch).map(destinationRepo::checkout);
        Optional.ofNullable(commitId).map(destinationRepo::checkout);

        return destinationRepo.getRootDir().resolve(sourceRepo.getRootDir().relativize(sourcePath));
    }

    public Path resolve() {

        if (commit == null && branch == null) {
            return path;
        }

        String sourceFolder = path.getFileName().toString();

        if (path.toString().equals(".")) {
            sourceFolder = FileSystems.getDefault().getPath("").toAbsolutePath().getFileName().toString();
        }

        Path tempPath = Paths.get(TMP_DIR, sourceFolder, Optional.ofNullable(commit).orElse(""), Optional.ofNullable(branch).orElse(""));

        try {
            GitRepository gitRepository = toGitRepository();
            return copyAndCheckout(gitRepository, path, tempPath, commit, branch);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't checkout git repository " + e.getMessage());
        }
    }

    private GitRepository toGitRepository() throws IOException {
        return new GitRepository(path);
    }
}
