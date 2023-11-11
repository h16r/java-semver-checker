package at.haesslerkirschner.semverchecker.checking.query;

import javax.lang.model.element.Element;

public record MatchingElements<T extends Element>(
        T baseline,
        T current
) {
}
