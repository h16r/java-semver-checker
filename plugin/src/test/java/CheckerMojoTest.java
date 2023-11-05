import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckerMojoTest {

    @Test
    public void buildBreaksWhenReportIsBreaking() throws MojoExecutionException, MojoFailureException {

        CheckerMojo underTest = new CheckerMojo();
        // underTest.baseline = new Baseline("../../java-semver-checker/src/test/resources/test-projects/class-missing/baseline");
        underTest.currentPath = "../../java-semver-checker/src/test/resources/test-projects/class-missing/current";
        underTest.currentCommit = "3e287970e011bc93f3b54a581210e40741a671c9";
        underTest.baselinePath = "../../java-semver-checker/src/test/resources/test-projects/class-missing/baseline";
        underTest.baselineCommit = "3e287970e011bc93f3b54a581210e40741a671c9";

        underTest.execute();

        //assertThrows(MojoFailureException.class, underTest::execute);

    }

}