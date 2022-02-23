package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionCallTokenTest {
    @Test
    void test() {
        FunctionCallToken.FunctionCallParser parser = FunctionCallToken.FunctionCallParser.function;
        assertNotNull(TestSuite.pj(parser.parse("mamam()")));
        assertNotNull(TestSuite.pj(parser.parse("mamam ()")));
        assertNotNull(TestSuite.pj(parser.parse("mamam (fyadyf)")));
        assertNotNull(TestSuite.pj(parser.parse("mamam (fyadyf, wdwf)")));
        assertNotNull(TestSuite.pj(parser.parse("mamam (fyadyf,wdwf)")));
        assertNotNull(TestSuite.pj(parser.parse("mamam (fyadyf,wdwf, wdw)")));
        assertNotNull(TestSuite.pj(parser.parse("mamam ( fyadyf,wdwf, wdw )")));
        assertNotNull(TestSuite.pj(parser.parse("mamam ( fyadyf   ,  wdwf ,    wdw )")));
    }
}