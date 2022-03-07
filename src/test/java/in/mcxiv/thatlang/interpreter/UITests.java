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

@Disabled("We had to disable it because of the sleep(5000000)")
class UITests {

    static StringBuilder builder;

    @BeforeAll
    static void setUp() {
        TestSuite.bindToOutput(builder = new StringBuilder());
    }

    public static void main(String[] args) {
        UITests.setUp();
        new UITests().test2();
    }

    @Test
    void test1() {
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
    void test2() {
        LogLevel.DEBUG.activate();
        String program = """
                program main {
                    val size = 500
                    UI("Example", size, size)
                    
                    // Draw a rectangle
                    UI().box() .xy -0.05 0 .xy 1.   1. .rgba 1. 1. 1. 50
                    UI().box() .xy  0    0 .xy 0.05 1. .rgba 1. 1. 1. 50
                    
                    // Draw an oval
                    UI().oval() .xy 0 0.9 .xy 1. 1.1 .rgba 1. 1. 1. 50
                    
                    // Draw a polygon
                    UI().poly(0,0, 0.1,1, 0.9,1, 1,0) .xy 0 0 .wh 1. .05 .rgba 1. 1. 1. 50
                    
                    // Create a Button
                    UI().button(onclick=act()) .xy 0.1 0.1 .wh 0.3 0.09
                    
                    // Create a Field
                    UI().field(text="enter smth") .xy 0.1 0.2 .wh 0.3 0.09
                    
                    // Create a Check Box
                    UI().check(text="Do you want it?") .xy 0.1 0.3 .wh 0.3 0.09
                    
                    // Create Radio Buttons
                    val rA = UI().radio("Option A") .xy 0.1 0.4 .wh 0.19 0.09
                    val rB = UI().radio("Option B") .xy 0.3 0.4 .wh 0.19 0.09
                    val rC = UI().radio("Option C") .xy 0.5 0.4 .wh 0.19 0.09
                    UI().group(rA, rB, rC)
                    
                    UI()
                    sleep(5000000)
                }
                    
                function act():
                    prtln("Button Pressed!")
                """;

        JustRunTheThing(program, false);
    }

    private void assertOutput(String s, StringBuilder builder) {
        s = s.replaceAll("[\n\r]", "");
        String act = builder.toString().replaceAll("[\n\r]", "");
        assertEquals(s, act.substring(act.length() - s.length()));
    }

    private static void JustRunTheThing(String program, boolean print) {
        ProgramFileToken file;
        assertNotNull((file = ProgramFileToken.ProgramFileParser.programFile.parse(program)));
        if (print) System.out.println(TestSuite.pj(file));

        AbstractVM vm = Try.GetAnd(ThatVM::new).Else(Assertions::fail);
        vm.load(file);
        vm.run();
    }

}