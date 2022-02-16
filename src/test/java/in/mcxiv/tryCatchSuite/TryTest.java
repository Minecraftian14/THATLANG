package in.mcxiv.tryCatchSuite;

import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TryTest {

    @Test
    void simpleMethodTest() {
        assertEquals(100, alsoPrtln(Try.GetAnd(() -> 1000 / 10).Else(() -> -1)));
        assertEquals(-1, alsoPrtln(Try.GetAnd(() -> 1000 / 0).Else(() -> -1)));

        assertThrows(RuntimeException.class, () -> Try.RunAnd(() -> String.valueOf(1000 / 0)).Catch(exception -> {
            throw new RuntimeException();
        }));


        assertEquals(1, Try.If(() -> false).Then(() -> 0).Else(() -> 1));
        assertEquals(0, Try.If(() -> true).Then(() -> 0).Else(() -> 1));
    }
}