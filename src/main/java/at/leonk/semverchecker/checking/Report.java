package at.leonk.semverchecker.checking;

import java.util.List;

public record Report(List<Violation> differences) {
    public boolean breaking() {
        return !differences.isEmpty();
    }
}
