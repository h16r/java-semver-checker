package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.Predicates;
import at.leonk.semverchecker.checking.Queries;
import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.Violation;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;
import java.util.stream.Stream;

public class EnumValueAddedCheck implements SemverCheck {

    @Override
    public Stream<Violation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return baselineElements.stream()
                .flatMap(Queries.findTypeElementsOfKind(ElementKind.ENUM))
                .flatMap(Queries.matchElementsByQualifiedNameWith(currentElements))
                .flatMap(els -> variablesIn(els.current()).filter(
                        varInCurrent -> variablesIn(els.baseline()).noneMatch(Predicates.matchesSimpleNameOf(varInCurrent))))
                .map(missingEnumValue -> Violation.Removed(missingEnumValue.getSimpleName().toString(), "asdf", "asdf"));
    }

    private static Stream<VariableElement> variablesIn(TypeElement element) {
        return element.getEnclosedElements().stream()
                .filter(VariableElement.class::isInstance)
                .map(VariableElement.class::cast);
    }

}
