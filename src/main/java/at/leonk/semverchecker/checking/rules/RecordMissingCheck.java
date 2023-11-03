package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.Diff;
import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.TypePredicate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RecordMissingCheck implements SemverCheck {
    @Override
    public Stream<Diff> check(Set<Element> baselineElements, Set<Element> currentElements) {

        final Predicate<TypeElement> presentInCurrentElements = (baseline) ->
                publicRecords(currentElements)
                        .noneMatch(currentElement -> currentElement
                                .getQualifiedName()
                                .equals(baseline.getQualifiedName()));

        return publicRecords(baselineElements).filter(presentInCurrentElements).map(missingElement -> Diff.Removed(missingElement.getQualifiedName().toString(), "record", missingElement.getQualifiedName().toString()));
    }

    private static Stream<TypeElement> publicRecords(Set<Element> elements) {
        return elements.stream()
                .filter(TypePredicate.PUBLIC_RECORD)
                .map(element -> (TypeElement) element);
    }
}
