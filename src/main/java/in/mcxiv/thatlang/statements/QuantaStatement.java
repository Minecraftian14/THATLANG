package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

public class QuantaStatement extends StatementToken {

    QuantaExpressionToken token;

    public QuantaStatement(QuantaExpressionToken token) {
        this.token = token;
    }

    public QuantaStatement(Node parent, QuantaExpressionToken token) {
        super(parent);
        this.token = token;
    }

    public QuantaExpressionToken getToken() {
        return token;
    }

    @Override
    public List<Node> getChildren() {
        return token.getChildren();
    }

    @Override
    public String toString() {
        return toExtendedString(token.getChildren());
    }

    public static final class QuantaStatementParser implements Parser<QuantaStatement> {

        public static final QuantaStatementParser instance = new QuantaStatementParser();

        @Override
        public QuantaStatement __parse__(ParsableString string, Node parent) {
            QuantaExpressionToken node = QuantaExpressionToken.QuantaExpressionParser.instance.parse(string);
            if(node == null) return null;
            return new QuantaStatement(parent, node);
        }
    }

}
