package in.mcxiv.thatlang.blocks;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.*;
import in.mcxiv.thatlang.parser.tokens.SpacesToken;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

class IndentedBlockToken extends BlockToken {

    public IndentedBlockToken(Node... statements) {
        super(statements);
    }

    public static class IndentedBlockParser implements Parser<IndentedBlockToken> {

        public static final IndentedBlockParser instance = new IndentedBlockParser();

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

            String spaces = string.subSequencePS(lastOccurrenceOfNewLine+1, string.getCursor()-1).toStringValue();

            string.setCursor(fallBack);

            RepeatableParser parser = new RepeatableParser(new EitherParser(
                    new CompoundParser(
                            new WordParser("\n" + spaces),
                            new StatementToken.StatementParser()
                    ),
                    new CompoundParser(
                            new WordParser("\n"),
                            new OptionalParser(SpacesToken.SpacesParser.instance)
                    )
            ));
            Node node = parser.parse(string);
            if (node == null) return null;

            IndentedBlockToken token = new IndentedBlockToken();
            node.getChildren().stream() // for all repeat nodes which are compound nodes
                    .map(ch -> ch.get(0)) // map them to their first arguments which are also compound nodes
                    .map(ch -> ch.get(1)) // now, map those to their second arguments which can either be StatementToken or an empty node by optional
                    .forEach(token::addChild);

//            IntStream.range(0, node.noOfChildren() / 2)
//                    .map(i -> 2 * i)
//                    .mapToObj(node::get)
//                    .forEach(token::addChild);
            parent.addChild(token);
            return token;
        }
    }
}