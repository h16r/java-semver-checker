package at.leonk;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        ExposedDeser exposed = toDeser(new UniversalScanner().collect(environment.getSpecifiedElements()));


        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("specV2"))) {

            System.out.println("exposed = " + exposed);

            out.writeObject(exposed);
            out.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static ExposedDeser toDeser(Exposed exposed) {
        return new ExposedDeser(exposed.absolutePath(), exposed.name(), exposed.type(), exposed.children().stream().map(UniversalDoclet::toDeser).collect(Collectors.toList()));
    }
}
