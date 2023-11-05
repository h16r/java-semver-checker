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
import java.util.stream.Stream;

public class GitRepository {

    private final Repository repository;

    public GitRepository(Path source) throws IOException {

        this.repository = new FileRepositoryBuilder()
                .setWorkTree(source.toFile())
                .findGitDir()
                .build();
    }

    public GitRepository (File gitDir) throws IOException {
        this.repository = new FileRepositoryBuilder().setGitDir(gitDir).build();

    }

    public GitRepository copyTo(Path target) throws IOException {

        try {
            Files.createDirectories(target);
        } catch (Exception e) {
            //
        }

        Path gitRootDir = Paths.get(repository.getDirectory().getParent());

        try (Stream<Path> stream = Files.walk(gitRootDir)) {
            stream.filter(source -> !source.toString().contains(".smvr")).forEach(source -> {
                try {
                    Files.copy(source, target.resolve(gitRootDir.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    //
                }
            });
        }

        System.out.println("target = " + target);

        return new GitRepository(target.resolve(".git").toFile());
    }

    public GitRepository checkout(String commitId) {

        try {
            Git.wrap(this.repository).checkout().setName(commitId).setForced(true).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        return this;
    }

    public Path getRootDir() {
        return Paths.get(this.repository.getDirectory().getParent());
    }
}
