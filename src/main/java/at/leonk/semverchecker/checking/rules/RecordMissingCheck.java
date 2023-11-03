package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.Violation;
import at.leonk.semverchecker.checking.query.Predicates;
import at.leonk.semverchecker.checking.query.Queries;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Collection;
import java.util.stream.Stream;

public class RecordMissingCheck implements SemverCheck {
    @Override
    public Stream<Violation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return baselineElements.stream()
                .flatMap(Queries.findPublicTypesOfKind(ElementKind.RECORD))
                .filter(baselineElement -> currentElements.stream()
                        .flatMap(Queries.findPublicTypesOfKind(ElementKind.RECORD))
                        .noneMatch(Predicates.matchesQualifiedNameOf(baselineElement)))
                .map(missingElement -> Violation.Removed(missingElement.getQualifiedName().toString(), "record", missingElement.getQualifiedName().toString()));
    }

}
