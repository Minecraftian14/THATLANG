package in.mcxiv.thatlang.blocks;

import in.mcxiv.thatlang.blocks.BracedBlockToken.BracedBlockParser;
import in.mcxiv.thatlang.blocks.IndentedBlockToken.IndentedBlockParser;
import in.mcxiv.thatlang.blocks.SingleStatementBlockToken.SingleStatementBlockParser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;

public class BlockToken extends Node {

    public BlockToken(Node... statements) {
        for (Node node : statements) addChild(node);
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
