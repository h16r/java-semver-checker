package at.leonk.semverchecker.checking.query;

import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;
import java.util.function.Predicate;

public class Predicates {
    public static Predicate<QualifiedNameable> matchesQualifiedNameOf(QualifiedNameable element) {
        return el -> el.getQualifiedName().contentEquals(element.getQualifiedName());
    }

    public static Predicate<Element> matchesSimpleNameOf(Element element) {
        return el -> el.getSimpleName().contentEquals(element.getSimpleName());
    }
}
