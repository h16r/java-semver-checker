package at.leonk;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner14;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UniversalScanner extends ElementScanner14<Exposed, Exposed> {

    UniversalScanner() {
        super(new Exposed());
    }

    @Override
    public Exposed visitType(TypeElement e, Exposed parent) {

        System.out.println("visitType");

        e.getModifiers().forEach(System.out::println);
        e.getEnclosedElements().forEach(System.out::println);
        System.out.println("e.getInterfaces() = " + e.getInterfaces());
        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        return super.visitType(e, new Exposed(parent, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitRecordComponent(RecordComponentElement e, Exposed exposed) {
        System.out.println("visitRecordComponent");

        e.getModifiers().forEach(System.out::println);
        e.getEnclosedElements().forEach(System.out::println);
        System.out.println("e.getSimpleName() = " + e.getSimpleName());

        return super.visitRecordComponent(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitModule(ModuleElement e, Exposed exposed) {
        System.out.println("visitModule");

        e.getModifiers().forEach(System.out::println);
        e.getEnclosedElements().forEach(System.out::println);

        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        return super.visitModule(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitVariable(VariableElement e, Exposed exposed) {
        System.out.println("visitVariable");

        e.getModifiers().forEach(System.out::println);
        e.getEnclosedElements().forEach(System.out::println);
        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        return super.visitVariable(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitPackage(PackageElement e, Exposed exposed) {
        System.out.println("visitPackage");

        e.getModifiers().forEach(System.out::println);
        e.getEnclosedElements().forEach(System.out::println);
        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        return super.visitPackage(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitTypeParameter(TypeParameterElement e, Exposed exposed) {
        System.out.println("visitTypeParameter");

        e.getModifiers().forEach(System.out::println);
        e.getEnclosedElements().forEach(System.out::println);
        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        return super.visitTypeParameter(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitUnknown(Element e, Exposed exposed) {

        System.out.println("visitUnknown v1");
        //e.getModifiers().forEach(System.out::println);
        //e.getEnclosedElements().forEach(System.out::println);
        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        System.out.println("e.getEnclosingElement() = " + e.getEnclosingElement());
        System.out.println("e.asType() = " + e.asType());

        Exposed child = new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name());

        return super.visitUnknown(e, child);
    }

    @Override
    public Exposed visitExecutable(ExecutableElement e, Exposed parent) {


        Exposed child = new Exposed(parent, e.getSimpleName().toString(), e.getKind().name());
        List<Exposed> parameters = e.getParameters().stream().map(parameter -> visitVariable(parameter, child)).collect(Collectors.toList());

        System.out.println("e.getSimpleName() = " + e.getSimpleName());
        System.out.println("e.getParameters() = " + e.getParameters());

        System.out.println("child = " + child);

        return super.visitExecutable(e, child);
    }

    public Exposed collect(Set<? extends Element> elements) {
        Exposed exposed = new Exposed();
        super.scan(elements, exposed);

        return exposed;
    }
}
