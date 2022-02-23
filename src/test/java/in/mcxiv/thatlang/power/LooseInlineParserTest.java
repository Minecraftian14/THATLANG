package in.mcxiv.thatlang.power;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.power.LooseInlineParser;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.*;

class LooseInlineParserTest {
    @Test
    void test() {

        assertNotNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString(","))));
        assertNotNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString(" ,"))));
        assertNotNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString(", "))));
        assertNotNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString(" , "))));

        assertNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString("="))));
        assertNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString(" ="))));
        assertNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString("= "))));
        assertNull(alsoPrtln(new LooseInlineParser(",").parse(new ParsableString(" = "))));

        assertNotNull(alsoPrtln(new LooseInlineParser("book").parse(new ParsableString("book"))));
        assertNotNull(alsoPrtln(new LooseInlineParser("book").parse(new ParsableString(" book"))));
        assertNotNull(alsoPrtln(new LooseInlineParser("book").parse(new ParsableString("book "))));
        assertNotNull(alsoPrtln(new LooseInlineParser("book").parse(new ParsableString(" book "))));

        assertNull(alsoPrtln(new LooseInlineParser("melo").parse(new ParsableString("nelo"))));
        assertNull(alsoPrtln(new LooseInlineParser("melo").parse(new ParsableString(" nelo"))));
        assertNull(alsoPrtln(new LooseInlineParser("melo").parse(new ParsableString("nelo "))));
        assertNull(alsoPrtln(new LooseInlineParser("melo").parse(new ParsableString(" nelo "))));
    }
}