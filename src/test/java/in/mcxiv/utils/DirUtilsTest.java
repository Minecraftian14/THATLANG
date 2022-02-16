package in.mcxiv.utils;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import java.io.File;

import static in.mcxiv.TestSuite.alsoPrtln;
import static in.mcxiv.utils.DirUtils.trimSeperator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DirUtilsTest {

    @Test
    void simpleMethodTest() {
        assertEquals("Documents", alsoPrtln(trimSeperator("\\Documents/")));
        assertEquals("D:\\Documents\\Profile.log", alsoPrtln(DirUtils.formPath("D:", "Documents", "Profile.log")));
    }
}