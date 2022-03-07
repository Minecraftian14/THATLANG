package thatlang.core;

import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.interpreter.FunctionEvaluator;
import thatlang.core.util.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class THATObject {

    public Class<?> primaryInference;
    ArrayList<Class<?>> secondaryInferences = new ArrayList<>();

    public String name = "loaded from memory";
    public Object value;
    HashMap< /*DATA_KEY*/ String, /*DATA_VALUE*/ Object> objectData = new HashMap<>();

    HashMap< /*NAME*/ String, /*OBJECT*/ THATObject> accessibleMember = new HashMap<>();
    List<FunctionEvaluator> accessibleFunctions = new ArrayList<>();

    public Function<THATObject, String> toStringFunction = object -> "" + object.value;

    public Object getObjectData(String dataKey) {
        return objectData.get(dataKey);
    }

    public <T> T getObjectData(String dataKey, Class<T> dataClass) {
        return dataClass.cast(objectData.get(dataKey));
    }

    public void putObjectData(String dataKey, Object dataValue) {
        objectData.put(dataKey, dataValue);
    }

    public THATObject getMember(String memberName) {
        return switch (memberName) {
            case "type" -> THOSEObjects.createValue(primaryInference.getSimpleName());
            default -> accessibleMember.get(memberName);
        };
    }

    public void putMember(String memberName, THATObject member) {
        accessibleMember.put(memberName, member);
    }

    public void putMember(THATObject member) {
        accessibleMember.put(member.name, member);
    }

    public THATObject getPossiblyNewMember(String memberName) {
        THATObject member = getMember(memberName);
        if (member != null) return member;
        member = THOSEObjects.createValue(null);
        return member;
    }

    public void addFunction(FunctionEvaluator evaluator) {
        accessibleFunctions.add(evaluator);
    }

    public THATObject seekFunction(FunctionCallToken fct) {

        for (FunctionEvaluator evaluator : accessibleFunctions)
            if (evaluator.isApplicable(fct))
                return evaluator.apply(fct);

        for (FunctionEvaluator evaluator : accessibleFunctions)
            if (evaluator.isDigestible(fct))
                return evaluator.apply(fct);

        return THOSEObjects.NULL;
    }

    public String v() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        THATObject that = (THATObject) o;
        // TODO
        if (primaryInference == String.class && that.primaryInference == String.class)
            return value.equals(that.value);
        if (Number.class.isAssignableFrom(primaryInference) && Number.class.isAssignableFrom(that.primaryInference))
            return ((Number) value).doubleValue() == ((Number) that.value).doubleValue();
        if (primaryInference == that.primaryInference) // TODO
            return value.equals(that.value);
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryInference, secondaryInferences, name, value, objectData, accessibleMember);
    }

    @Override
    public String toString() {
        return toStringFunction.apply(this);
    }

    public Object printSafe() {
        if (primaryInference == null) return toStringFunction.apply(this);
        if (Types.isPrimitiveType(primaryInference)) return value;
        if (Types.isNativeType(primaryInference)) return toStringFunction.apply(this);
        throw new IllegalStateException("Hmmm... Then what kind of a type is " + primaryInference + "??");
    }
}
