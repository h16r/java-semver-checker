package at.haesslerkirschner.semverchecker.parsing;

import javax.lang.model.element.Element;
import javax.tools.DocumentationTool;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PublicApiParser {
    private static final DocumentationTool documentationTool = ToolProvider.getSystemDocumentationTool();

    public static PublicApi parse(Path baselineApiPath, Path currentApiPath) throws IOException {
        var baselineCompilationUnits = getCompilationUnits(baselineApiPath);
        var baselineElements = new ArrayList<Element>();
        OldApiParserDoclet.baselineApiConsumer = baselineElements::addAll;
        parseApi(OldApiParserDoclet.class, baselineCompilationUnits);

        var currentCompilationUnits = getCompilationUnits(currentApiPath);
        var currentElements = new ArrayList<Element>();
        NewApiParserDoclet.currentApiConsumer = currentElements::addAll;
        parseApi(NewApiParserDoclet.class, currentCompilationUnits);

        return new PublicApi(baselineElements, currentElements);
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
