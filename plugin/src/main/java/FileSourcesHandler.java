import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileSourcesHandler {

    private final String tmpPrefix = ".smvrtmp";

    private final FileSource baseline;
    private final FileSource current;

    public FileSourcesHandler(FileSource baseline, FileSource current) {
        this.baseline = baseline;
        this.current = current;
    }

    private Path copyAndCheckout(Path source, Path destination, String commitId) throws IOException {

        GitRepository sourceRepo = new GitRepository(source);

        GitRepository destinationRepo = sourceRepo
                .copyTo(destination)
                .checkout(commitId);

        System.out.println("destinationRepo.getRootDir().resolve(sourceRepo.getRootDir().relativize(source.toAbsolutePath())) = " + destinationRepo.getRootDir().resolve(sourceRepo.getRootDir().relativize(source.toAbsolutePath())));
        System.out.println("destinationRepo.getRootDir() = " + destinationRepo.getRootDir());
        return destinationRepo.getRootDir().resolve(sourceRepo.getRootDir().relativize(source.toAbsolutePath()));
    }

    private Path resolve(FileSource source) {

        Path sourcePath = Paths.get(source.getPath());

        if (source.getCommit().isEmpty()) {
            return sourcePath;
        }

        Path tempPath = Paths.get(tmpPrefix, sourcePath.getFileName().toString());

        try {
            return copyAndCheckout(Paths.get(source.getPath()), tempPath, source.getCommit().get());
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
