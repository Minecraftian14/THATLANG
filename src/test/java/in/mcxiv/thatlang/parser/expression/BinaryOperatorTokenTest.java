package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.expression.BinaryOperatorToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinaryOperatorTokenTest {
    @Test
    void test() {
        BinaryOperatorToken.BinaryOperatorParser parser = new BinaryOperatorToken.BinaryOperatorParser("+");
        assertNotNull(TestSuite.pj(parser.parse("uo+78")));
    }
}