package in.mcxiv.thatlang;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.RepeatableParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tokens.SpacesToken;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

public class IndentedBlockToken extends Node {

    public IndentedBlockToken(Node... statements) {
        for (Node node : statements) addChild(node);
    }

    @Override
    public String toString() {
        return toExtendedString("statements", getChildren());
    }

    public StatementToken[] getStatements() {
        return getChildren().stream().filter(node -> node instanceof StatementToken)
                .map(node -> ((StatementToken) node))
                .toArray(StatementToken[]::new);
    }

    public static class IndentedBlockParser implements Parser<IndentedBlockToken> {

        public static final IndentedBlockParser instance = new IndentedBlockParser();

        @Override
        public IndentedBlockToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getChar(string) != '\n') return null;

            // We only want to take a peek at the knd of spacing ahead, but using space parser will move the cursor ahead, so we need to reset it.
            int fallBack = string.getCursor();
            string.moveCursor(1); // skip the \n

            SpacesToken spacesToken = SpacesToken.SpacesParser.instance.parse(string);
            if (spacesToken == null) return null;
            string.setCursor(fallBack);
            String spaces = spacesToken.getValue();
            if (spaces.length() == 0)
                return null; // FIXME: 11-02-2022 I think this thing is not even possible... There will always be at least one space :thinking:

            RepeatableParser parser = new RepeatableParser(
                    new WordParser("\n" + spaces),
                    new StatementToken.StatementParser()
            );
            Node node = parser.parse(string);
            if (node == null) return null;

            IndentedBlockToken token = new IndentedBlockToken();
            node.getChildren().stream().map(ch -> ch.get(1))
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