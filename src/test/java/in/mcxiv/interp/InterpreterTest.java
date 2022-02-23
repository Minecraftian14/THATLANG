package in.mcxiv.interp;

import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {


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
        assertNotNull(TestSuite.pj(file = ProgramFileToken.ProgramFileParser.programFile.parse(program)));

        assertDoesNotThrow(() -> new InterpreterOLD(file, new String[0]));
        assertDoesNotThrow(() -> new InterpreterOLD(file, new String[0]).run(file));

    }

    @Test
    void simpleTest() {
        TestSuite.redefineInput("simple Test");
        String program = """
                program main->
                    pln(scan())
                """;
        JustRunTheThing(program);
        assertOutput("simple Test", builder);
    }

    @Test
    void testTheNewIndentTypeBlck() {
        TestSuite.redefineInput("simple Test");
        String program = """
                program main:
                                
                    pln(scan())
                    
                    prtf("%s", "lmao")
                    
                """;
        JustRunTheThing(program);
        assertOutput("simple Test\nlmao", builder);
    }

    @Test
        // let_s__Pray__Bhagvaan
    void testVariablesAndInputs() {
        TestSuite.redefineInput("TestVariablesAndInputs");

        String program = """
                program main {
                    var Value = Hello
                    pln(Value)
                    var Name = scan()
                    pln(Name)
                }
                """;

        JustRunTheThing(program);
        assertOutput("Hello\nTestVariablesAndInputs", builder);
    }

    @Test
        // thanks_A_Lot_Dear_Bhagvaan
    void testExpressions() {
        TestSuite.redefineInput("2\n3");

        String program = """
                program main:
                    var a = scani()
                    var b = scanf()
                    pln ( a + b * (a ** b) - (a&&b) * (a xand b) +( a<<b)/a)
                """;

        JustRunTheThing(program);
        assertOutput("12.0", builder);
    }

    @Test
    void testingForLoops() {
        TestSuite.redefineInput("10");

        String program = """
                program main:
                    for (var a = scanf(); a > 0; a = a - 1)->pln(a)
                   
                """;

        JustRunTheThing(program);
        assertOutput("1.0", builder);
    }

    @Test
    void calculator() {
        TestSuite.redefineInput("10\n20\n*");

        String program = """
program main {
    var a = scani()
    var b = scani()
    var o = scan()
    prt("The value of operand A received is ")
    pln(a)
    prt("The value of operand B received is ")
    pln(b)
    prt("The operation on A and B requested is ")
    pln(o)
    prt("The result of this operation is ")
    if(o=="*")->pln(a*b)
    if(o=="/")->pln(a/b)
    if(o=="+")->pln(a+b)
    if(o=="-")->pln(a-b)
    if(o=="%")->pln(a%b)
                }
                                """;
        JustRunTheThing(program);
        assertOutput("200.0", builder);
    }

    private void assertOutput(String s, StringBuilder builder) {
        s = s.replaceAll("[\n\r]", "");
        String act = builder.toString().replaceAll("[\n\r]", "");
        assertEquals(s, act.substring(act.length() - s.length()));
    }

    private static void JustRunTheThing(String program) {
        ProgramFileToken file;
        assertNotNull((file = ProgramFileToken.ProgramFileParser.programFile.parse(program)));

        ThatVM vm = Try.GetAnd(ThatVM::new).Else(Assertions::fail);
        vm.load(file);
        vm.run();
    }
}