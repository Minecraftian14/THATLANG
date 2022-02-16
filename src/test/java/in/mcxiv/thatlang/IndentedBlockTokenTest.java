package in.mcxiv.thatlang;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.IndentedBlockToken.IndentedBlockParser;
import org.junit.jupiter.api.Test;

class IndentedBlockTokenTest {
    @Test
    void test() {

        IndentedBlockParser parser = new IndentedBlockParser();
        TestSuite.pj(parser.parse("\n    aStatement.aCall"));

    }
}