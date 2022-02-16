package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.parser.tree.Node;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExpressionsTokenTest {
    @Test
    void test() {
        ExpressionsToken.ExpressionsParser parser = ExpressionsToken.ExpressionsParser.instance;

        assertNotNull(TestSuite.pj(parser.parse("oneWord.hello")));
        assertNotNull(TestSuite.pj(parser.parse("oneWord.hello()")));
        assertNotNull(TestSuite.pj(parser.parse("oneWord")));
        assertNotNull(TestSuite.pj(parser.parse("oneWord+anotherWord")));
    }

    @Test
    void testSomethingMoreComplex() {
        ExpressionsToken.ExpressionsParser parser = ExpressionsToken.ExpressionsParser.instance;
        Node node;

        assertNotNull(TestSuite.pj(node = parser.parse("1+2*4-4")));
        //                         converts things to (1+(2*4))-4
        assertEquals(2, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("(1+2)*4-4")));
        assertEquals(2, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("( 1 + 2   )  *    4   -  4")));
        assertEquals(2, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("(1+2)")));
        assertEquals(0, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("(1)")));
        assertEquals(0, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("((1))")));
        assertEquals(0, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("1-(4+3)")));
        assertEquals(1, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));

        assertNotNull(TestSuite.pj(node = parser.parse("((95-89*463)*768-(9*3)+2)*(4-1)-4")));
        assertEquals(8, alsoPrtln("wildSearch", node.wildSearch(BinaryOperatorToken.class)));
    }
}