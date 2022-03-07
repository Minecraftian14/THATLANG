package in.mcxiv.parser.generic;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static in.mcxiv.parser.generic.NumeralToken.NumeralParser.numeral;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NumeralTokenTest {
    @Test
    void test() {
        assertEquals("1", TestSuite.alsoPrtln(numeral.parse("1")).getValue());
        assertEquals("115739", TestSuite.alsoPrtln(numeral.parse("115739")).getValue());
        assertEquals("+115739", TestSuite.alsoPrtln(numeral.parse("+115739")).getValue());
        assertEquals("-115739", TestSuite.alsoPrtln(numeral.parse("-115739")).getValue());
        assertEquals("1.0", TestSuite.alsoPrtln(numeral.parse("1.0")).getValue());
        assertEquals(".0", TestSuite.alsoPrtln(numeral.parse(".0")).getValue());
        assertEquals("1.", TestSuite.alsoPrtln(numeral.parse("1.")).getValue());
        assertEquals("9.65e4", TestSuite.alsoPrtln(numeral.parse("9.65e4")).getValue());
        assertEquals("9.65e+4", TestSuite.alsoPrtln(numeral.parse("9.65e+4")).getValue());
        assertEquals("9.65e-4", TestSuite.alsoPrtln(numeral.parse("9.65e-4")).getValue());
    }
}