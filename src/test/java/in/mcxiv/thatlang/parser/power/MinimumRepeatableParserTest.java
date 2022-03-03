package in.mcxiv.thatlang.parser.power;

import in.mcxiv.parser.power.MinimumRepeatableParser;
import in.mcxiv.parser.power.PowerUtils;
import in.mcxiv.parser.power.RepeatableParser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.SpacesToken;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.pj;
import static org.junit.jupiter.api.Assertions.*;

class MinimumRepeatableParserTest {
    @Test
    void testMinimum() {
        RepeatableParser repeatable = new MinimumRepeatableParser(3, PowerUtils.compound(NameToken.NameParser.name, SpacesToken.SpacesParser.spaces));
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