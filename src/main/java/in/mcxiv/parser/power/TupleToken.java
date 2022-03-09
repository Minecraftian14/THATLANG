package in.mcxiv.parser.power;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;

import java.util.ArrayList;
import java.util.function.Function;

import static in.mcxiv.parser.power.PowerUtils.*;

/**
 * IT"S NOT THE SAME TUPLE AS SEEN IN PYTHON
 * <p>
 * It's just used to parser a 'something' separated by 'something' list
 */
public class TupleToken<Item extends Node> extends Node {

    final ArrayList<Item> items;

    public TupleToken(ArrayList<Item> items) {
        this(null, items);
    }

    public TupleToken(Node parent, ArrayList<Item> items) {
        super(parent);
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public static class TupleParser<Item extends Node, Separator extends Node, Parsable extends TupleToken<Item>> implements Parser<Parsable> {

        final Parser<Item> itemParser;
        final Parser<Separator> separatorParser;
        final Function<Parser<?>, Parser<?>> wrapper;
        private final Parser<Node> parser;

        public TupleParser(Parser<Item> itemParser, Parser<Separator> separatorParser) {
            this(itemParser, separatorParser, PowerUtils::inline);
        }

        public TupleParser(Parser<Item> itemParser, Parser<Separator> separatorParser, Function<Parser<?>, Parser<?>> wrapper) {
            this.itemParser = itemParser;
            this.separatorParser = separatorParser;
            this.wrapper = wrapper;
            parser = compound(
                    this.itemParser,
                    optional(repeatable(compound(wrapper.apply(separatorParser), itemParser)))
            );
        }

        @Override
        @SuppressWarnings("unchecked")
        public Parsable __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;

            ArrayList<Item> items = new ArrayList<>();
            items.add((Item) node.get(0));

            if (node.noOfChildren() > 1)
                node.get(1).getChildren().stream()
                        .map(ch -> ch.get(1))
                        .map(ch -> ((Item) ch))
                        .forEach(items::add);
            return (Parsable) new TupleToken<>(parent, items);
        }

        @SuppressWarnings("unchecked")
        public Parsable cast(Node node) {
            return (Parsable) node;
        }
    }

}
