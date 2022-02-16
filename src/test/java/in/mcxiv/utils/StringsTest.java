package in.mcxiv.utils;

import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static in.mcxiv.utils.Strings.*;
import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void testSimpleMethods() {
        assertEquals("STRINGS_TEST", alsoPrtln(camelToUpper("StringsTest")));
        assertEquals(2, alsoPrtln(countUpperCase("StringsTest")));
        assertEquals("StringsTest", alsoPrtln(getLastWord("in.mcxiv.utils.StringsTest", '.')));
        assertEquals("in.mcxiv.utils", alsoPrtln(exceptLastWord("in.mcxiv.utils.StringsTest", '.')));
    }
}