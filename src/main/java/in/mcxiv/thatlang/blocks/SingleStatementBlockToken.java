package in.mcxiv.thatlang.blocks;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

import static in.mcxiv.thatlang.statements.StatementToken.StatementParser.statement;

class SingleStatementBlockToken extends BlockToken {

    public SingleStatementBlockToken(StatementToken statement) {
        super(statement);
    }

    public SingleStatementBlockToken(Node parent, StatementToken statement) {
        super(statement);
        if (parent != null) parent.addChild(this);
    }

    public static class SingleStatementBlockParser implements Parser<SingleStatementBlockToken> {

        public static final SingleStatementBlockParser singleStatementBlock = new SingleStatementBlockParser();

        private SingleStatementBlockParser() {
        }

        @Override
        public SingleStatementBlockToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '-') return null;
            if (Cursors.getCharAndNext(string) != '>') return null;

            while (Cursors.bound(string) && Cursors.isBlank(string)) string.moveCursor(1);

            StatementToken node = statement.parse(string);
            if (node == null) return null;

            // We gotta see if there are any condensable statements ahead...
            if (node.isCondensable()) while (Cursors.bound(string)) {
                int backup = string.getCursor();
                while (Cursors.bound(string) && Cursors.isBlank(string)) string.moveCursor(1);
                StatementToken possibility = statement.parse(string);
                if (node.isAccepted(possibility))
                    node.processCondensability(possibility);
                else {
                    string.setCursor(backup);
                    break;
                }
            }

            return new SingleStatementBlockToken(parent, node);
        }
    }
}