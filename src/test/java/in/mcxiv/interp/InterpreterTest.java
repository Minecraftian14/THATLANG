package in.mcxiv.interp;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    // AIM: calculator
    // readLine
    // if condition
    // operators

    StringBuilder builder;

    @BeforeEach
    void setUp() {
        TestSuite.bindToOutput(builder = new StringBuilder());
    }

    @Test
    @Disabled("Deprecated")
    void test() {
        String program = """
                main ->
                    print(HelloWorld)
                """;

        ProgramFileToken file;
        assertNotNull(TestSuite.pj(file = ProgramFileToken.ProgramFileParser.instance.parse(program)));

        assertDoesNotThrow(() -> new InterpreterOLD(file, new String[0]));
        assertDoesNotThrow(() -> new InterpreterOLD(file, new String[0]).run(file));

    }

    @Test
    void simpleTest() {
        String program = """
                main ->
                    pln(HelloWorld)
                """;
        JustRunTheThing(program);
        assertOutput("HelloWorld", builder);
    }

    @Test
    void let_s__Pray__Bhagvaan() {
        TestSuite.redefineInput("97");

        String program = """
                main ->
                    var Value = Hello
                    pln(Value)
                    var Name = scan()
                    pln(Name)
                """;

        JustRunTheThing(program);
        assertOutput("Hello\n97", builder);
    }

    @Test
    void thanks_A_Lot_Dear_Bhagvaan() {
        TestSuite.redefineInput("2\n3");

        String program = """
                main ->
                    var a = scani()
                    var b = scanf()
                    pln(a+b*(a ** b)-(a&&b)*(a xand b)+(a<<b)/a)
                """;

        JustRunTheThing(program);
        assertOutput("12.0", builder);
    }

    @Test
    void testingForLoops() {
        TestSuite.redefineInput("10");

        String program = """
                main ->
                    for (var a = scanf(); a > 0; a = a - 1) ->
                        pln(a)
                """;

        JustRunTheThing(program);
        assertOutput("3.0", builder);
    }

    private void assertOutput(String s, StringBuilder builder) {
        s = s.replaceAll("[\n\r]", "");
        String act = builder.toString().replaceAll("[\n\r]", "");
        assertEquals(s, act.substring(act.length() - s.length()));
    }

    private static void JustRunTheThing(String program) {
        ProgramFileToken file;
        assertNotNull((file = ProgramFileToken.ProgramFileParser.instance.parse(program)));

        ThatVM vm = Try.GetAnd(ThatVM::new).Else(Assertions::fail);
        vm.load(file);
        vm.run();
    }
}