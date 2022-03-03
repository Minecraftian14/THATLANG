package in.mcxiv.parser;

import in.mcxiv.TestSuite;
import in.mcxiv.parser.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeTest {

    @Test
    void testSimpleConstruction() {
        TETETE a = new TETETE();

        Node b = new Node(a);
        TETETE c = new TETETE(a);

        assertTrue(TestSuite.alsoPrtln(a.children.get(0).getB().isInstance(b)));
        assertTrue(TestSuite.alsoPrtln(a.hasChild(TETETE.class)));

        assertEquals(c, TestSuite.alsoPrtln(a.get(TETETE.class)));

//        TETETE c_exp = a.getExp();
//        assertEquals(c, TestSuite.alsoPrtln(c_exp));
    }

    public static class TETETE extends Node {
        public TETETE() {
        }

        public TETETE(Node parent) {
            super(parent);
        }
    }

}