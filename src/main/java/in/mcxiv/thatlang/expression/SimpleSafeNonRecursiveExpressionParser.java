package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.QuantaExpressionToken.QuantaExpressionParser;
import in.mcxiv.thatlang.natives.ExternalCodeToken;
import in.mcxiv.thatlang.natives.ExternalCodeToken.ExternalCodeParser;
import in.mcxiv.thatlang.natives.MultilineStringToken.MultilineStringParser;
import in.mcxiv.thatlang.natives.StringToken;
import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.parser.Node;

public class SimpleSafeNonRecursiveExpressionParser extends EitherParser {

    public static final Parser<Node> safeExpression = new SimpleSafeNonRecursiveExpressionParser();

    private SimpleSafeNonRecursiveExpressionParser() {
        super(
                MultilineStringParser.multiLineString,
                ExternalCodeParser.externalCode,
                StringToken.StringParser.string,
                ParenthesisParser.parenthesisBlock,
                QuantaExpressionParser.quantaExpression
        );
    }
}
