package in.mcxiv.thatlang.blocks;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.power.RepeatableParser;
import in.mcxiv.thatlang.parser.tokens.SpacesToken;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

class IndentedBlockToken extends BlockToken {

    public IndentedBlockToken(StatementToken... statements) {
        super(statements);
    }

    public static class IndentedBlockParser implements Parser<IndentedBlockToken> {

        public static final IndentedBlockParser indentedBlock = new IndentedBlockParser();

        @Override
        public IndentedBlockToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != ':') return null;

            while (Cursors.bound(string) && Cursors.isSpace(string)) string.moveCursor(1);

            if (Cursors.getChar(string) != '\n') return null;

            // We only want to take a peek at the knd of spacing ahead, but using space parser will move the cursor ahead, so we need to reset it.
            int fallBack = string.getCursor();
            string.moveCursor(1); // skip the \n

            int lastOccurrenceOfNewLine = fallBack;

            while (Cursors.bound(string)) {
                char c = Cursors.getCharAndNext(string);
                if (c == ' ' || c == '\t') continue;
                if (c == '\n' || c == '\r')
                    lastOccurrenceOfNewLine = string.getCursor() - 1;
                else /* if c is some code */
                    break;
            }

            String spaces = string.subSequencePS(lastOccurrenceOfNewLine + 1, string.getCursor() - 1).toStringValue();

            string.setCursor(fallBack);

            RepeatableParser parser = repeatable(new EitherParser(
                    compound(
                            word("\n" + spaces),
                            new StatementToken.StatementParser()
                    ),
                    compound(
                            word("\n"),
                            optional(SpacesToken.SpacesParser.spaces)
                    )
            ));
            Node node = parser.parse(string);
            if (node == null) return null;

            // remove the last line if it had no statement
            // TODO: instead of checking with SpaceToken **WHICH MIGHT NOT EVEN BE THERE** check if the SVN has only "\n" or "\n   +"
            List<Node> children = new ArrayList<>(node.getChildren());
            if (children.get(children.size() - 1).get(1) instanceof SpacesToken st) {
                string.moveCursor(-st.getValue().length() - 1);
                children.remove(children.size() - 1);
            }

            IndentedBlockToken token = new IndentedBlockToken(
                    children.stream() // for all repeat nodes which are compound nodes
                            .map(ch -> ch.get(1)) // now, map those to their second arguments which can either be StatementToken or an empty node by optional
                            .filter(ch -> ch instanceof StatementToken)
                            .toArray(StatementToken[]::new)
            );

            if (parent != null)
                parent.addChild(token);
            return token;
        }
    }
}