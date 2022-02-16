package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;

public class MemberCallToken extends StringValueNode {

    public MemberCallToken(String value) {
        super(value);
    }

    public MemberCallToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return toExtendedString("member name", getValue());
    }

    public static class MemberCallParser implements Parser<MemberCallToken> {

        public static final MemberCallParser instance = new MemberCallParser();

        private static final NameToken.NameParser parser = NameToken.NameParser.instance;

        private MemberCallParser() {
        }

        @Override
        public MemberCallToken __parse__(ParsableString string, Node parent) {
            NameToken node = parser.parse(string);
            if (node == null) return null;
            return new MemberCallToken(parent, node.getValue());
        }
    }

}
