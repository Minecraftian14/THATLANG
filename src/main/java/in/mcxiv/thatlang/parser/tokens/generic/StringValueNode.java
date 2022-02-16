package in.mcxiv.thatlang.parser.tokens.generic;

import in.mcxiv.thatlang.parser.tree.Node;

public class StringValueNode extends Node {

    private String value;

    public StringValueNode(String value) {
        this(null, value);
    }

    public StringValueNode(Node parent, String value) {
        super(parent);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return toExtendedString("value", value);
    }
}
