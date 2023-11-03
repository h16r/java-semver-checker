package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.TypePredicate;
import at.leonk.semverchecker.checking.Violation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RecordMissingCheck implements SemverCheck {
    @Override
    public Stream<Violation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        final Predicate<TypeElement> presentInCurrentElements = (baseline) ->
                publicRecords(currentElements)
                        .noneMatch(currentElement -> currentElement
                                .getQualifiedName()
                                .contentEquals(baseline.getQualifiedName()));

        return publicRecords(baselineElements).filter(presentInCurrentElements).map(missingElement -> Violation.Removed(missingElement.getQualifiedName().toString(), "record", missingElement.getQualifiedName().toString()));
    }

    private static Stream<TypeElement> publicRecords(Collection<Element> elements) {
        return elements.stream()
                .filter(TypePredicate.PUBLIC_RECORD)
                .map(element -> (TypeElement) element);
    }
}
