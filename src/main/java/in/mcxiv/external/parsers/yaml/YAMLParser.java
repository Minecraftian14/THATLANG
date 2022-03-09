package in.mcxiv.external.parsers.yaml;

import in.mcxiv.external.parsers.yaml.primitive.DocumentToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;

import static in.mcxiv.parser.power.PowerUtils.block;
import static in.mcxiv.parser.power.PowerUtils.repeatable;

public class YAMLParser implements Parser<YAMLNode> {

    public static final Parser<YAMLNode> yaml = new YAMLParser();

    private static final Parser<?> parser = block(repeatable(DocumentToken.DocumentParser.document));

    @Override
    public YAMLNode __parse__(ParsableString string, Node parent) {
        Node node = parser.parse(string);
        if (node == null) return null;
        return new YAMLNode(parent, node.getChildren().toArray(Node[]::new));
    }
}
