package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.MultiAssignmentToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

import java.util.List;

public class MultiAssignmentStatement extends StatementToken {

    MultiAssignmentToken token;

    public MultiAssignmentStatement(MultiAssignmentToken token) {
        this.token = token;
    }

    public MultiAssignmentStatement(Node parent, MultiAssignmentToken token) {
        super(parent);
        this.token = token;
    }

    public MultiAssignmentToken getToken() {
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

    public static final class MultiAssignmentStatementParser implements Parser<MultiAssignmentStatement> {

        public static final MultiAssignmentStatementParser multiAssignment = new MultiAssignmentStatementParser();

        @Override
        public MultiAssignmentStatement __parse__(ParsableString string, Node parent) {
            var node = MultiAssignmentToken.MultiAssignmentParser.multiAssignment.parse(string);
            if (node == null) return null;
            return new MultiAssignmentStatement(parent, node);
        }
    }
}
