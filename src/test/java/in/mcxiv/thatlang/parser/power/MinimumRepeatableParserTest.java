package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.SpacesToken;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.pj;
import static org.junit.jupiter.api.Assertions.*;

class MinimumRepeatableParserTest {
    @Test
    void testMinimum() {
        RepeatableParser repeatable = new MinimumRepeatableParser(3, NameToken.NameParser.instance, SpacesToken.SpacesParser.instance);
        assertNull(pj(repeatable.parse("-neubue   ")));
        assertNull(pj(repeatable.parse("")));
        assertNull(pj(repeatable.parse("aname")));
        assertNull(pj(repeatable.parse("aname   ")));
        assertNull(pj(repeatable.parse("aname   dswd")));
        assertNull(pj(repeatable.parse("aname   dswd  \t")));
        assertNull(pj(repeatable.parse("aname   dswd  \t6868")));
        assertNotNull(pj(repeatable.parse("aname   dswd  \t6868  \t ")));
    }
}