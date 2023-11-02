package at.leonk.semverchecker.checking;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Stream;

public interface SemverCheck {
    Stream<Diff> check(Set<Element> oldElements, Set<Element> newElements);
}
