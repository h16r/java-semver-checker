package at.haesslerkirschner.semverchecker.checking;

import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;

public record ViolatingLocation(String fullyQualifiedName) {
    public static ViolatingLocation fromElement(Element element) {
        return new ViolatingLocation(switch (element) {
            case QualifiedNameable nameable -> nameable.getQualifiedName().toString();
            case Element el when el.getEnclosingElement() instanceof QualifiedNameable parent ->
                    parent.getQualifiedName().toString().concat(".%s".formatted(element.getSimpleName().toString()));
            default ->
                    throw new IllegalArgumentException("Unhandled edge case! Tried to fully qualify element: " + element);
        });
    }
}
