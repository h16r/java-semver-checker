package at.leonk.semverchecker.checking;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Stream;

public class ClassMissingCheck implements SemverCheck {
    @Override
    public Stream<Diff> check(Set<Element> oldElements, Set<Element> newElements) {
        return publicClasses(oldElements)
                .filter(oldElement -> publicClasses(newElements).noneMatch(newElement -> newElement.getQualifiedName().equals(oldElement.getQualifiedName())))
                .map(missingElement -> Diff.Removed(missingElement.getQualifiedName().toString(), "class", missingElement.getQualifiedName().toString()));
    }

    private static Stream<TypeElement> publicClasses(Set<Element> elements) {
        return elements.stream()
                .filter(element -> element.getModifiers().contains(Modifier.PUBLIC) && element.getKind().equals(ElementKind.CLASS))
                .map(element -> (TypeElement) element);
    }
}
