package at.haesslerkirschner.semverchecker.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileSourcesTest {

    @AfterEach
    void cleanUp() {
        FileSourcesTest.clean();
    }

    @BeforeAll
    static void clean() {
        FileOps.deleteTmpDir();
    }

    @Test
    public void testResolvingWithoutGitOps() {

        FileSource underTest = new FileSource(Paths.get("src/test/resources/test-projects/class-missing/baseline"));

        Path expectedTarget = Paths.get("src/test/resources/test-projects/class-missing/baseline");
        Path actualTarget = underTest.resolve().path();

        assertEquals(expectedTarget, actualTarget);
        assertEquals(expectedTarget, actualTarget);

        assertTrue(expectedTarget.toFile().exists());
        assertTrue(expectedTarget.toFile().isDirectory());

    }

    @Test
    public void testResolvingWithGitOps() throws GitAPIException, IOException {

        Git repository = GitOps.initGitRepositoryWithFiles(Paths.get("src/test/resources/test-projects/class-missing/baseline"));
        RevCommit commit = repository.commit().setMessage("initial commit").call();

        Path repositoryPath = repository.getRepository().getDirectory().toPath().getParent();

        FileSource underTest = new FileSource(repositoryPath, commit.getName());

        Path actualTarget = underTest.resolve().path();
        Path expectedTarget = Paths.get(FileSource.TMP_DIR,"baseline",commit.getName());

        assertEquals(expectedTarget, actualTarget);
    }
}