package at.leonk.semverchecker.parsing;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

public record PublicApi(
        Set<Element> oldElements,
        Set<Element> newElements
) {
    @Override
    public String toString() {
        return """
                Old elements: %s
                New elements: %s
                """
                .formatted(
                        oldElements.stream().map(Object::toString).collect(Collectors.joining()),
                        newElements.stream().map(Object::toString).collect(Collectors.joining())
                );
    }
}
