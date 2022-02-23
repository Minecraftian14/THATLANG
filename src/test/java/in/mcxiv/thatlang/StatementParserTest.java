package in.mcxiv.thatlang;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.statements.StatementToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatementParserTest {
    @Test
    void test() {
        StatementToken.StatementParser parser = StatementToken.StatementParser.statement;

        Assertions.assertNotNull(TestSuite.pj(parser.parse("print(shit)")));
    }
}