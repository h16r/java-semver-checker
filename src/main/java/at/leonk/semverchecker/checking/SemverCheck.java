package at.leonk.semverchecker.checking;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.stream.Stream;

public interface SemverCheck {
    Stream<Violation> check(Collection<Element> baselineElements, Collection<Element> currentElements);
}
