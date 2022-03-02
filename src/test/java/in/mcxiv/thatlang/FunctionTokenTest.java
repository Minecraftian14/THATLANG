package in.mcxiv.thatlang;

import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTokenTest {
    @Test
    void testNoArg() {
        FunctionToken token = FunctionToken.function.parse("""
                fun hello():
                    print(Heya)
                """);
        assertEquals("hello", alsoPrtln("name", token.getValue()));
        assertEquals(0, alsoPrtln("parm", token.getParameterNames().length));
        assertEquals(0, alsoPrtln("rets", token.getReturnArgNames().length));
        assertEquals(1, alsoPrtln("stms", token.noOfChildren()));
    }

    @Test
    void testYesArg() {
        FunctionToken token = FunctionToken.function.parse("""
                func world (a, b, c) {
                    print(Heya)
                        pln("%s %s %s", a, b, c)
                            }
                """);
        assertEquals("world", alsoPrtln("name", token.getValue()));
        assertEquals(3, alsoPrtln("parm", token.getParameterNames().length));
        assertEquals(0, alsoPrtln("rets", token.getReturnArgNames().length));
        assertEquals(2, alsoPrtln("stms", token.noOfChildren()));
    }

    @Test
    void testNoArgYesRet() {
        FunctionToken token = FunctionToken.function.parse("""
                func e,f world() {
                    print(Heya)
                        pln("%s %s %s", a, b, c)
                            }
                """);
        assertEquals("world", alsoPrtln("name", token.getValue()));
        assertEquals(0, alsoPrtln("parm", token.getParameterNames().length));
        assertEquals(2, alsoPrtln("rets", token.getReturnArgNames().length));
        assertEquals(2, alsoPrtln("stms", token.noOfChildren()));
    }

    @Test
    void testYesArgYesRet() {
        FunctionToken token = FunctionToken.function.parse("""
                func e,f world(ugceugm, wdhwd, wdihm) {
                    print(Heya)
                        pln("%s %s %s", a, b, c)
                            }
                """);
        assertEquals("world", alsoPrtln("name", token.getValue()));
        assertEquals(3, alsoPrtln("parm", token.getParameterNames().length));
        assertEquals(2, alsoPrtln("rets", token.getReturnArgNames().length));
        assertEquals(2, alsoPrtln("stms", token.noOfChildren()));
    }
}