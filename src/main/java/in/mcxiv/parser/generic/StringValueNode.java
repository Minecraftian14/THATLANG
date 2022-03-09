package in.mcxiv.parser.generic;

import in.mcxiv.parser.Node;

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
        return "\"StringValueNode\":\"" + value + "\"";
    }
}
