package in.mcxiv.thatlang.blocks;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

class SingleStatementBlockToken extends BlockToken {

    public SingleStatementBlockToken(Node statement) {
        super(statement);
    }

    public SingleStatementBlockToken(Node parent, Node statement) {
        super(statement);
        parent.addChild(this);
    }

    public static class SingleStatementBlockParser implements Parser<SingleStatementBlockToken> {

        public static final SingleStatementBlockParser singleStatementBlock = new SingleStatementBlockParser();

        private SingleStatementBlockParser() {
        }

        @Override
        public SingleStatementBlockToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '-') return null;
            if (Cursors.getCharAndNext(string) != '>') return null;

            while (Cursors.bound(string) && Cursors.isWhite(string)) string.moveCursor(1);

            StatementToken node = StatementToken.StatementParser.statement.parse(string);
            if (node == null) return null;

            return new SingleStatementBlockToken(parent, node);
        }
    }
}