package at.leonk.semverchecker.checking;


import java.util.List;

public record Violation(
        String code,
        String description,
        String docUrl,
        List<ViolatingLocation> locations
) {
}
