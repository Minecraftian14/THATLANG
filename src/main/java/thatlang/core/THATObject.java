package thatlang.core;

import in.mcxiv.thatlang.parser.expression.FunctionCallToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class THATObject {

    public Class<?> primaryInference;
    ArrayList<Class<?>> secondaryInferences = new ArrayList<>();

    public String name = "loaded from memory";
    public Object value;
    HashMap< /*DATA_KEY*/ String, /*DATA_VALUE*/ Object> objectData = new HashMap<>();

    HashMap< /*NAME*/ String, /*OBJECT*/ THATObject> accessibleMember = new HashMap<>();

    public THATObject seekMember(String name) {
        return accessibleMember.getOrDefault(name, null);
    }

    public THATObject seekFunction(FunctionCallToken name) {
        // TODO
        return null;
    }

    public String v() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        THATObject that = (THATObject) o;
        // TODO
        if (primaryInference == String.class && that.primaryInference == String.class)
            return value.equals(that.value);
        if (primaryInference == that.primaryInference)
            return value.equals(that.value);
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryInference, secondaryInferences, name, value, objectData, accessibleMember);
    }
}
