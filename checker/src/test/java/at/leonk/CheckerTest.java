package at.leonk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    @Test
    public void breakingChangesDetected() {

        Report report = Main.check("src/test/resources/specV3", "src/test/resources/specV2");
        assertTrue(report.breaking());
        report.differences().forEach(System.out::println);

    }

}