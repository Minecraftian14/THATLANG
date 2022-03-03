package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.Node;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.word;

public class ArgumentsToken extends TupleToken<ExpressionsToken> {

    public static final Parser<ArgumentsToken> arguments = new Parser<>() {
        private final TupleParser<ExpressionsToken, StringValueNode, TupleToken<ExpressionsToken>> parser = new TupleParser<>(ExpressionsParser.expression, word(","));

        @Override
        public ArgumentsToken __parse__(ParsableString string, Node parent) {
            TupleToken<ExpressionsToken> node = parser.parse(string);
            if (node == null) return null;
            return new ArgumentsToken(parent, node.getItems());
        }
    };

    public ArgumentsToken() {
        this(new ArrayList<>());
    }

    public ArgumentsToken(ArrayList<ExpressionsToken> expressions) {
        this(null, expressions);
    }

    public ArgumentsToken(Node parent, ArrayList<ExpressionsToken> expressions) {
        super(parent, expressions);
        for (ExpressionsToken expression : expressions)
            addChild(expression);
    }

    @Override
    public String toString() {
        return toExtendedString("arguments", getItems());
    }

    public List<ExpressionsToken> getExpressions() {
        return getItems();
    }
}