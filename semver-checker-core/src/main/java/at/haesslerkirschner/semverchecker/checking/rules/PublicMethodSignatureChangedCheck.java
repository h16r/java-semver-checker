package at.haesslerkirschner.semverchecker.checking.rules;

import at.haesslerkirschner.semverchecker.checking.SemverCheck;
import at.haesslerkirschner.semverchecker.checking.ViolatingLocation;
import at.haesslerkirschner.semverchecker.checking.query.Predicates;
import at.haesslerkirschner.semverchecker.checking.query.Queries;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PublicMethodSignatureChangedCheck implements SemverCheck {
    @Override
    public String description() {
        return "public method signature changed";
    }

    @Override
    public String docUrl() {
        return null; // TODO
    }

    @Override
    public Stream<ViolatingLocation> check(Collection<Element> baselineElements, Collection<Element> currentElements) {
        return baselineElements.stream()
                .flatMap(Queries.findPublicTypes())
                .flatMap(Queries.matchElementsByQualifiedNameWith(currentElements))
                .flatMap(matchingElements -> publicMethodsOf(matchingElements.baseline())
                        .filter(baselineMethod -> publicMethodsOf(matchingElements.current())
                                .filter(Predicates.matchesSimpleNameOf(baselineMethod))
                                .findFirst()
                                .stream().anyMatch(currentMethod -> methodSignaturesDiffer(baselineMethod, currentMethod)))
                        .map(ViolatingLocation::fromElement));
    }

    private static boolean methodSignaturesDiffer(ExecutableElement baselineMethod, ExecutableElement currentMethod) {
        return !Signature.fromExecutableElement(baselineMethod).equals(Signature.fromExecutableElement(currentMethod));
    }

    private static Stream<ExecutableElement> publicMethodsOf(TypeElement element) {
        return element.getEnclosedElements().stream()
                .filter(el -> el.getModifiers().contains(Modifier.PUBLIC))
                .filter(ExecutableElement.class::isInstance)
                .map(ExecutableElement.class::cast);
    }

    private record Signature(
            TypeMirror returnType,
            List<TypeMirror> parameterTypes
    ) {
        static Signature fromExecutableElement(ExecutableElement element) {
            return new Signature(element.getReturnType(), element.getParameters().stream().map(VariableElement::asType).toList());
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Signature other)) return false;
            if (parameterTypes.size() != other.parameterTypes.size()) return false;
            return returnType.toString().equals(other.returnType.toString()) && IntStream.range(0, parameterTypes.size())
                    .allMatch(i -> parameterTypes.get(i).toString().equals(other.parameterTypes.get(i).toString()));
        }
    }
}

