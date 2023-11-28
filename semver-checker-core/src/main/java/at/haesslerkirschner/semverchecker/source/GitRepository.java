package at.haesslerkirschner.semverchecker.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import java.util.stream.Stream;

final class GitRepository {

    private final Repository repository;

    private static final Logger LOGGER = Logger.getLogger(GitRepository.class.getSimpleName());

    public GitRepository(Path source) throws IOException {

        this.repository = new FileRepositoryBuilder()
                .findGitDir(source.toFile())
                .build();
    }

    private GitRepository(File gitDir) throws IOException {
        this.repository = new FileRepositoryBuilder().setGitDir(gitDir).build();
    }

    GitRepository copyTo(Path target) throws IOException {

        Path gitRootDir = Paths.get(repository.getDirectory().getParent());

        LOGGER.info(() -> "Copying git repository from %s to %s".formatted(gitRootDir, target));

        try {
            LOGGER.fine(() -> "Creating parent directory");
            Files.createDirectories(target);
        } catch (Exception e) {
            LOGGER.severe(() -> "Error creating parent directory: " + e.getMessage());
        }

        try (Stream<Path> stream = Files.walk(gitRootDir)) {
            stream.forEach(source -> {
                try {
                    Files.copy(source, target.resolve(gitRootDir.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("Couldn't copy git repository to path '%s'. Error: %s ".formatted(target, e.getMessage()));
                }
            });
        }

        return new GitRepository(target.resolve(".git").toFile());
    }

    GitRepository checkout(String ref) {

        try {
            Git.wrap(this.repository).checkout().setName(ref).call();
        } catch (GitAPIException e) {
            LOGGER.severe(() -> "Couldn't checkout ref '%s'; Error: %s".formatted(ref, e.getMessage()));
            throw new IllegalStateException(e);
        }

        return this;
    }

    public Path getRootDir() {
        return Paths.get(this.repository.getDirectory().getParent());
    }
}
