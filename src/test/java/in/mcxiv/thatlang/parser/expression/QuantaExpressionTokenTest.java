package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.expression.QuantaExpressionToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantaExpressionTokenTest {
    @Test
    void test() {
        QuantaExpressionToken.QuantaExpressionParser parser = QuantaExpressionToken.QuantaExpressionParser.quantaExpression;
        assertNotNull(TestSuite.pj(parser.parse("happy.man")));
        assertNotNull(TestSuite.pj(parser.parse("happy(oof).man")));
        assertNotNull(TestSuite.pj(parser.parse("happy.man()")));
    }
}