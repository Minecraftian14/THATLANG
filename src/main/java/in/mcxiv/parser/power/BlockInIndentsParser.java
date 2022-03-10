package in.mcxiv.parser.power;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.SpacesToken;
import in.mcxiv.utils.Cursors;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;

public class BlockInIndentsParser implements Parser<Node> {

    private final String blockStartingKey;
    private final Parser<?> statement;

    public BlockInIndentsParser(String blockStartingKey, Parser<?> statement) {
        this.blockStartingKey = blockStartingKey;
        this.statement = statement;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        if (!Cursors.matches(string, blockStartingKey)) return null;
        string.moveCursor(blockStartingKey.length());

        Cursors.skipSpaces(string);

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

        RepeatableParser parser = repeatable(either(
                compound(
                        word("\n" + spaces),
                        statement
                ),
                compound(
                        word("\n"),
                        optional(SpacesToken.SpacesParser.spaces),
                        optional(SpacesToken.SpacesParser.spaces) // Added a third so that it makes differentiating either easier.
                )
        ));
        Node node = parser.parse(string);
        if (node == null) return null;

        // remove the last line if it had no statement
        // TODO: instead of checking with SpaceToken **WHICH MIGHT NOT EVEN BE THERE** check if the SVN has only "\n" or "\n   +"
        List<Node> children = new ArrayList<>(node.getChildren());
        Node last = children.get(children.size() - 1);
        if (last.noOfChildren() == 3) {
            String st = "\n";
            if (last.get(1) instanceof SpacesToken spc) st += spc.getValue();
            if (last.get(2) instanceof SpacesToken spc) st += spc.getValue();
            string.moveCursor(-st.length());
        }

        Node block = new Node(parent);
        children.stream() // for all repeat nodes which are compound nodes
                .filter(ch -> ch.noOfChildren() != 3) // Remove those empty lines.
                .map(ch -> ch.get(1)) // now, map those to their second arguments which can either be StatementToken or an empty node by optional
                .forEach(block::addChild);

        return block;
    }
}
