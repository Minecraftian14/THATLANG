package in.mcxiv.thatlang;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.ThatVM;
import in.mcxiv.tryCatchSuite.DangerousRunnable;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

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

    @Test
    void testStreamsBasedIOOps() {
        String[] args = {};

        AbstractVM vm = new ThatVM();

        var closables = new LinkedList<DangerousRunnable>
                (List.of(() -> System.out.println("Streams have been closed.")));

        Arrays.stream(args)
                .map(File::new)
                .filter(File::isFile)
                .filter(file -> file.getName().startsWith("that."))
                .map(file -> Try.getAnd(() -> new FileInputStream(file)).elseNull())
                .filter(Objects::nonNull)
                .peek(fis -> closables.addFirst(fis::close))
                .map(fis -> Try.getAnd(fis::readAllBytes).elseNull())
                .filter(Objects::nonNull)
                .map(String::new)
                .map(ProgramFileToken.ProgramFileParser.programFile::parse)
                .forEach(vm::load);
        closables.forEach(Try::run);

        vm.run();
    }
}