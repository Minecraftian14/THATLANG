package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;

public class StatementToken extends SimpleNestableToken {
    public StatementToken(Node parent, Node... children) {
        super(parent, children);
    }

    public static final class StatementParser implements Parser<StatementToken> {

        public static final Parser<StatementToken> statement = new StatementParser();

        @Override
        public StatementToken __parse__(ParsableString string, Node parent) {
            return null;
        }
    }

}
