package in.mcxiv.thatlang.tokens;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.generic.SpacesToken;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.*;

class SpacesTokenTest {
    @Test
    void test() {
        SpacesToken.SpacesParser parser = new SpacesToken.SpacesParser();
        assertNotNull(alsoPrtln(parser.parse(new ParsableString(" "))));
        assertNotNull(alsoPrtln(parser.parse(new ParsableString("\t"))));
        assertNull(alsoPrtln(parser.parse(new ParsableString("8c8f"))));
        assertNull(alsoPrtln(parser.parse(new ParsableString("8c8f"))));
        assertNotNull(alsoPrtln(parser.parse(new ParsableString(" \t \t"))));
        assertNotNull(alsoPrtln(parser.parse(new ParsableString("\t \t "))));
    }
}