package thatlang.core;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.*;

class THOSEObjectsTest {
    @Test
    void testObjectCreationByReduction() {
        assertEquals(Integer.class, alsoPrtln(THOSEObjects.createAfterReducing("5799").primaryInference));
        assertEquals(Integer.class, alsoPrtln(THOSEObjects.createAfterReducing("+5799").primaryInference));
        assertEquals(Integer.class, alsoPrtln(THOSEObjects.createAfterReducing("-5799").primaryInference));
        assertEquals(Long.class, alsoPrtln(THOSEObjects.createAfterReducing("5799l").primaryInference));
        assertEquals(Float.class, alsoPrtln(THOSEObjects.createAfterReducing("5799.90f").primaryInference));
        assertEquals(Double.class, alsoPrtln(THOSEObjects.createAfterReducing("5799.90").primaryInference));
    }
}