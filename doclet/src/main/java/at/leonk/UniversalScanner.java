package at.leonk;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner14;
import java.util.Set;

public class UniversalScanner extends ElementScanner14<Exposed, Exposed> {

    UniversalScanner() {
        super(new Exposed());
    }

    @Override
    public Exposed visitType(TypeElement e, Exposed parent) {

        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        parent.addChild(child);

        return super.visitType(e, child);
    }

    @Override
    public Exposed visitRecordComponent(RecordComponentElement e, Exposed exposed) {
        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        exposed.addChild(child);

        return super.visitRecordComponent(e, exposed);
    }

    @Override
    public Exposed visitModule(ModuleElement e, Exposed exposed) {

        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        exposed.addChild(child);

        return super.visitModule(e, exposed);
    }

    @Override
    public Exposed visitVariable(VariableElement e, Exposed exposed) {

        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        exposed.addChild(child);

        return super.visitVariable(e, exposed);
    }

    @Override
    public Exposed visitPackage(PackageElement e, Exposed exposed) {
        System.out.println("e = " + e);
        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        exposed.addChild(child);

        return super.visitPackage(e, exposed);
    }

    @Override
    public Exposed visitTypeParameter(TypeParameterElement e, Exposed exposed) {
        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        exposed.addChild(child);

        return super.visitTypeParameter(e, exposed);
    }

    @Override
    public Exposed visitUnknown(Element e, Exposed exposed) {
        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        exposed.addChild(child);

        return super.visitUnknown(e, exposed);
    }

    @Override
    public Exposed visitExecutable(ExecutableElement e, Exposed parent) {

        Exposed child = new Exposed(e.getSimpleName().toString(), e.getKind().name());
        parent.addChild(child);

        return super.visitExecutable(e, child);
    }

    public Exposed collect(Set<? extends Element> elements) {
        Exposed exposed = new Exposed("root");
        super.scan(elements, exposed);

        return exposed;
    }
}
