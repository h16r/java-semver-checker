package at.leonk;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;

public class UniversalDoclet implements Doclet {
    @Override
    public void init(Locale locale, Reporter reporter) {

    }

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

    @Override
    public boolean run(DocletEnvironment environment) {

        // new PrintStream(new FileOutputStream("response.txt"))

        Exposed exposed = new UniversalScanner().collect(environment.getSpecifiedElements());
        try (PrintWriter target = new PrintWriter("api.json", StandardCharsets.UTF_8)) {
            target.println(exposed);
            target.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
