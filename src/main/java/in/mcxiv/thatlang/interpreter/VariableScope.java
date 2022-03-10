package in.mcxiv.thatlang.interpreter;

import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.HashMap;
import java.util.function.Consumer;

public class VariableScope {

    HashMap<String, THATObject> variables;

    public VariableScope() {
        variables = new HashMap<>();
    }

    public void newVariable(String type, String name, String eval) {
        variables.put(name, THOSEObjects.create(type, name, eval));
    }

    public void addVariable(THATObject object) {
        variables.put(object.name, object);
    }

    public THATObject seek(String name) {
        return variables.get(name);
    }

    public void foreach(Consumer<THATObject> consumer) {
        variables.forEach((s, object) -> consumer.accept(object));
    }
}
