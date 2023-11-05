import java.util.Optional;

public class FileSource {

    private final String path;
    private final Optional<String> commit;

    public FileSource(String path, Optional<String> commit) {
        this.path = path;
        this.commit = commit;
    }

    public Optional<String> getCommit() {
        return commit;
    }

    public String getPath() {
        return path;
    }
}
