package in.mcxiv.thatlang;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberCallTokenTest {
    @Test
    void test() {
        MemberCallToken.MemberCallParser parser = new MemberCallToken.MemberCallParser();
        assertNotNull(TestSuite.pj(parser.parse("box . x.value .set")));
        assertNull(TestSuite.pj(parser.parse("box")));
    }
}