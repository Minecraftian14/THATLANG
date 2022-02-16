package in.mcxiv.thatlang.power;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.SpacesToken;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EitherParserTest {

    @Test
    void test() {
        EitherParser either = new EitherParser(new NameToken.NameParser(), new SpacesToken.SpacesParser());
        assertNotNull(alsoPrtln(either.parse(new ParsableString("4689"))));
        assertNotNull(alsoPrtln(either.parse(new ParsableString("mold"))));
        assertNull(alsoPrtln(either.parse(new ParsableString("-8c8f"))));
        assertNull(alsoPrtln(either.parse(new ParsableString("{8c8f"))));
        assertNotNull(alsoPrtln(either.parse(new ParsableString("   "))));
        assertNotNull(alsoPrtln(either.parse(new ParsableString("\t\t"))));
    }
}