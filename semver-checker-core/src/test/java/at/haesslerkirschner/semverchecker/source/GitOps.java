package at.haesslerkirschner.semverchecker.source;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class GitOps {

    public static Path initGitRepository() throws GitAPIException {

        Path gitRepository = FileOps.tmpDir(UUID.randomUUID().toString());

        Git.init().setDirectory(gitRepository.toFile()).call();

        return gitRepository;
    }

    public static Git initGitRepositoryWithFiles(Path sources) throws GitAPIException, IOException {

        Path gitRepository = FileOps.tmpDir(sources.getFileName().toString());

        FileOps.copyDir(sources, gitRepository);

        return Git.init().setDirectory(gitRepository.toFile()).call();
    }
}
