package in.mcxiv.thatlang;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.interpreter.AbstractVMTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class THATRuntimeTest {

    static StringBuilder builder;

    @BeforeAll
    static void setUp() {
        LogLevel.DEBUG.activate();
        TestSuite.bindToOutput(builder = new StringBuilder());
    }

    @Test
    void test() throws FileNotFoundException {
//        TestSuite.redefineInput("simple Test");
//        new FileInputStream("that.ExampleProgramFile");
        THATRuntime.main(new String[]{"that.ExampleProgramFile"});
//        AbstractVMTest.assertOutput("simple Test", builder);
    }
}