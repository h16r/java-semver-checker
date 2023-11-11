import at.haesslerkirschner.semverchecker.checking.Checker;
import at.haesslerkirschner.semverchecker.checking.Report;
import at.haesslerkirschner.semverchecker.source.FileSource;
import at.haesslerkirschner.semverchecker.source.FileSourcesHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;


@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class CheckerMojo extends AbstractMojo {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerMojo.class);

    @Parameter(property = "baseline.path", required = true)
    String baselinePath;

    @Parameter(property = "baseline.commit")
    String baselineCommit;

    @Parameter(property = "current.path")
    String currentPath;

    @Parameter(property = "current.commit")
    String currentCommit;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        //FIXME: add logging & tests to classes

        String currentPath = Optional.ofNullable(this.currentPath).orElseGet(() -> {
            LOGGER.warn("current.path not provided, using baseline.path; configure current.commit or current.branch for a different file resolver strategy");
            return baselinePath;
        });

        FileSource baseline = new FileSource(baselinePath, Optional.empty(), Optional.ofNullable(baselineCommit));
        FileSource current = new FileSource(currentPath, Optional.empty(), Optional.ofNullable(currentCommit));

        if (baseline.equals(current)) {
            LOGGER.warn("File sources for baseline and current ");
        }

        FileSourcesHandler filesHandler = new FileSourcesHandler(baseline, current);

        try {

            Path resolvedBaseline = filesHandler.resolveBaseline();
            Path resolvedCurrent = filesHandler.resolveCurrent();

            LOGGER.info("Checking: ");
            LOGGER.info(" - " + resolvedBaseline.toString());
            LOGGER.info(" - " + resolvedCurrent.toString());

            Report report = Checker.check(resolvedBaseline, resolvedCurrent);

            if (!report.breaking()) {
                return;
            }

            int violations = report.differences().size();

            LOGGER.error("------------------------------------------------------------------------");
            LOGGER.error("");

            LOGGER.error("Found %d Rule violation%s".formatted(violations, violations > 1 ? "s" : ""));

            report.differences().forEach(violation -> {
                LOGGER.error("");
                LOGGER.error("Rule %s (%s); %d violations".formatted(violation.code(), violation.description(), violation.locations().size()));
                violation.locations().stream().map(l -> "- $." + l.fullyQualifiedName()).forEach(LOGGER::error);
            });

            LOGGER.error("");

            throw new MojoFailureException("%d Breaking change%s detected. See previous Logs".formatted(violations, violations > 1 ? "s" : ""));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}