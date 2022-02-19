package in.mcxiv.thatlang.blocks;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.blocks.IndentedBlockToken.IndentedBlockParser;
import org.junit.jupiter.api.Test;

class IndentedBlockTokenTest {
    @Test
    void test() {

        IndentedBlockParser parser = new IndentedBlockParser();
        TestSuite.pj(parser.parse("\n    aStatement.aCall"));

    }
}