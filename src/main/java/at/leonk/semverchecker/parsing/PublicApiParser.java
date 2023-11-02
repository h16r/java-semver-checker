package at.leonk.semverchecker.parsing;

import javax.lang.model.element.Element;
import javax.tools.DocumentationTool;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

public class PublicApiParser {
    private static final DocumentationTool documentationTool = ToolProvider.getSystemDocumentationTool();

    public static PublicApi parse(Path oldApiPath, Path newApiPath) throws IOException {
        var oldCompilationUnits = getCompilationUnits(oldApiPath);
        var oldElements = new HashSet<Element>();
        OldApiParserDoclet.oldApiConsumer = oldElements::addAll;
        parseApi(OldApiParserDoclet.class, oldCompilationUnits);

        var newCompilationUnits = getCompilationUnits(newApiPath);
        var newElements = new HashSet<Element>();
        NewApiParserDoclet.newApiConsumer = newElements::addAll;
        parseApi(NewApiParserDoclet.class, newCompilationUnits);

        return new PublicApi(oldElements, newElements);
    }

    private static void parseApi(Class<? extends ApiParserDoclet> docletClass, Iterable<? extends JavaFileObject> compilationUnits) {
        var docTask = documentationTool.getTask(
                null,
                null,
                null,
                docletClass,
                null,
                compilationUnits);
        docTask.call();
    }

    private static Iterable<? extends JavaFileObject> getCompilationUnits(Path projectPath) throws IOException {
        var fileManager = documentationTool.getStandardFileManager(null, null, null);
        return fileManager.getJavaFileObjects(Files.find(projectPath, Integer.MAX_VALUE, PublicApiParser::isJavaFile, FileVisitOption.FOLLOW_LINKS).toArray(Path[]::new));
    }

    private static boolean isJavaFile(Path path, BasicFileAttributes basicFileAttributes) {
        return path.getFileName().toString().endsWith(".java");
    }
}
