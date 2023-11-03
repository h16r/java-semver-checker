package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.Diff;
import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.TypePredicate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Stream;

public class ClassMissingCheck implements SemverCheck {
    @Override
    public Stream<Diff> check(Set<Element> baselineElements, Set<Element> currentElements) {
        return publicClasses(baselineElements)
                .filter(baselineElement -> publicClasses(currentElements).noneMatch(currentElement -> currentElement.getQualifiedName().equals(baselineElement.getQualifiedName())))
                .map(missingElement -> Diff.Removed(missingElement.getQualifiedName().toString(), "class", missingElement.getQualifiedName().toString()));
    }

    private static Stream<TypeElement> publicClasses(Set<Element> elements) {
        return elements.stream()
                .filter(TypePredicate.PUBLIC_CLASS)
                .map(element -> (TypeElement) element);
    }
}
