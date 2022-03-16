package in.mcxiv.external.parsers.json;

import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.expression.MappedArgumentsToken;
import in.mcxiv.thatlang.expression.NumeralExpressionToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.ThatVM;
import in.mcxiv.thatlang.natives.StringToken;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import thatlang.core.THATObject;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JSONParserTest {
    @Test
    void test() {
        assertNotNull(alsoPrtln(JSONParser.json.parse("\"hello\"")));
        assertNotNull(alsoPrtln(JSONParser.json.parse("-123.67e+78")));

        JSONNode node;
        assertNotNull(alsoPrtln(node = JSONParser.json.parse("""
                {
                    "field1": "some string value",
                    "field2": -123.56,
                    "field3": [true, false, null],
                    "field4": {
                        "field6": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
                        "field7": {"a": 1,"b": 2,"c": 3,"d": 4}
                    }
                }
                """)));

        ProgramFileToken.ProgramFileParser.programFile.parse("""
                program main-> pln("Hello World")
                """);
        AbstractVM vm = Try.GetAnd(ThatVM::new).Else(Assertions::fail);
        THATObject object = node.interpret(vm);
        assertEquals("some string value", alsoPrtln(object.getMember("field1").value));
        assertEquals(-123.56, alsoPrtln(object.getMember("field2").value));
        assertEquals(true, alsoPrtln(object.getMember("field3").seekFunction(mockSpliceCall(0)).value));
        assertEquals(false, alsoPrtln(object.getMember("field3").seekFunction(mockSpliceCall(1)).value));
        assertEquals(null, alsoPrtln(object.getMember("field3").seekFunction(mockSpliceCall(2)).value));
        object = object.getMember("field4");
        assertEquals(3, alsoPrtln(object.getMember("field6").seekFunction(mockSpliceCall(2)).value));
        assertEquals(2, alsoPrtln(object.getMember("field7").getMember("b").value));
    }

    public static final FunctionCallToken mockSpliceCall(int index) {
        return new FunctionCallToken("__splice__", new MappedArgumentsToken(new MappedArgumentsToken.MappingsToken[0], new ExpressionsToken[]{
                new NumeralExpressionToken(index)
        }));
    }
}