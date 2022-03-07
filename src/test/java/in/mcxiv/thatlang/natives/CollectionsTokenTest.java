package in.mcxiv.thatlang.natives;

import in.mcxiv.TestSuite;
import in.mcxiv.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsTokenTest {
    @Test
    void test() {
        Parser<?> parser;

        parser = CollectionsToken.CollectionType.ARRAY_LIST.parser;
        assertNotNull(TestSuite.pj(parser.parse("[1, 2, 3]")));

        parser = CollectionsToken.CollectionType.LINKED_LIST.parser;
        assertNotNull(TestSuite.pj(parser.parse("l[1, 2, 3]")));

        parser = CollectionsToken.CollectionType.HASH_SET.parser;
        assertNotNull(TestSuite.pj(parser.parse("{1, 2, 3}")));

        parser = CollectionsToken.CollectionType.HASH_MAP.parser;
        assertNotNull(TestSuite.pj(parser.parse("{1:0, 2:1, 3:2}")));
    }
}