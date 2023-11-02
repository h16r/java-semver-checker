package at.leonk.semverchecker.parsing;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

public record PublicApi(
        Set<Element> baselineElements,
        Set<Element> currentElements
) {
    @Override
    public String toString() {
        return """
                Old elements: %s
                New elements: %s
                """
                .formatted(
                        baselineElements.stream().map(Object::toString).collect(Collectors.joining()),
                        currentElements.stream().map(Object::toString).collect(Collectors.joining())
                );
    }
}
