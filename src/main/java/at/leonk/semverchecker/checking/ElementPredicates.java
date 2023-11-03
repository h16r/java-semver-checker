package at.leonk.semverchecker.checking;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public final class ElementPredicates {
    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }
}
