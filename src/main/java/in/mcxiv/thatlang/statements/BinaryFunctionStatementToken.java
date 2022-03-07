package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.*;
import in.mcxiv.thatlang.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.parser.power.PowerUtils;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.NameToken.NameParser;
import in.mcxiv.parser.Node;

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
                            new MappedArgumentsToken(new MappedArgumentsToken.MappingsToken[0], list.toArray(ExpressionsToken[]::new)))
            }));
        }
    };

}
