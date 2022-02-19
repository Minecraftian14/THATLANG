package thatlang.core;

import thatlang.core.THATObject;
import thatlang.core.util.Types;

public final class THOSEObjects {

    public static final String DATA_KEY_CONSTRUCTION_TYPE = "construction type";
    public static final String DATA_VALUE_CONSTRUCTION_TYPE_VALUE = "ct value";
    public static final String DATA_VALUE_CONSTRUCTION_TYPE_VARIABLE = "ct value";

    private THOSEObjects() {
    }

    public static THATObject create(Object value) {
        THATObject object = new THATObject();

        object.value = value;
        if (Types.isNativeType(value.getClass()))
            object.primaryInference = value.getClass();
        else {
            object.primaryInference = null;
            object.secondaryInferences.add(value.getClass());
        }

        return object;
    }

    public static THATObject createValue(Object value) {
        THATObject object = create(value);
        object.objectData.put(DATA_KEY_CONSTRUCTION_TYPE, DATA_VALUE_CONSTRUCTION_TYPE_VALUE);
        return object;
    }

    public static THATObject createVariable(Object value) {
        THATObject object = create(value);
        object.objectData.put(DATA_KEY_CONSTRUCTION_TYPE, DATA_VALUE_CONSTRUCTION_TYPE_VARIABLE);
        return object;
    }

}
