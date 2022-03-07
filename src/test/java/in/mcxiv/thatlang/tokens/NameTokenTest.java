package in.mcxiv.thatlang.tokens;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.generic.NameToken.NameParser;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.*;

class NameTokenTest {
    @Test
    void test() {
        NameParser parser =  NameParser.name;
        assertNotNull(alsoPrtln(parser.parse(new ParsableString("4689"))));
        assertNotNull(alsoPrtln(parser.parse(new ParsableString("mold"))));
        assertNull(alsoPrtln(parser.parse(new ParsableString(" 8c8f"))));
        assertNull(alsoPrtln(parser.parse(new ParsableString("{8c8f"))));
        assertNotNull(alsoPrtln(parser.parse(new ParsableString("a5f7"))));
        assertNotNull(alsoPrtln(parser.parse(new ParsableString("8c8f"))));
    }
}