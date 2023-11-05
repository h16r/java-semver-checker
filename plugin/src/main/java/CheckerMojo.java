import at.leonk.semverchecker.checking.Checker;
import at.leonk.semverchecker.checking.Report;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Mojo(name = "semver-checker-mojo", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckerMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() {

        System.out.println("project = " + project.getProperties());
        System.out.println("project.getExecutionProject().getProperties() = " + project.getExecutionProject().getProperties());

        try {
            Report report = Checker.check(Paths.get("/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/baseline"), Paths.get("/Users/leonkirschner/Documents/work/os/java-semver-checker/src/test/resources/test-projects/class-missing/current"));
            System.out.println("report = " + report);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}