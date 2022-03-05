package in.mcxiv.thatlang.interpreter;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AbstractVMTest {

    static StringBuilder builder;

    @BeforeAll
    static void setUp() {
        TestSuite.bindToOutput(builder = new StringBuilder());
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
                    pln ( a + b * (a ** b) - (a / b) * (a << b) +( a<<b)/a)
                """;

        JustRunTheThing(program);
        assertOutput("18", builder);
    }

    @Test
    void testingForLoops() {
        TestSuite.redefineInput("9");

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
                    if(o equals "*")->pln(a*b)
                    if(o equals "/")->pln(a/b)
                    if(o equals "+")->pln(a+b)
                    if(o equals "-")->pln(a-b)
                    if(o equals "%")->pln(a%b)
                }
                                                """;
        JustRunTheThing(program);
        assertOutput("200", builder);
    }

    @Test
    void test___ProllyTheLast___Well() {
        TestSuite.redefineInput("11");

        //noinspection UnnecessaryStringEscape
        String program = """
                program main:
                    var val = scani()
                    if val == 10 -> pln("YES")
                    else if val == 11 -> pln("<'_'>")
                    else -> pln("BRUH")
                    val.sub = 10
                    pln(val.sub)
                    val .sub 10 11 12 .max 1 2 3
                    printf("%s %s %s\n", val.s, val.u, val.b)
                    printf("%s %s %s\n", val.m, val.a, val.x)
                """;

        JustRunTheThing(program);
        assertOutput("""
                <'_'>
                10
                10 11 12
                1 2 3
                """, builder);

    }

    @Test
    void functions() {
        String program = """
                program main  -> act(hey)
                fun act(word) -> printf(">> %s%n", word)
                """;

        JustRunTheThing(program);
        assertOutput("hey", builder);

        program = """
                program main:
                    act(Hello, World)
                    Hello act World
                fun act(a, b) -> printf(">> %s %s%n", a, b)
                """;

        JustRunTheThing(program);
        assertOutput("Hello World", builder);

        program = """
                program main:
                    act("Hello", "World")
                    pln(c)
                fun c act(a, b) -> c = a + b
                """;

        JustRunTheThing(program);
        assertOutput("HelloWorld", builder);
    }

    @Test
    void simPleProGram() {
        String program = """
                                
                program main {
                    act(19867, 23)
                    pf("Q = %d, R = %d\n", quo, rem)
                }
                    
                fun quo, rem act(numA, numB) {
                    quo = numA / numB
                    rem = numA - quo
                }
                                
                """;

        JustRunTheThing(program);
        assertOutput("Q = 863, R = 19004", builder);
    }

    public static void main(String[] args) {
        AbstractVMTest test = new AbstractVMTest();
        test.setUp();
        test.testUI();
    }

    @Test
    void testUI() {
        LogLevel.DEBUG.activate();
        String program = """
                program main {
                    val size = 500
                    UI("Example", size, size)
                    for (var i = 0; i < size; i = i+50) :
                        for (var j = 0; j < size; j = j+50) :
                            var box = UI().box()
                            box .xy i j .wh 0.1 0.1
                            if ((i + j)/50) % 2 == 0 -> box .rgb 255 0 255
                            or else                  -> box .rgb 255 255 0
                            sleep(16)
                    UI().button("Press Me!", act("oi?")) .xywh 0.4 0.4 0.2 0.1
                    UI()
                    sleep(5000000)
                }
                    
                function act(word):
                    prtln(word)
                """;

        JustRunTheThing(program);
    }

    private void assertOutput(String s, StringBuilder builder) {
        s = s.replaceAll("[\n\r]", "");
        String act = builder.toString().replaceAll("[\n\r]", "");
        assertEquals(s, act.substring(act.length() - s.length()));
    }

    private static void JustRunTheThing(String program) {
        ProgramFileToken file;
        assertNotNull((file = ProgramFileToken.ProgramFileParser.programFile.parse(program)));
//        System.out.println(TestSuite.pj(file));

        AbstractVM vm = Try.GetAnd(ThatVM::new).Else(Assertions::fail);
        vm.load(file);
        vm.run();
    }

}