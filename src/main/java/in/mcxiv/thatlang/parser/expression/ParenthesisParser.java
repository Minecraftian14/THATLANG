package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tree.Node;

/**
 * B of BODMAS
 */
public class ParenthesisParser implements Parser<ExpressionsToken> {

    public static final ParenthesisParser instance = new ParenthesisParser();

    private static final Parser parser = new CompoundParser(
            new WordParser("("),
            new LooseSpaceBoundedParser(ExpressionsToken.ExpressionsParser.instance),
            new WordParser(")")
    );

    private ParenthesisParser() {
    }

    @Override
    public ExpressionsToken __parse__(ParsableString string, Node parent) {
        Node node = parser.parse(string);
        if (node == null) return null;
        ExpressionsToken token = (ExpressionsToken) node.get(1);
        if (parent != null) parent.addChild(token);
        return token;
    }
}
