package at.leonk.semverchecker.source;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public record FileSource(String path, Optional<String> gitDir, Optional<String> commit) {
    FileSource(String path) {
        this(path, Optional.empty(), Optional.empty());
    }

    static FileSource viaGitDir(String gitDir, Optional<String> commit) {
        return new FileSource(Paths.get(gitDir).getParent().toString(), Optional.of(gitDir), commit);
    }

    GitRepository toGitRepository() throws IOException {

        return gitDir().map(gitDir -> {
            try {
                return GitRepository.viaGitDir(new File(gitDir));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(new GitRepository(Paths.get(this.path)));

    }
}
