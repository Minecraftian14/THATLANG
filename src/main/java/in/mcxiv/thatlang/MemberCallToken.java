package in.mcxiv.thatlang;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.RepeatableParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.Arrays;

public class MemberCallToken extends Node {

    private final String variableName;
    private final String[] memberChain;

    public MemberCallToken(Node parent, String variableName, String[] memberChain) {
        super(parent);
        this.variableName = variableName;
        this.memberChain = memberChain;
    }

    @Override
    public String toString() {
        return toExtendedString("primer", variableName, "call sequence", Arrays.toString(memberChain), getChildren());
    }

    public String getVariableName() {
        return variableName;
    }

    public String[] getMemberChain() {
        return memberChain;
    }

    public static class MemberCallParser implements Parser<MemberCallToken> {

        private static final CompoundParser parser =
                new CompoundParser(
                        NameToken.NameParser.instance,
                        new RepeatableParser(
                                new LooseSpaceBoundedParser("."),
                                NameToken.NameParser.instance
                        ));

        @Override
        public MemberCallToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if(node==null)return null;
            String variableName = ((NameToken) node.get(0)).getValue();
            Node repeatable = node.get(1);
            String[] memberChain = new String[repeatable.noOfChildren()];
            for (int i = 0; i < repeatable.noOfChildren(); i++)
                memberChain[i] = ((NameToken) repeatable.get(i).get(1)).getValue();
            return new MemberCallToken(parent, variableName, memberChain);
        }
    }
}
