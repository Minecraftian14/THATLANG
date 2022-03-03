package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.Node;

import static in.mcxiv.parser.power.PowerUtils.*;

/**
 * B of BODMAS
 */
public class ParenthesisParser implements Parser<ExpressionsToken> {

    public static final ParenthesisParser parenthesisBlock = new ParenthesisParser();

    private static final Parser parser = compound(
            word("("),
            inline(ExpressionsToken.ExpressionsParser.expression),
            word(")")
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
