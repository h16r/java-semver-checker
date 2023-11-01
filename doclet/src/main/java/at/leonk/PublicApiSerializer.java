package at.leonk;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class PublicApiSerializer {
    public static void main(String[] args) throws IOException {
        var documentationTool = ToolProvider.getSystemDocumentationTool();
        var fileManager = documentationTool.getStandardFileManager(null, null, null);
        var compilationUnits = fileManager.getJavaFileObjects(Files.find(
                Path.of("doclet/src/test/java/v1"),
                Integer.MAX_VALUE,
                (path, basicFileAttributes) -> path.getFileName().toString().endsWith(".java"),
                FileVisitOption.FOLLOW_LINKS).toArray(Path[]::new));

        var docTask = documentationTool.getTask(
                null,
                null,
                null,
                UniversalDoclet.class,
                null,
                compilationUnits);
        docTask.call();
    }
}
