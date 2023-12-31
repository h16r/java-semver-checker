package at.haesslerkirschner.semverchecker.checking.query;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Queries {
    public static Function<Element, Stream<? extends TypeElement>> findPublicTypesOfKind(ElementKind... kinds) {
        return el -> Stream.of(el)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC)
                        && Arrays.stream(kinds).anyMatch(kind -> e.getKind().equals(kind)))
                .map(TypeElement.class::cast);
    }

    public static Function<TypeElement, Stream<MatchingElements<TypeElement>>> matchElementsByQualifiedNameWith(Collection<Element> elements) {
        return el -> elements.stream()
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .filter(Predicates.matchesQualifiedNameOf(el))
                .findAny()
                .map(otherEl -> new MatchingElements<>(el, otherEl))
                .stream();
    }

    public static Function<Element, Stream<TypeElement>> findPublicTypes() {
        return el -> Stream.of(el)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC) && el.getKind().isDeclaredType())
                .map(TypeElement.class::cast);
    }
}
