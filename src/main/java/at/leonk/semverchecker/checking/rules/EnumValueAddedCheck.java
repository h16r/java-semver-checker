package at.leonk.semverchecker.checking.rules;

import at.leonk.semverchecker.checking.Constants;
import at.leonk.semverchecker.checking.SemverCheck;
import at.leonk.semverchecker.checking.ViolatingLocation;
import at.leonk.semverchecker.checking.query.Predicates;
import at.leonk.semverchecker.checking.query.Queries;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;
import java.util.stream.Stream;

public class EnumValueAddedCheck implements SemverCheck {

    @Override
    public String description() {
        return "enum value has been added";
    }

    @Override
    public String docUrl() {
        return Constants.REPO_URL + "#major-adding-new-enum-values";
    }

    @Override
    public Stream<ViolatingLocation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return baselineElements.stream()
                .flatMap(Queries.findPublicTypesOfKind(ElementKind.ENUM))
                .flatMap(Queries.matchElementsByQualifiedNameWith(currentElements))
                .flatMap(els -> variablesIn(els.current()).filter(
                        varInCurrent -> variablesIn(els.baseline()).noneMatch(Predicates.matchesSimpleNameOf(varInCurrent))))
                .map(ViolatingLocation::fromElement);
    }

    private static Stream<VariableElement> variablesIn(TypeElement element) {
        return element.getEnclosedElements().stream()
                .filter(VariableElement.class::isInstance)
                .map(VariableElement.class::cast);
    }
}
