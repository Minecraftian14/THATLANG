package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tree.Node;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

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
