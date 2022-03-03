package in.mcxiv.thatlang.blocks;

import in.mcxiv.thatlang.blocks.BracedBlockToken.BracedBlockParser;
import in.mcxiv.thatlang.blocks.IndentedBlockToken.IndentedBlockParser;
import in.mcxiv.thatlang.blocks.SingleStatementBlockToken.SingleStatementBlockParser;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.statements.StatementToken;

public class BlockToken extends Node {

    public BlockToken(StatementToken... statements) {
        for (int i = 0; i < statements.length; i++) {
            StatementToken stmt = statements[i];
            addChild(stmt);
            if (stmt.isCondensable())
                for (++i; i < statements.length; i++) {
                    if (stmt.isAccepted(statements[i]))
                        stmt.processCondensability(statements[i]);
                    else {
                        --i;
                        break;
                    }
                }
        }
//        for (Node node : statements) addChild(node);
    }

    @Override
    public String toString() {
        return toExtendedString("statements", getChildren());
    }

    public StatementToken[] getStatements() {
        return getChildren(StatementToken.class).toArray(new StatementToken[0]);
    }

    public static final class BlockParser implements Parser<BlockToken> {

        public static final BlockParser block = new BlockParser();

        private BlockParser() {
        }

        private static final EitherParser parser = new EitherParser(
                IndentedBlockParser.indentedBlock,
                SingleStatementBlockParser.singleStatementBlock,
                BracedBlockParser.bracedBlock
        );

        @Override
        public BlockToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string, parent);
            if (node == null) return null;
            return (BlockToken) node;
        }
    }
}
