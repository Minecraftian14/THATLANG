package in.mcxiv.thatlang;

import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTokenTest {
    @Test
    void testNoArg() {
        FunctionToken token = FunctionToken.function.parse("""
                fun hello:
                    print(Heya)
                """);
        assertEquals("hello", alsoPrtln("name", token.getValue()));
        assertEquals(0, alsoPrtln("args", token.parameterNames.length));
        assertEquals(1, alsoPrtln("stmts", token.noOfChildren()));
    }

    @Test
    void testYesArg() {
        FunctionToken token = FunctionToken.function.parse("""
                func world < a, b, c {
                    print(Heya)
                        pln("%s %s %s", a, b, c)
                            }
                """);
        assertEquals("world", alsoPrtln("name", token.getValue()));
        assertEquals(3, alsoPrtln("args", token.parameterNames.length));
        assertEquals(2, alsoPrtln("stmts", token.noOfChildren()));
    }
}