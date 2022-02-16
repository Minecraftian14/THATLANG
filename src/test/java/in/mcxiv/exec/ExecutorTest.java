package in.mcxiv.exec;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.CentralRepository;
import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import java.io.File;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExecutorTest {

    @Test
    void testThatTheThingBuildsWithoutError_____LOL() {
//        TestSuite.setLevel(LogLevel.DEBUG);
        assertDoesNotThrow(() -> Class.forName(Executor.class.getName()));
    }

    @Test
    void testAllThoseMethods___sob() {
        TestSuite.setLevel(LogLevel.DEBUG);
        assertDoesNotThrow(() -> alsoPrtln(Executor.runCLCommand("cmd", "/C dir", new File(CentralRepository.USER_HOME))));
        assertDoesNotThrow(() -> alsoPrtln(Executor.get("20 * 78 / 6").get()));
        assertDoesNotThrow(() -> alsoPrtln(Executor.get("Math.sin(90)").get()));
    }
}