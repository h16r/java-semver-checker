package at.leonk.semverchecker.checking;

import javax.lang.model.element.Element;

public record MatchingElements<T extends Element>(
        T baseline,
        T current
) {
}
