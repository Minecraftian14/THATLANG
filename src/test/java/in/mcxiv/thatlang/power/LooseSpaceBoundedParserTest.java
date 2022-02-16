package in.mcxiv.thatlang.power;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.*;

class LooseSpaceBoundedParserTest {
    @Test
    void test() {

        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString(","))));
        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString(" ,"))));
        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString(", "))));
        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString(" , "))));

        assertNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString("="))));
        assertNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString(" ="))));
        assertNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString("= "))));
        assertNull(alsoPrtln(new LooseSpaceBoundedParser(",").parse(new ParsableString(" = "))));

        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser("book").parse(new ParsableString("book"))));
        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser("book").parse(new ParsableString(" book"))));
        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser("book").parse(new ParsableString("book "))));
        assertNotNull(alsoPrtln(new LooseSpaceBoundedParser("book").parse(new ParsableString(" book "))));

        assertNull(alsoPrtln(new LooseSpaceBoundedParser("melo").parse(new ParsableString("nelo"))));
        assertNull(alsoPrtln(new LooseSpaceBoundedParser("melo").parse(new ParsableString(" nelo"))));
        assertNull(alsoPrtln(new LooseSpaceBoundedParser("melo").parse(new ParsableString("nelo "))));
        assertNull(alsoPrtln(new LooseSpaceBoundedParser("melo").parse(new ParsableString(" nelo "))));
    }
}