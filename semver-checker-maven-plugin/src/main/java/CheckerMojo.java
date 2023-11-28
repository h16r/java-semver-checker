import at.haesslerkirschner.semverchecker.checking.Checker;
import at.haesslerkirschner.semverchecker.checking.Report;
import at.haesslerkirschner.semverchecker.source.FileSource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;


@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckerMojo extends AbstractMojo {

    @Parameter(property = "baseline.path")
    String baselinePath;

    @Parameter(property = "baseline.commit")
    String baselineCommit;

    @Parameter(property = "baseline.branch")
    String baselineBranch;

    @Parameter(property = "current.path")
    String currentPath;

    @Parameter(property = "current.commit")
    String currentCommit;

    @Parameter(property = "current.branch")
    String currentBranch;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        String baselinePath = Optional.ofNullable(this.baselinePath).orElseGet(() -> {
            getLog().warn("basline.path not provided, checking in current directory");
            return ".";
        });

        String currentPath = Optional.ofNullable(this.currentPath).orElseGet(() -> {
            getLog().warn("current.path not provided, using baseline.path; configure current.commit or current.branch for a different file resolver strategy");
            return baselinePath;
        });

        FileSource baseline = new FileSource(Paths.get(baselinePath), baselineCommit, baselineBranch);
        FileSource current = new FileSource(Paths.get(currentPath), currentCommit, currentBranch);

        if (baseline.equals(current)) {
            getLog().warn("File sources for baseline and current ");
        }

        try {

            getLog().info("Checking semver violations: ");
            getLog().info(" baseline (old): " + baseline);
            getLog().info(" against ");
            getLog().info(" current (new): " + current);

            Report report = Checker.check(baseline, current);

            if (!report.breaking()) {
                return;
            }

            int violations = report.differences().size();

            getLog().error("------------------------------------------------------------------------");
            getLog().error("");

            getLog().error("Found %d Rule violation%s".formatted(violations, violations > 1 ? "s" : ""));

            report.differences().forEach(violation -> {
                getLog().error("");
                getLog().error("Rule %s (%s); %d violations".formatted(violation.code(), violation.description(), violation.locations().size()));
                violation.locations().stream().map(l -> "- " + l.fullyQualifiedName()).forEach(getLog()::error);
            });

            getLog().error("");

            throw new MojoFailureException("%d Breaking change%s detected. See previous Logs".formatted(violations, violations > 1 ? "s" : ""));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}