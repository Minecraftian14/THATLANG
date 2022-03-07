package in.mcxiv.external.parsers.json;

import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONParserTest {
    @Test
    void test() {
        assertNotNull(alsoPrtln(JSONParser.json.parse("\"hello\"")));
        assertNotNull(alsoPrtln(JSONParser.json.parse("-123.67e+78.7")));
        assertNotNull(alsoPrtln(JSONParser.json.parse("""
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
    }
}