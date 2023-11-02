package at.leonk.semverchecker.parsing;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

abstract class ApiParserDoclet implements Doclet {
    protected abstract Consumer<Set<Element>> getElementConsumer();

    @Override
    public boolean run(DocletEnvironment environment) {
        getElementConsumer().accept((Set<Element>) environment.getSpecifiedElements());
        return true;
    }

    @Override
    public void init(Locale locale, Reporter reporter) {}

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return null;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
