package in.mcxiv.parser.generic;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static in.mcxiv.parser.generic.NumeralToken.NumeralParser.numeral;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NumeralTokenTest {
    @Test
    void testParser() {
        assertEquals("1", TestSuite.alsoPrtln(numeral.parse("1")).getValue());
        assertEquals("115739", TestSuite.alsoPrtln(numeral.parse("115739")).getValue());
        assertEquals("+115739", TestSuite.alsoPrtln(numeral.parse("+115739")).getValue());
        assertEquals("-115739", TestSuite.alsoPrtln(numeral.parse("-115739")).getValue());
        assertEquals("1.0", TestSuite.alsoPrtln(numeral.parse("1.0")).getValue());
        assertEquals(".0", TestSuite.alsoPrtln(numeral.parse(".0")).getValue());
        assertEquals("1.", TestSuite.alsoPrtln(numeral.parse("1.")).getValue());
        assertEquals("9.65e4", TestSuite.alsoPrtln(numeral.parse("9.65e4")).getValue());
        assertEquals("9.65e+4.5", TestSuite.alsoPrtln(numeral.parse("9.65e+4.5")).getRawNumber());
        assertEquals("9.65e-4.9", TestSuite.alsoPrtln(numeral.parse("9.65e-4.9")).getRawNumber());
    }

    @Test
    void testReducer() {
        assertEquals(1, TestSuite.alsoPrtln(numeral.parse("1")).reduceToNumber());
        assertEquals(115739, TestSuite.alsoPrtln(numeral.parse("115739")).reduceToNumber());
        assertEquals(+115739, TestSuite.alsoPrtln(numeral.parse("+115739")).reduceToNumber());
        assertEquals(-115739, TestSuite.alsoPrtln(numeral.parse("-115739")).reduceToNumber());
        assertEquals(1.0, TestSuite.alsoPrtln(numeral.parse("1.0")).reduceToNumber());
        assertEquals(.0, TestSuite.alsoPrtln(numeral.parse(".0")).reduceToNumber());
        assertEquals(1., TestSuite.alsoPrtln(numeral.parse("1.")).reduceToNumber());
        assertEquals(9.65e4, TestSuite.alsoPrtln(numeral.parse("9.65e4")).reduceToNumber());
        assertEquals(305159.7942062486, TestSuite.alsoPrtln(numeral.parse("9.65e+4.5")).reduceToNumber());
        assertEquals(1.2148630223813703E-4, TestSuite.alsoPrtln(numeral.parse("9.65e-4.9")).reduceToNumber());
    }
}