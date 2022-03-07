package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.interpreter.AbstractEnvironment;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.FunctionEvaluator;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.*;
import java.util.function.Supplier;

import static in.mcxiv.parser.power.PowerUtils.*;

public class CollectionsToken extends MultilineStringToken {

    @SuppressWarnings("unchecked")
    public enum CollectionType {
        ARRAY_LIST("[", "]", ExpressionsParser.expression,
                () -> new ArrayList<THATObject>(), (o, n, vm) -> ((ArrayList<THATObject>) o).get(((Number) ((THATObject) n).value).intValue()),
                (o, n, vm) -> ((ArrayList<THATObject>) o).add(((ExpressionsToken) n).interpret(vm))),
        LINKED_LIST("l[", "]", ExpressionsParser.expression,
                () -> new LinkedList<THATObject>(), (o, n, vm) -> ((LinkedList<THATObject>) o).get(((Number) ((THATObject) n).value).intValue()),
                (o, n, vm) -> ((LinkedList<THATObject>) o).add(((ExpressionsToken) n).interpret(vm))),
        HASH_MAP("{", "}", compound(ExpressionsParser.expression, inline(":"), ExpressionsParser.expression),
                () -> new HashMap<THATObject, THATObject>(), (o, n, vm) -> ((HashMap<THATObject, THATObject>) o).get(((THATObject) n)),
                (o, n, vm) -> ((HashMap<THATObject, THATObject>) o).put(((ExpressionsToken) n.get(0)).interpret(vm), ((ExpressionsToken) n.get(2)).interpret(vm))),
        HASH_TABLE("t{", "}", compound(ExpressionsParser.expression, inline(":"), ExpressionsParser.expression),
                () -> new Hashtable<>(), (o, n, vm) -> ((Hashtable<THATObject, THATObject>) o).get(((THATObject) n)),
                (o, n, vm) -> ((Hashtable<THATObject, THATObject>) o).put(((ExpressionsToken) n.get(0)).interpret(vm), ((ExpressionsToken) n.get(2)).interpret(vm))),
        HASH_SET("{", "}", ExpressionsParser.expression,
                () -> new HashSet<THATObject>(), (o, n, vm) -> THOSEObjects.createValue(((HashSet<THATObject>) o).contains(((THATObject) n))),
                (o, n, vm) -> ((HashSet<THATObject>) o).add(((ExpressionsToken) n).interpret(vm))),
        STACK("|", "<", ExpressionsParser.expression,
                () -> new Stack<THATObject>(), (o, n, vm) -> ((Stack<THATObject>) o).get(((Number) ((THATObject) n).value).intValue()),
                (o, n, vm) -> ((Stack<THATObject>) o).push(((ExpressionsToken) n).interpret(vm)));

        public static final Parser<?> collections = either(Arrays.stream(values()).map(collectionType -> collectionType.parser).toArray(Parser[]::new));

        public final String starter;
        public final String ender;
        public final Parser<?> elementParser;
        public final Supplier<Object> creator;
        public final TriConsumer<Object, Object, AbstractVM, THATObject> accessor;
        public final TriConsumer<Object, Node, AbstractVM, ?> visitor;

        public final CollectionsParser parser;

        CollectionType(String starter, String ender, Parser<?> elementParser, Supplier<Object> creator, TriConsumer<Object, Object, AbstractVM, THATObject> accessor, TriConsumer<Object, Node, AbstractVM, ?> visitor) {
            this.starter = starter;
            this.ender = ender;
            this.elementParser = elementParser;
            this.creator = creator;
            this.accessor = accessor;
            this.visitor = visitor;
            this.parser = new CollectionsParser(this);
        }

        public void addAll(Object collection, List<Node> content, AbstractVM vm) {
            for (Node child : content)
                visitor.consume(collection, child, vm);
        }

        public THATObject access(Object collection, Object key, AbstractVM vm) {
            return accessor.consume(collection, key, vm);
        }
    }

    private final CollectionType type;

    public CollectionsToken(CollectionType type, Node[] children) {
        this(null, type, children);
    }

    public CollectionsToken(Node parent, CollectionType type, Node[] children) {
        super(parent, "");
        this.type = type;
        for (Node child : children) addChild(child);
    }

    @Override
    public String toString() {
        return toExtendedString("type", type.name(), "values", getChildren());
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        Object collection = type.creator.get();
        THATObject object = THOSEObjects.createValue(collection);
        type.addAll(collection, getChildren(), vm);
        object.toStringFunction = o -> collectionToStringPort(type, o.value);
        object.addFunction(new SpliceFunction(vm.executionEnvironment, collection, type));
        return object;
    }

    public static String collectionToStringPort(CollectionType type, Object collection) {
        String string = collection.toString();
        string = string.substring(1, string.length() - 1);
        return type.starter + string + type.ender;
    }

    public static final class CollectionsParser extends MultilineStringToken.MultilineStringParser {

        private final CollectionType type;

        private final TupleToken.TupleParser<?, StringValueNode, ? extends TupleToken<?>> parser;

        private CollectionsParser(CollectionType type) {
            super(type.starter, type.ender);
            this.type = type;
            parser = new TupleToken.TupleParser<>(type.elementParser, word(","));
        }

        @Override
        public CollectionsToken __parse__(ParsableString string, Node parent) {
            MultilineStringToken token = super.__parse__(string, null);
            if (token == null) return null;

            ParsableString substring = new ParsableString(token.getValue().trim());
            TupleToken<?> node = parser.parse(substring);
            if (node == null) return null;

            return new CollectionsToken(parent, type, node.getItems().stream().map(n -> (Node) n).toArray(Node[]::new));
        }
    }

    public interface TriConsumer<A, B, C, R> {
        R consume(A o, B n, C vm);
    }

    private static class SpliceFunction extends FunctionEvaluator {

        final Object collection;
        final CollectionType type;

        public SpliceFunction(AbstractEnvironment environment, Object collection, CollectionType type) {
            super(environment);
            this.collection = collection;
            this.type = type;
        }

        @Override
        public boolean isApplicable(FunctionCallToken fct) {
            return "__splice__".equals(fct.getValue());
        }

        @Override
        public THATObject apply(FunctionCallToken fct) {
            return type.access(collection, fct.getArguments().getExpressions().get(0).interpret(environment.vm), environment.vm);
        }
    }

}
