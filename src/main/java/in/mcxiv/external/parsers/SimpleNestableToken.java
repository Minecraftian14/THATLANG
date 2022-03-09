package in.mcxiv.external.parsers;

import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

public class SimpleNestableToken extends Node implements Interpretable<AbstractVM, THATObject> {
    public SimpleNestableToken(Node parent, Node... children) {
        super(parent);
        for (Node child : children) addChild(child);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.NULL;
    }
}
