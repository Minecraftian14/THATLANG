package in.mcxiv.thatlang.power;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.power.CompoundParser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.SpacesToken;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static in.mcxiv.parser.power.PowerUtils.compound;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompoundParserTest {
    @Test
    void test() {
        NameToken.NameParser name_parser = NameToken.NameParser.name;
        SpacesToken.SpacesParser spaces_parser = new SpacesToken.SpacesParser();
        CompoundParser compound = compound(name_parser, spaces_parser, name_parser);
        assertNotNull(alsoPrtln(compound.parse(new ParsableString("46 89"))));
        assertNotNull(alsoPrtln(compound.parse(new ParsableString("mo\tld"))));
        assertNull(alsoPrtln(compound.parse(new ParsableString("-8c8f"))));
        assertNull(alsoPrtln(compound.parse(new ParsableString("{8c8f"))));
        assertNotNull(alsoPrtln(compound.parse(new ParsableString("12   43"))));
        assertNotNull(alsoPrtln(compound.parse(new ParsableString("asbd \t\t 89"))));
    }
}