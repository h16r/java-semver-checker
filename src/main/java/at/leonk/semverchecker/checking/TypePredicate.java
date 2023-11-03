package at.leonk.semverchecker.checking;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.util.function.Predicate;

public enum TypePredicate implements Predicate<Element> {
    PUBLIC_CLASS,
    PUBLIC_RECORD;

    @Override
    public boolean test(Element element) {

        return switch (this) {

            case PUBLIC_CLASS ->
                    element.getModifiers().contains(Modifier.PUBLIC) && element.getKind().equals(ElementKind.CLASS);
            case PUBLIC_RECORD ->
                    element.getModifiers().contains(Modifier.PUBLIC) && element.getKind().equals(ElementKind.RECORD);
        };
    }
}
