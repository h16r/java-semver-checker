package at.haesslerkirschner.semverchecker.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitRepositoryTest {

    Path initGitRepository() throws GitAPIException {

        Path gitRepository = FileOps.tmpDir().resolve("git").resolve(UUID.randomUUID().toString());

        Git repository = Git.init().setDirectory(gitRepository.toFile()).call();

        return gitRepository;
    }

    @BeforeAll
    static void init() throws IOException {
        if (Files.exists(Paths.get("/tmp/semver"))) {
            Files.walk(Paths.get("/tmp/semver"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void loadGitRepositoryByGitDirectory() throws IOException, GitAPIException {

        Path baseline = initGitRepository();
        GitRepository underTest = new GitRepository(baseline);

        assertEquals(underTest.getRootDir(), underTest.getRootDir());

    }

    @Test
    void copyGitDirectoryToTmp() throws IOException, GitAPIException {

        Path targetPath = FileOps.tmpDir().resolve("example");

        Path gitDirectory = initGitRepository();

        GitRepository underTest = GitRepository.viaGitDir(gitDirectory.toFile());
        GitRepository target = underTest.copyTo(targetPath);

        assertEquals(targetPath, target.getRootDir());
        assertTrue(target.getRootDir().toFile().isDirectory());
        assertTrue(target.getRootDir().toFile().exists());
    }
}