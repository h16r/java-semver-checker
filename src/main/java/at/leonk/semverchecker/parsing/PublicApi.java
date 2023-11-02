package at.leonk.semverchecker.parsing;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

public record PublicApi(
        Set<Element> baselineElements,
        Set<Element> curretElements
) {
    @Override
    public String toString() {
        return """
                Old elements: %s
                New elements: %s
                """
                .formatted(
                        baselineElements.stream().map(Object::toString).collect(Collectors.joining()),
                        curretElements.stream().map(Object::toString).collect(Collectors.joining())
                );
    }
}
