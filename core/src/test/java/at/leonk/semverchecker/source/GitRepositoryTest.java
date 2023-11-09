package at.leonk.semverchecker.source;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class GitRepositoryTest {

    @Test
    void loadGitRepositoryByGitDirectory() throws IOException {

        Path gitDirectory = Paths.get("src/test/resources/git-projects/example/.git");

        GitRepository underTest = GitRepository.viaGitDir(gitDirectory.toFile());
        assertEquals(gitDirectory.getParent(), underTest.getRootDir());

    }

    @Test
    void copyGitDirectoryToTmp() throws IOException {

        Path gitDirectory = Paths.get("src/test/resources/git-projects/example/.git");

        Path targetPath = Paths.get("/tmp/semver/example");

        GitRepository underTest = GitRepository.viaGitDir(gitDirectory.toFile());
        GitRepository target = underTest.copyTo(targetPath);

        assertEquals(Paths.get(targetPath.toString()), target.getRootDir());
        assertTrue(target.getRootDir().toFile().isDirectory());
        assertTrue(target.getRootDir().toFile().exists());
    }


}