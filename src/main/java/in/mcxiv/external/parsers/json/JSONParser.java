package in.mcxiv.external.parsers.json;

import in.mcxiv.external.parsers.json.primitive.ValueToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.PowerUtils;

public class JSONParser implements Parser<JSONNode> {

    public static final Parser<JSONNode> json = new JSONParser();

    private static final Parser<?> parser = PowerUtils.block(ValueToken.ValueParser.value);

    private JSONParser() {
    }

    @Override
    public JSONNode __parse__(ParsableString string, Node parent) {
        Node node = parser.parse(string);
        if (node == null) return null;
        return new JSONNode(parent, node);
    }
}
