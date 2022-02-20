package in.mcxiv.interp;

import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;

public class VariableScope {

    ArrayList<THATObject> variables;

    public VariableScope() {
        variables = new ArrayList<>();
    }

    public void newVariable(String type, String name, String eval) {
        variables.add(THOSEObjects.create(type, name, eval));
    }

    public THATObject seek(String name) {
        return variables.stream().filter(variable -> variable.name.equals(name)).findFirst().orElse(null);
    }

}
