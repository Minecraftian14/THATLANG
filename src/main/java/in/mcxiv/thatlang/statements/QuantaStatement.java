package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.QuantaExpressionToken;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

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
    public THATObject interpret(AbstractVM vm) {
        return token.interpret(vm);
    }

    @Override
    public String toString() {
        return toExtendedString("call seq", token.getChildren());
    }

    public static final class QuantaStatementParser implements Parser<QuantaStatement> {

        public static final QuantaStatementParser quantaStatement = new QuantaStatementParser();

        @Override
        public QuantaStatement __parse__(ParsableString string, Node parent) {
            QuantaExpressionToken node = QuantaExpressionToken.QuantaExpressionParser.quantaExpression.parse(string);
            if (node == null) return null;
            return new QuantaStatement(parent, node);
        }
    }

}
