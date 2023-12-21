package at.haesslerkirschner.semverchecker.checking.rules;

import at.haesslerkirschner.semverchecker.checking.SemverCheck;
import at.haesslerkirschner.semverchecker.checking.ViolatingLocation;
import at.haesslerkirschner.semverchecker.checking.query.Queries;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.stream.Stream;

public class PublicMethodMissingCheck implements SemverCheck {
    @Override
    public String description() {
        return "public method has been renamed or removed";
    }

    @Override
    public String docUrl() {
        return null; // TODO
    }

    @Override
    public Stream<ViolatingLocation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return baselineElements.stream()
                .flatMap(Queries.findPublicTypes())
                .flatMap(Queries.matchElementsByQualifiedNameWith(currentElements))
                .flatMap(matchingTypes -> matchingTypes.baseline().getEnclosedElements().stream()
                        .filter(el -> el.getKind().equals(ElementKind.METHOD) && el.getModifiers().contains(Modifier.PUBLIC))
                        .filter(oldMethod -> matchingTypes.current().getEnclosedElements().stream()
                                .noneMatch(el -> el.getSimpleName().contentEquals(oldMethod.getSimpleName()))
                        )
                )
                .map(ViolatingLocation::fromElement);
    }
}
