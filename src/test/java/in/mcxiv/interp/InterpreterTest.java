package in.mcxiv.interp;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    // AIM: calculator
    // readLine
    // if condition
    // operators

    static StringBuilder builder;

    @BeforeAll
    static void setUp() {
        TestSuite.bindToOutput(builder = new StringBuilder());
    }

    @Test
    @Disabled
    void runAllTests() {
        simpleTest();
        testExpressions();
        testVariablesAndInputs();
        testingForLoops();
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
        TestSuite.redefineInput("simple Test");
        String program = """
                main:
                    pln(scan())
                """;
        JustRunTheThing(program);
        assertOutput("simple Test", builder);
    }

    @Test
    // let_s__Pray__Bhagvaan
    void testVariablesAndInputs() {
        TestSuite.redefineInput("TestVariablesAndInputs");

        String program = """
                main:
                    var Value = Hello
                    pln(Value)
                    var Name = scan()
                    pln(Name)
                """;

        JustRunTheThing(program);
        assertOutput("Hello\nTestVariablesAndInputs", builder);
    }

    @Test
    // thanks_A_Lot_Dear_Bhagvaan
    void testExpressions() {
        TestSuite.redefineInput("2\n3");

        String program = """
                main:
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
                main:
                    for (var a = scanf(); a > 0; a = a - 1):
                        prt(a)
                """;

        JustRunTheThing(program);
        assertOutput("1.0", builder);
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