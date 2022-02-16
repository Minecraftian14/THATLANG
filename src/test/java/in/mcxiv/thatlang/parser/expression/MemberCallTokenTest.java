package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberCallTokenTest {
    @Test
    void test() {
        MemberCallToken.MemberCallParser parser = MemberCallToken.MemberCallParser.instance;
        assertNotNull(TestSuite.pj(parser.parse("mamam")));
    }
}