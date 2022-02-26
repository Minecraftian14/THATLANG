package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ArgumentsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.parser.power.PowerUtils;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.NameToken.NameParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.ArrayList;

public class BinaryFunctionStatementToken {

    public static final Parser<QuantaStatement> binaryFunctionStmt = new Parser<>() {

        private static final Parser<Node> parser = PowerUtils.compound(
                ExpressionsParser.expression,
                PowerUtils.inline(NameParser.name),
                ExpressionsParser.expression
        );

        @Override
        public QuantaStatement __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            var list = new ArrayList<ExpressionsToken>();
            list.add((ExpressionsToken) node.get(0));
            list.add((ExpressionsToken) node.get(2));
            return new QuantaStatement(parent, new QuantaExpressionToken(new Node[]{
                    new FunctionCallToken(node.getExp(NameToken.class).getValue(),
                            new ArgumentsToken(list))
            }));
        }
    };

}
