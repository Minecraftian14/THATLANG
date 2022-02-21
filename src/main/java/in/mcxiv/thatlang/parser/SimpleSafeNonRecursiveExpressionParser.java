package in.mcxiv.thatlang.parser;

import in.mcxiv.thatlang.parser.expression.ParenthesisParser;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken.QuantaExpressionParser;
import in.mcxiv.thatlang.parser.natives.StringToken;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.tree.Node;

public class SimpleSafeNonRecursiveExpressionParser extends EitherParser {

    public static final Parser<Node> instance = new SimpleSafeNonRecursiveExpressionParser();

    private SimpleSafeNonRecursiveExpressionParser() {
        super(
                StringToken.StringParser.instance,
                ParenthesisParser.instance,
                QuantaExpressionParser.instance
        );
    }
}
