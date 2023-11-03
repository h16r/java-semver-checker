package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.TypePredicate;
import at.leonk.semverchecker.checking.Violation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.stream.Stream;

public class ClassMissingCheck implements SemverCheck {
    @Override
    public Stream<Violation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return publicClasses(baselineElements)
                .filter(baselineElement -> publicClasses(currentElements).noneMatch(currentElement -> currentElement.getQualifiedName().contentEquals(baselineElement.getQualifiedName())))
                .map(missingElement -> Violation.Removed(missingElement.getQualifiedName().toString(), "class", missingElement.getQualifiedName().toString()));
    }

    private static Stream<TypeElement> publicClasses(Collection<Element> elements) {
        return elements.stream()
                .filter(TypePredicate.PUBLIC_CLASS)
                .map(element -> (TypeElement) element);
    }
}
