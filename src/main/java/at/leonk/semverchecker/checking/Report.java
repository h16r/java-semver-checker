package at.leonk.semverchecker.checking;

import java.util.List;

public record Report(boolean breaking, List<Violation> differences) {

    public static Report of(List<Violation> violations) {
        return new Report(
                violations.stream().anyMatch(diff -> diff.criticality() == Violation.Criticality.BREAKING),
                violations
        );
    }
}
