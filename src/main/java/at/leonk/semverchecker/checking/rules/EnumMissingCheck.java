package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.Constants;
import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.ViolatingLocation;
import at.leonk.semverchecker.checking.query.Predicates;
import at.leonk.semverchecker.checking.query.Queries;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Collection;
import java.util.stream.Stream;

public class EnumMissingCheck implements SemverCheck {
    @Override
    public String description() {
        return "enum has been renamed or removed";
    }

    @Override
    public String docUrl() {
        return Constants.REPO_URL + "#major-renamingmovingremoving-any-public-elements";
    }

    @Override
    public Stream<ViolatingLocation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return baselineElements.stream()
                .flatMap(Queries.findPublicTypesOfKind(ElementKind.ENUM))
                .filter(baselineElement -> currentElements.stream()
                        .flatMap(Queries.findPublicTypesOfKind(ElementKind.ENUM))
                        .noneMatch(Predicates.matchesQualifiedNameOf(baselineElement)))
                .map(ViolatingLocation::fromElement);
    }
}
