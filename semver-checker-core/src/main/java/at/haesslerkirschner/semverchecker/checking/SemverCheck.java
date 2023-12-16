package at.haesslerkirschner.semverchecker.checking;

import at.haesslerkirschner.semverchecker.Bump;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface SemverCheck {
    String description();

    String docUrl();

    Stream<ViolatingLocation> check(Collection<Element> baselineElements, Collection<Element> currentElements);

    default Optional<Violation> checkForViolations(Collection<Element> baselineElements, Collection<Element> currentElements, Bump bump) {
        var violatingLocations = check(baselineElements, currentElements).toList();
        if (violatingLocations.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Violation(getClass().getSimpleName(), description(), docUrl(), violatingLocations));
    }
}
