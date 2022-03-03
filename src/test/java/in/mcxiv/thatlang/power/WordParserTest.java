package in.mcxiv.thatlang.power;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.power.WordParser;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.*;

class WordParserTest {
    @Test
    void test() {
        assertNotNull(alsoPrtln(new WordParser(",").parse(new ParsableString(","))));
        assertNull(alsoPrtln(new WordParser(",").parse(new ParsableString("="))));
        assertNotNull(alsoPrtln(new WordParser("book").parse(new ParsableString("book"))));
        assertNull(alsoPrtln(new WordParser("melo").parse(new ParsableString("nelo"))));
    }
}