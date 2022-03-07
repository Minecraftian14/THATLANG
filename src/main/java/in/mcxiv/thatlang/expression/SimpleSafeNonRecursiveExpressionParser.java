package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NumeralToken.NumeralParser;
import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.thatlang.expression.NumeralExpressionToken.NumeralExpressionParser;
import in.mcxiv.thatlang.expression.QuantaExpressionToken.QuantaExpressionParser;
import in.mcxiv.thatlang.natives.ExternalCodeToken.ExternalCodeParser;
import in.mcxiv.thatlang.natives.MultilineStringToken.MultilineStringParser;
import in.mcxiv.thatlang.natives.StringToken.StringParser;

public class SimpleSafeNonRecursiveExpressionParser extends EitherParser {

    public static final Parser<Node> safeExpression = new SimpleSafeNonRecursiveExpressionParser();

    private SimpleSafeNonRecursiveExpressionParser() {
        super(
                MultilineStringParser.multiLineString,
                ExternalCodeParser.externalCode,
                StringParser.string,
                NumeralExpressionParser.numericalExpr,
                ParenthesisParser.parenthesisBlock,
                QuantaExpressionParser.quantaExpression
        );
    }
}
