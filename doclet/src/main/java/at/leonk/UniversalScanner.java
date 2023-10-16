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
        return super.visitType(e, new Exposed(parent, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitRecordComponent(RecordComponentElement e, Exposed exposed) {
        return super.visitRecordComponent(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitModule(ModuleElement e, Exposed exposed) {
        return super.visitModule(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitVariable(VariableElement e, Exposed exposed) {
        return super.visitVariable(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitPackage(PackageElement e, Exposed exposed) {
        return super.visitPackage(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitTypeParameter(TypeParameterElement e, Exposed exposed) {
        return super.visitTypeParameter(e, new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name()));
    }

    @Override
    public Exposed visitUnknown(Element e, Exposed exposed) {

        Exposed child = new Exposed(exposed, e.getSimpleName().toString(), e.getKind().name());

        return super.visitUnknown(e, child);
    }

    @Override
    public Exposed visitExecutable(ExecutableElement e, Exposed parent) {

        Exposed child = new Exposed(parent, e.getSimpleName().toString(), e.getKind().name());

        return super.visitExecutable(e, child);
    }

    public Exposed collect(Set<? extends Element> elements) {
        Exposed exposed = new Exposed();
        super.scan(elements, exposed);

        return exposed;
    }
}
