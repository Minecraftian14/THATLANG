package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.RawLineToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.power.BlockInIndentsParser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

public class MultilineStringToken extends StringValueNode implements Interpretable<AbstractVM, THATObject> {

    public MultilineStringToken(String value) {
        super(value);
    }

    public MultilineStringToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public THATObject interpret(AbstractVM abstractVM) {
        return THOSEObjects.createValue(getValue());
    }

    public static final class MultilineStringParser implements Parser<MultilineStringToken> {

        public static final Parser<MultilineStringToken> multilineString = new MultilineStringParser();

        private static final Parser<?> parser = new BlockInIndentsParser(">", RawLineToken.RawLineParser.rawLine);

        private MultilineStringParser() {
        }

        @Override
        public MultilineStringToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            StringBuilder builder = new StringBuilder();
            node.forEachChild(n -> builder.append(((RawLineToken) n).getValue()).append(" "));
            // TODO: remove the extra spaces before each line...
            return new MultilineStringToken(parent, builder.toString().strip());
        }
    }

}
