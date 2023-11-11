import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class GitTest {

    @Test
    @Disabled
    public void testGit() throws IOException, GitAPIException {

        String path = "/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/baseline";

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = builder.setWorkTree(new File(path)).findGitDir().build();

        Path gitDir = Paths.get(repo.getDirectory().getParent());
        Path destGitDir = Paths.get(".smrvtmp", "baseline");

        Ref git = Git.open(destGitDir.resolve(".git").toFile()).checkout().setName("e644d87b33d42c76d213268e40599b95fcbdf489").setForced(true).call();

        Path returnedPath = destGitDir.resolve(gitDir.relativize(Paths.get(path)));
        System.out.println("returnedPath = " + returnedPath);

    }
}
