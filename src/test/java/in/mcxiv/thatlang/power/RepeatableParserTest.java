package in.mcxiv.thatlang.power;

import in.mcxiv.parser.power.PowerUtils;
import in.mcxiv.parser.power.RepeatableParser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.SpacesToken;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.pj;
import static org.junit.jupiter.api.Assertions.*;

class RepeatableParserTest {
    @Test
    void test() {
        RepeatableParser repeatable = new RepeatableParser(PowerUtils.compound(NameToken.NameParser.name, SpacesToken.SpacesParser.spaces));
        assertNull(pj(repeatable.parse("-neubue   ")));
        assertNull(pj(repeatable.parse("")));
        assertNull(pj(repeatable.parse("aname")));
        assertNotNull(pj(repeatable.parse("aname   ")));
        assertNotNull(pj(repeatable.parse("aname   dswd")));
        assertNotNull(pj(repeatable.parse("aname   dswd  \t")));
        assertNotNull(pj(repeatable.parse("aname   dswd  \t6868")));
        assertNotNull(pj(repeatable.parse("aname   dswd  \t6868  \t ")));
    }
}