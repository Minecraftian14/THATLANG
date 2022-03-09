package in.mcxiv.external.parsers.json;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.external.parsers.json.primitive.ValueToken;
import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

public class JSONNode extends SimpleNestableToken implements Interpretable<AbstractVM, THATObject> {

    private final Node tree;

    public JSONNode(Node tree) {
        this(null, tree);
    }

    public JSONNode(Node parent, Node tree) {
        super(parent);
        this.tree = tree;
        addChild(tree);
    }

    @Override
    public String toString() {
        return toExtendedString("tree", tree);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return ((ValueToken) tree).interpret(vm);
    }

}
