package at.haesslerkirschner.semverchecker;

import at.haesslerkirschner.semverchecker.checking.Checker;
import at.haesslerkirschner.semverchecker.checking.Report;
import at.haesslerkirschner.semverchecker.source.FileOps;
import at.haesslerkirschner.semverchecker.source.FileSource;
import at.haesslerkirschner.semverchecker.source.GitOps;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {

    @BeforeAll
    static void setup() {
        FileOps.deleteTmpDir();
    }

    @Test
    void publicInterfaceRemovedAfterCommit() throws GitAPIException, IOException {

        Path source = Paths.get("src/test/resources/test-projects/class-missing/baseline");

        Git repository = GitOps.initGitRepositoryWithFiles(source);
        Path target = repository.getRepository().getDirectory().toPath().getParent();

        Files.deleteIfExists(target.resolve("SomeClass.java"));
        repository.add().addFilepattern(".").call();

        String removalCommit = repository.commit().setMessage("remove public class").call().getName();

        FileSource baseline = new FileSource(Paths.get("src/test/resources/test-projects/class-missing/baseline"));
        FileSource current = new FileSource(target, removalCommit);

        Report report = Checker.check(new Configuration(baseline, current, Bump.PATCH));
        assertTrue(report.breaking());
    }

    @Test
    void publicInterfaceRemovedOnFeatureBranch() throws GitAPIException, IOException {

        Path source = Paths.get("src/test/resources/test-projects/class-missing/baseline");

        Git repository = GitOps.initGitRepositoryWithFiles(source);
        Path target = repository.getRepository().getDirectory().toPath().getParent();

        String featureBranch = "feature";

        repository.add().addFilepattern(".").call();
        repository.commit().setMessage("initial commit").call();

        repository.checkout().setName(featureBranch).setCreateBranch(true).call();

        Files.deleteIfExists(target.resolve("SomeClass.java"));

        repository.add().addFilepattern(".").call();
        repository.commit().setMessage("remove public class").call();

        repository.checkout().setName("main").call();

        FileSource baseline = new FileSource(source);
        FileSource current = new FileSource(target, "feature");

        Report report = Checker.check(new Configuration(baseline, current, Bump.PATCH));
        assertTrue(report.breaking());
    }
}
