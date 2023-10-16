import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner9;
import javax.lang.model.util.Types;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main implements Doclet {

    private Reporter reporter;

    private Types typeUtils;

    @Override
    public void init(Locale locale, Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    public boolean run(DocletEnvironment environment) {

        typeUtils = environment.getTypeUtils();

        ShowElements se = null;
        try {
            se = new ShowElements(new PrintStream(new FileOutputStream("response.txt")));
            se.show(environment.getSpecifiedElements());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    class I {

        String name;

    }

    class ShowElements extends ElementScanner9<Void, Integer> {

        final PrintStream out;

        final List<Void> publicApi;

        ShowElements(PrintStream out) {
            this.out = out;
            this.publicApi = new ArrayList<>();
        }

        void show(Set<? extends Element> elements) {
            scan(elements, 0);
        }
        @Override
        public Void scan(Element e, Integer depth) {
            return super.scan(e, depth + 1);
        }

        @Override
        public Void visitExecutable(ExecutableElement ee, Integer depth) {
            String indent = "  ".repeat(depth);
            out.println(indent + ee.getKind() + ": " + ee);
            if (!ee.getTypeParameters().isEmpty()) {
                out.println(indent + "[Type Parameters]");
                scan(ee.getTypeParameters(), depth);
            }
            if (ee.getKind() == ElementKind.METHOD) {
                show("Return type", ee.getReturnType(), depth);
            }
            if (!ee.getParameters().isEmpty()) {
                out.println(indent + "[Parameters]");
                scan(ee.getParameters(), depth);
            }
            show("Throws", ee.getThrownTypes(), depth);

            return super.visitExecutable(ee, depth);
        }

        @Override
        public Void visitVariable(VariableElement ve, Integer depth) {
            if (ve.getKind() == ElementKind.PARAMETER) {
                String indent = "  ".repeat(depth);
                out.println(indent + ve.getKind() + ": " + ve);
                show("Type", ve.asType(), depth);
            }
            return super.visitVariable(ve, depth);
        }

        private void show(String label, List<? extends TypeMirror> list,
                          int depth) {
            if (!list.isEmpty()) {
                String indent = "  ".repeat(depth);
                out.println(indent + "[" + label + "]");
                int i = 0;
                for (TypeMirror tm : list) {
                    show("#" + (i++), tm, depth + 1);
                }
            }
        }

        private void show(String label, TypeMirror tm, int depth) {
            String indent = "  ".repeat(depth);
            out.println(indent + "[" + label + "]");
            out.println(indent + "  TypeMirror: " + tm);
            out.println(indent + "  as Element: " + typeUtils.asElement(tm));
        }
    }
}
