package in.mcxiv.thatlang.parser;

import in.mcxiv.thatlang.parser.expression.ParenthesisParser;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken.QuantaExpressionParser;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.QuantaStatement;

public class SimpleSafeNonRecursiveExpressionParser extends EitherParser {

    public static final Parser<Node> instance = new SimpleSafeNonRecursiveExpressionParser();

    private SimpleSafeNonRecursiveExpressionParser() {
        super(
                ParenthesisParser.instance,
                QuantaExpressionParser.instance

        );
    }
}
