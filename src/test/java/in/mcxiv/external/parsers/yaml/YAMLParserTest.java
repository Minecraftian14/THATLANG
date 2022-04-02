package in.mcxiv.external.parsers.yaml;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.external.parsers.json.JSONParserTest;
import in.mcxiv.external.parsers.yaml.primitive.DocumentToken;
import in.mcxiv.external.parsers.yaml.primitive.LinearValueToken;
import in.mcxiv.external.parsers.yaml.primitive.ValueToken;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.ThatVM;
import in.mcxiv.tryCatchSuite.Try;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import thatlang.core.THATObject;

import java.util.HashMap;
import java.util.List;

import static in.mcxiv.TestSuite.alsoPrtln;
import static in.mcxiv.TestSuite.pj;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SimplifiableAssertion")
class YAMLParserTest {
    @Test
    void testPrimitiveAndSimpleObjects() {
        YAMLNode node;
        assertNotNull(pj(node = YAMLParser.yaml.parse("""
                                
                                
                ---
                                
                aQuotedString: "some string value"
                aNumerical___: -34.67e58
                posInfinite__: .inf
                negInfinite__: -.inf
                                
                nan__________: .nan
                true_________: true
                false________: false
                on___________: on
                off__________: off
                nullShorty___: ~
                                
                null_________: null
                rawLine______: hello there monseuir!
                ...
                                
                """)));

        assertEquals(1, alsoPrtln("noOfDocuments", node.noOfDocuments()));
        DocumentToken document = node.getDocument(0);
        assertEquals(12, alsoPrtln("noOfFields", document.noOfFields()));
        assertEquals("some string value" /*     */, alsoPrtln("aQuotedString", document.getField("aQuotedString").getValue()));
        assertEquals(-3.467E59 /*               */, alsoPrtln("aNumerical___", document.getField("aNumerical___").getValue()));
        assertEquals(Double.POSITIVE_INFINITY /**/, alsoPrtln("posInfinite__", document.getField("posInfinite__").getValue()));
        assertEquals(Double.NEGATIVE_INFINITY /**/, alsoPrtln("negInfinite__", document.getField("negInfinite__").getValue()));
        assertEquals(Double.NaN /*              */, alsoPrtln("nan__________", document.getField("nan__________").getValue()));
        assertEquals(true /*                    */, alsoPrtln("true_________", document.getField("true_________").getValue()));
        assertEquals(false /*                   */, alsoPrtln("false________", document.getField("false________").getValue()));
        assertEquals(true /*                    */, alsoPrtln("on___________", document.getField("on___________").getValue()));
        assertEquals(false /*                   */, alsoPrtln("off__________", document.getField("off__________").getValue()));
        assertEquals(null /*                    */, alsoPrtln("nullShorty___", document.getField("nullShorty___").getValue()));
        assertEquals(null /*                    */, alsoPrtln("null_________", document.getField("null_________").getValue()));
        assertEquals("hello there monseuir!" /* */, alsoPrtln("rawLine______", document.getField("rawLine______").getValue()));

        ProgramFileToken.ProgramFileParser.programFile.parse("program main-> pln(\"Hello World\")");
        AbstractVM vm = Try.getAnd(ThatVM::new).elseRun(Assertions::fail);
        THATObject object = node.interpret(vm).seekFunction(JSONParserTest.mockSpliceCall(0));
        assertEquals("some string value" /*     */, alsoPrtln("aQuotedString", object.getMember("aQuotedString").value));
        assertEquals(-3.467E59 /*               */, alsoPrtln("aNumerical___", object.getMember("aNumerical___").value));
        assertEquals(Double.POSITIVE_INFINITY /**/, alsoPrtln("posInfinite__", object.getMember("posInfinite__").value));
        assertEquals(Double.NEGATIVE_INFINITY /**/, alsoPrtln("negInfinite__", object.getMember("negInfinite__").value));
        assertEquals(Double.NaN /*              */, alsoPrtln("nan__________", object.getMember("nan__________").value));
        assertEquals(true /*                    */, alsoPrtln("true_________", object.getMember("true_________").value));
        assertEquals(false /*                   */, alsoPrtln("false________", object.getMember("false________").value));
        assertEquals(true /*                    */, alsoPrtln("on___________", object.getMember("on___________").value));
        assertEquals(false /*                   */, alsoPrtln("off__________", object.getMember("off__________").value));
        assertEquals(null /*                    */, alsoPrtln("nullShorty___", object.getMember("nullShorty___").value));
        assertEquals(null /*                    */, alsoPrtln("null_________", object.getMember("null_________").value));
        assertEquals("hello there monseuir!" /* */, alsoPrtln("rawLine______", object.getMember("rawLine______").value));

    }

    @SuppressWarnings("unchecked")
    @Test
    void testPrimitiveAndAdvancedObjects() {
        YAMLNode node;
        assertNotNull(pj(node = YAMLParser.yaml.parse("""
                ---
                polylinearString__________: >
                    Hello there; nice to meet you.
                    This; what you see here is a multiline string.
                    However; whatever you juz read; will actually
                    be treated as a single line.
                euNeoLinerPolylinearString: |
                    Hello there; nice to meet you.
                    This; what you see here is a multiline string.
                    However; whatever you juz read; will actually
                    be treated as a single line.
                anArray: [1, "a", 3, true]
                anoArray:
                    - ooof
                    - ooof
                aMap: { fie 1: 3, fie 2: true}
                aoMap:
                    fie 1: 3
                    fie 2: true
                ...
                """)));

        // TODO: Maybe move Bracketed*Token to LVT?

        assertEquals(1, alsoPrtln("noOfDocuments", node.noOfDocuments()));
        DocumentToken document = node.getDocument(0);
        assertEquals(6, alsoPrtln("noOfFields", document.noOfFields()));
        assertEquals(152 /*       */, alsoPrtln("polylinearString__________", document.getField("polylinearString__________").getValue().toString().length()));
        assertEquals(152 /*       */, alsoPrtln("euNeoLinerPolylinearString", document.getField("euNeoLinerPolylinearString").getValue().toString().length()));

        Object obj = alsoPrtln("anArray", document.getField("anArray").getValue());
        List<LinearValueToken> anArray = (List<LinearValueToken>) obj;
        assertEquals(4 /*       */, anArray.size());

        obj = alsoPrtln("anoArray", document.getField("anoArray").getValue());
        List<ValueToken> anoArray = (List<ValueToken>) obj;
        assertEquals(2 /*       */, anoArray.size());

        obj = alsoPrtln("aMap", document.getField("aMap").getValue());
        HashMap<String , LinearValueToken> aMap = (HashMap<String , LinearValueToken>) obj;
        assertEquals(2 /*       */, aMap.size());
        assertEquals(true /*    */, aMap.get("fie 2").getValue());

        obj = alsoPrtln("aoMap", document.getField("aoMap").getValue());
        HashMap<String , ValueToken> aoMap = (HashMap<String , ValueToken>) obj;
        assertEquals(2 /*       */, aoMap.size());
        assertEquals(true /*    */, aoMap.get("fie 2").getValue());
    }
}