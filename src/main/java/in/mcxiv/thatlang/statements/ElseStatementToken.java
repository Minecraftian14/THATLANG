package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.Node;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;
import static in.mcxiv.parser.generic.SpacesToken.SpacesParser.spaces;

public class ElseStatementToken extends StatementToken {

    public ElseStatementToken(StatementToken[] statements) {
        this(null, statements);
    }

    public ElseStatementToken(Node parent, StatementToken[] statements) {
        super(parent);
        for (Node statement : statements) addChild(statement);
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    @Override
    public String toString() {
        return toExtendedString("statements", getChildren());
    }

    public static class ElseStatementParser implements Parser<ElseStatementToken> {

        public static final ElseStatementParser elseStatement = new ElseStatementParser();

        private static final Parser<Node> parser = compound(
                either(word("else"), compound(word("or"), spaces, word("else"))),
                optional(spaces),
                BlockToken.BlockParser.block
        );

        @Override
        public ElseStatementToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            var bdy = node.getExp(BlockToken.class);
            return new ElseStatementToken(parent, bdy.getStatements());
        }
    }
}
