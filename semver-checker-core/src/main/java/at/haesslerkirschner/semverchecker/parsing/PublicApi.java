package at.haesslerkirschner.semverchecker.parsing;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.stream.Collectors;

public record PublicApi(
        Collection<Element> baselineElements,
        Collection<Element> currentElements
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
