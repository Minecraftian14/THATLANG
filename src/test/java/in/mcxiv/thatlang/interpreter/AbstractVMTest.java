package in.mcxiv.thatlang.interpreter;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.TestSuite;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AbstractVMTest {

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
        JustRunTheThing(program, false);
        assertOutput("simple Test", builder);
    }

    @Test
    void testTheNewIndentTypeBlck() {
        TestSuite.redefineInput("simple Test");
        String program = """
                program main:
                                
                    pln(scan())
                    
                    var something = ""
                        OMG
                        HEYA
                        noice
                    ""
                    
                    pln(something)
                    
                    something = <%
                        <!DOCTYPE json>
                        {
                            "field1": 1289,
                            "field2": {"value":[1987]}
                        }
                    %>
                    
                    pln(something.field1)
                    pln(something.field2.value[0])
                    
                    val something = <%
                        <!DOCTYPE yaml>
                        ---
                        field1: 1289
                        field2: {value: 1987}
                        field 3:
                          that thing:
                            - Hello
                            - World
                        ...
                    %>
                    
                    pln(something[0].field1)
                    pln(something[0].field2.value)
                    pln(something[0]["field 3"]["that thing"][1])
                    
                    // A comment
                    
                    /* And a block comment
                                */
                    
                    /**
                     * And a box comment
                     **/
                    
                    /============================#=====================\\
                    |            MODEL           | Activation Function |
                    |----------------------------|---------------------|
                    | Regression                 | Linear Function     |
                    | Binary Classification      | Sigmoid Function    |
                    | Multiclass Classification  | Soft-Max Function   |
                    | Multilabel Classification  | Sigmoid Function    |
                    \\============================^=====================/
                    
                    prtf("%s", "lmao")
                    
                """;
        JustRunTheThing(program, false);
        assertOutput("""
                simple Test
                                
                OMG
                HEYA
                noice
                   \s
                                
                1289
                1987
                1289
                1987
                World
                                
                lmao""", builder);
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

        JustRunTheThing(program, false);
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
                    //  ( 2 + 3 * (2 ** 3) - (2 / 3) * (2 << 3) +( 2<<3)/2)
                    //  ( 2 + 3 *    8     -    0    *    16    +   16  /2)
                    //  ( 2 +      24      -         0          +    8    )
                    //  (                       34                        )
                """;

        JustRunTheThing(program, false);
        assertOutput("34", builder);
    }

    @Test
    void testingForLoops() {
        TestSuite.redefineInput("9");

        String program = """
                program main:
                    for (var a = scanf(); a > 0; a = a - 1)->pln(a)
                   
                """;

        JustRunTheThing(program, false);
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
        JustRunTheThing(program, false);
        assertOutput("200", builder);
    }

    @Test
    void test___ProllyTheLast___Well() {
        TestSuite.redefineInput("11");

        //noinspection UnnecessaryStringEscape
        String program = """
                program main:
                    var smx = scani()
                    if smx == 10 -> pln("YES")
                    elif smx == 11 -> pln("<'_'>")
                    or else -> pln("BRUH")
                    smx.sub = 10
                    pln(smx.sub)
                    smx .sub 10 11 12 .max 1 2 3
                    printf("%s %s %s\n", smx.s, smx.u, smx.b)
                    printf("%s %s %s\n", smx.m, smx.a, smx.x)
                    
                    smx = [1, 2, 3]
                    printf("%-20s %-20s %-20s\n", smx, smx[0], smx.type)
                    smx = l[1, 2, 3]
                    printf("%-20s %-20s %-20s\n", smx, smx[1], smx.type)
                    smx = {1, 2, 3}
                    printf("%-20s %-20s %-20s\n", smx, smx[2], smx.type)
                    smx = {"a":1, "b":2, "c":3}
                    printf("%-20s %-20s %-20s\n", smx, smx["a"], smx.type)
                    smx = t{a:1, b:2, c:3}
                    printf("%-20s %-20s %-20s\n", smx, smx[b], smx.type)
                    smx = |1, 2, 3<
                    printf("%-20s %-20s %-20s\n", smx, smx[0], smx.type)
                """;

        JustRunTheThing(program, false);
        assertOutput("""
                <'_'>
                10
                10 11 12
                1 2 3
                [1, 2, 3]            1                    ArrayList          \s
                l[1, 2, 3]           2                    LinkedList         \s
                {3, 1, 2}            true                 HashSet            \s
                {c=3, b=2, a=1}      1                    HashMap            \s
                t{c=3, b=2, a=1}     2                    Hashtable          \s
                |1, 2, 3<            1                    Stack              \s
                """, builder);

    }

    @Test
    void functions() {
        String program = """
                program main  -> act(hey)
                fun act(word) -> printf(">> %s%n", word)
                """;

        JustRunTheThing(program, false);
        assertOutput("hey", builder);

        program = """
                program main:
                    act(Hello, World)
                    Hello act World
                fun act(a, b) -> printf(">> %s %s%n", a, b)
                """;

        JustRunTheThing(program, false);
        assertOutput("Hello World", builder);

        program = """
                program main:
                    act("Hello", "World")
                    pln(c)
                fun c act(a, b) -> c = a + b
                """;

        JustRunTheThing(program, false);
        assertOutput("HelloWorld", builder);

        program = """
                program main:
                    // Simple call
                    defineVariables(45)
                    pln(" a = %.1f, b = %.1f" % [a, b])
                    
                    // A Binary call
                    a operatesOn b
                    pln(" a = %.1f " % a)
                            
                func a, b defineVariables(num):
                    a = 3.5 * num
                    b = 6.2 * num
                    
                func operatesOn(x, y):
                    x = x + y
                """;

        JustRunTheThing(program, false);

        program = """
                program main:
                   printThis("Hello", "World")
                   printThis(second="World", first="Hello")
                 
                func printThis(first, second):
                   p("%s %s!%n" % [first, second])
                """;

        JustRunTheThing(program, false);
    }

    @Test
    void simPleProGram() {
        String program = """
                                
                program main {
                    act(numB=23, 19867)
                    pf("Q = %d, R = %d\n", quo, rem)
                }
                    
                fun quo, rem act(numA, numB) {
                    quo = numA / numB
                    rem = numA - quo
                }
                                
                """;

        JustRunTheThing(program, true);
        assertOutput("Q = 863, R = 19004", builder);
    }

    public static void main(String[] args) {
        AbstractVMTest test = new AbstractVMTest();
        test.setUp();
//        test.testUI();
        test.testProgramsExecution();
    }

    @Test
    @Disabled("We had to disable it because of the sleep(5000000)")
    void testUI() {
        LogLevel.DEBUG.activate();
        String program = """
                program main {
                    val size = 500
                    UI("Example", size, size)
                    UI().box() .xy 0.1 0.1 .xy -0.1 -0.1
                    for (var i = 0; i < size; i = i+50) :
                        for (var j = 0; j < size; j = j+50) :
                            var box = UI().box()
                            box .xy i j .wh 0.1 0.1
                            if ((i + j)/50) % 2 == 0 -> box .rgb 200 0 200
                            or else                  -> box .rgb 200 200 0
                            sleep(16)
                    UI().oval() .xyxy 0.15 0.15 -0.15 -0.15 .rgb 100 0 100
                    UI().poly(0,1,1,1,0.5,0.8) .xyxy 0.15 0.15 -0.15 -0.15 .rgb 0 0 0
                    UI().button("Press Me!", act("oi?")) .xywh 0.4 0.4 0.2 0.1
                    UI()
                    sleep(5000000)
                }
                    
                function act(word):
                    prtln(word)
                """;

        JustRunTheThing(program, false);
    }

    @Test
    @Disabled("This test was disabled as having parallel execution of prog2 interferes with the output of other tests.")
    void testProgramsExecution() {
        String program = """
                program main:
                    pln("Main program running")
                    start prog1
                    launch prog2
                    launch prog2
                    end with prog3
                    pln("Main program ran")
                    
                program prog1 -> pln("Prog1 program ran")
                program prog3 {
                    pln("Prog3 program ran")
                }
                                
                program prog2:
                    for(val i=0;i<50;i=i+1):
                        prt(i+" ")
                        // sleep(1000/50)
                    pln("ooof")
                """;

        JustRunTheThing(program, false);
    }

    @Test
    void testContext() {
        String program = """
                context letter:
                    val a = 1
                    val b = 2
                    val c = 3
                    
                program main:
                    val letter = acquire letter
                    pln("Heya")
                    pln("letter.%s = %d" % ["a", letter.a])
                    start prog1
                    
                program prog1:
                    val letter = acquire letter
                    pln("letter.b = %d" % letter.b)
                """;

        JustRunTheThing(program, false);
    }

    @Test
    void testTheRuntimeTestExample() {
        TestSuite.redefineInput("13");

        String program = """
                program main:
                    pln("Please enter a number:")
                    var number = scani()
                    
                    pln("You entered %d." % number)
                               
                    if isPrime(number) ->
                        println("The given number is a prime number.")
                    or else ->
                        println("The given number is not a prime number.")
                               
                               
                func isPrime isPrime(number):
                    if number == 0 or number == 1:
                        isPrime = false
                    else:
                        isPrime = true
                        for (val i=2; isPrime and i<number; i=i+1):
                            if number%i==0 -> isPrime=false;
                """;

        JustRunTheThing(program, true);
    }

    public static void assertOutput(String s, StringBuilder builder) {
        s = s.replaceAll("[\n\r]", "");
        String act = builder.toString().replaceAll("[\n\r]", "");
        assertEquals(s, act.substring(act.length() - s.length()));
    }

    public static void JustRunTheThing(String program, boolean print) {
        ProgramFileToken file;
        assertNotNull((file = ProgramFileToken.ProgramFileParser.programFile.parse(program)));
        if (print) System.out.println(TestSuite.pj(file));

        AbstractVM vm = Try.getAnd(ThatVM::new).elseRun(Assertions::fail);
        vm.load(file);
        vm.run();
    }

}