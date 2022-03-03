package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.Node;

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

        public static final MemberCallParser member = new MemberCallParser();

        private static final NameToken.NameParser parser = NameToken.NameParser.name;

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
