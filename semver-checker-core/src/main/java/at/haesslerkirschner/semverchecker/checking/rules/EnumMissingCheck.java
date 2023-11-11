package at.haesslerkirschner.semverchecker.checking.rules;

import at.haesslerkirschner.semverchecker.checking.SemverCheck;
import at.haesslerkirschner.semverchecker.checking.ViolatingLocation;
import at.haesslerkirschner.semverchecker.checking.query.Predicates;
import at.haesslerkirschner.semverchecker.checking.query.Queries;
import at.haesslerkirschner.semverchecker.checking.Constants;

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
