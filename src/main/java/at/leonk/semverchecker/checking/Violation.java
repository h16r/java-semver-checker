package at.leonk.semverchecker.checking;


import java.util.Collection;

public record Violation(
        String code,
        String description,
        String docUrl,
        Collection<ViolatingLocation> locations
) {
}
