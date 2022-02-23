package in.mcxiv.thatlang.parser.natives;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.AssignmentToken.AssignmentParser;

import static in.mcxiv.thatlang.parser.power.PowerUtils.compound;
import static in.mcxiv.thatlang.parser.power.PowerUtils.word;

public class CollectionsToken extends Node {

    enum CollectionType {
        ARRAY_LIST("[", "]", ExpressionsParser.expression),
        LINKED_LIST("l[", "]", ExpressionsParser.expression),
        HASH_MAP("{", "}", AssignmentParser.assignment),
        HASH_SET("{", "}", ExpressionsParser.expression);

        public final String starter;
        public final String ender;
        public final Parser<?> elementParser;

        CollectionType(String starter, String ender, Parser<?> elementParser) {
            this.starter = starter;
            this.ender = ender;
            this.elementParser = elementParser;
        }
    }

    private final CollectionType type;

    public CollectionsToken(CollectionType type, Node[] children) {
        this(null, type, children);
    }

    public CollectionsToken(Node parent, CollectionType type, Node[] children) {
        super(parent);
        this.type = type;
        for (Node child : children) addChild(child);
    }

    public static final class CollectionsParser implements Parser<CollectionsToken> {

        private final CollectionType type;

        private final Parser<Node> parser;

        private CollectionsParser(CollectionType type) {
            this.type = type;
            parser = compound(
                    word(type.starter),
                    // impl `type.parser,type.parser,type.parser` pasrer

                    word(type.ender)
            );
        }

        @Override
        public CollectionsToken __parse__(ParsableString string, Node parent) {
            return null;
        }
    }

}
