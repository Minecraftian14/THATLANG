package in.mcxiv.external.parsers.yaml;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.external.parsers.yaml.primitive.DocumentToken;
import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.CollectionsToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.List;

import static in.mcxiv.thatlang.natives.CollectionsToken.collectionToStringPort;

public class YAMLNode extends SimpleNestableToken implements Interpretable<AbstractVM, THATObject> {

    public YAMLNode(Node parent, Node... children) {
        super(parent, children);
    }

    public int noOfDocuments() {
        return noOfChildren();
    }

    public DocumentToken getDocument(int index) {
        return ((DocumentToken) get(index));
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        List<Node> values = getChildren();
        var type = CollectionsToken.CollectionType.ARRAY_LIST;
        Object collection = type.creator.get();
        THATObject object = THOSEObjects.createValue(collection);
        type.addAll(collection, values, vm);
        object.toStringFunction = o -> collectionToStringPort(type, o.value);
        object.addFunction(new CollectionsToken.SpliceFunction(vm.executionEnvironment, collection, type));
        return object;
    }
}
