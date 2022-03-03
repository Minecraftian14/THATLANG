package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.expression.MemberCallToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberCallTokenTest {
    @Test
    void test() {
        MemberCallToken.MemberCallParser parser = MemberCallToken.MemberCallParser.member;
        assertNotNull(TestSuite.pj(parser.parse("mamam")));
    }
}