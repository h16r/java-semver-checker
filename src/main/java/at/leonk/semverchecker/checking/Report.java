package at.leonk.semverchecker.checking;

import java.util.List;

public record Report(boolean breaking, List<Diff> differences) {

    public static Report of(List<Diff> diffs) {
        return new Report(
                diffs.stream().anyMatch(diff -> diff.criticality() == Diff.Criticality.BREAKING),
                diffs
        );
    }
}
