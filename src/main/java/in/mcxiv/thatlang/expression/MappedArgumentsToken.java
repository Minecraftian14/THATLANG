package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken.NameParser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.expression.ExpressionsToken.ExpressionsParser;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;

public class MappedArgumentsToken extends Node {

    MappingsToken[] mappings;
    ExpressionsToken[] args;

    public MappedArgumentsToken(MappingsToken[] mappings, ExpressionsToken[] args) {
        this(null, mappings, args);
    }

    public MappedArgumentsToken(Node parent, MappingsToken[] mappings, ExpressionsToken[] args) {
        super(parent);
        this.mappings = mappings;
        this.args = args;
    }

    @Override
    public String toString() {
        return toExtendedString("mappings", mappings, "args", args);
    }

    public MappingsToken[] getMappings() {
        return mappings;
    }

    public ExpressionsToken[] getArgs() {
        return args;
    }

    public List<ExpressionsToken> getExpressions() {
        return List.of(args);
    }

    public ExpressionsToken acquire(String name) {
        for (MappingsToken mapping : mappings)
            if (mapping.name.equals(name))
                return mapping.expression;
        return null;
    }

    public ExpressionsToken[] resolve(String... names) {
        ExpressionsToken[] tokens = new ExpressionsToken[names.length];

        for (int i = 0; i < names.length; i++)
            tokens[i] = acquire(names[i]);

        for (int i = 0, j = 0; i < tokens.length && j < args.length; i++)
            if(tokens[i] == null) tokens[i] = args[j++];

        return tokens;
    }

    public static class MappedArgumentsParser implements Parser<MappedArgumentsToken> {

        public static final Parser<MappedArgumentsToken> mappedArguments = new MappedArgumentsParser();

        private final TupleToken.TupleParser<Node, StringValueNode, TupleToken<Node>> parser = new TupleToken.TupleParser<>(either(MappingsToken.MappingsParser.mapping, ExpressionsParser.expression), word(","));

        private MappedArgumentsParser() {
        }

        @Override
        public MappedArgumentsToken __parse__(ParsableString string, Node parent) {
            TupleToken<Node> node = parser.parse(string);
            if (node == null) return null;
            ArrayList<Node> items = node.getItems();
            ArrayList<MappingsToken> mappings = new ArrayList<>();
            ArrayList<ExpressionsToken> args = new ArrayList<>();
            for (Node item : items) {
                if (item instanceof MappingsToken mt) {
                    mappings.add(mt);
//                    args.add(mt.expression);
                } else if (item instanceof ExpressionsToken et)
                    args.add(et);
            }
            return new MappedArgumentsToken(parent, mappings.toArray(MappingsToken[]::new), args.toArray(ExpressionsToken[]::new));
        }
    }

    public static class MappingsToken extends Node {
        String name;
        ExpressionsToken expression;

        public MappingsToken(String name, ExpressionsToken expression) {
            this(null, name, expression);
        }

        public MappingsToken(Node parent, String name, ExpressionsToken expression) {
            super(parent);
            this.name = name;
            this.expression = expression;
        }

        public String getName() {
            return name;
        }

        public ExpressionsToken getExpression() {
            return expression;
        }

        public static class MappingsParser implements Parser<MappingsToken> {

            public static final Parser<MappingsToken> mapping = new MappingsParser();

            private static final Parser<?> parser = compound(NameParser.name, inline("="), ExpressionsParser.expression);

            private MappingsParser() {
            }

            @Override
            public MappingsToken __parse__(ParsableString string, Node parent) {
                Node node = parser.parse(string);
                if (node == null) return null;
                return new MappingsToken(parent, ((StringValueNode) node.get(0)).getValue(), ((ExpressionsToken) node.get(2)));
            }
        }
    }

}