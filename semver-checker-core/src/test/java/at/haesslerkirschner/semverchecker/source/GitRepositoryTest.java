package at.haesslerkirschner.semverchecker.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
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


    @AfterEach
    void init() throws IOException {
       FileOps.deleteTmpDir();
    }

    @Test
    void loadGitRepositoryByGitDirectory() throws IOException, GitAPIException {

        Path baseline = GitOps.initGitRepository();
        GitRepository underTest = new GitRepository(baseline);

        assertEquals(underTest.getRootDir(), underTest.getRootDir());

    }

    @Test
    void copyGitDirectoryToTmp() throws IOException, GitAPIException {

        Path targetPath = FileOps.tmpDir("example");

        Path gitDirectory = GitOps.initGitRepository();

        GitRepository underTest = new GitRepository(gitDirectory);
        GitRepository target = underTest.copyTo(targetPath);

        assertEquals(targetPath, target.getRootDir());
        assertTrue(target.getRootDir().toFile().isDirectory());
        assertTrue(target.getRootDir().toFile().exists());
    }
}