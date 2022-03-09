package thatlang.core;

import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.util.Types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class THOSEObjects {

    public static final THATObject NULL = create("val", "null", "null");

    public static final String DATA_KEY_CONSTRUCTION_TYPE = "construction type";
    public static final String DATA_VALUE_CONSTRUCTION_TYPE_VALUE = "ct value";
    public static final String DATA_VALUE_CONSTRUCTION_TYPE_VARIABLE = "ct value";

    private THOSEObjects() {
    }

    public static void mutateValue(THATObject object, THATObject value) {
        mutateValue(object, value.value);
        object.toStringFunction = value.toStringFunction;
        object.accessibleFunctions.removeAll(value.accessibleFunctions);
        object.accessibleFunctions.addAll(value.accessibleFunctions);
//      object.accessibleMember.removeAll(value.accessibleMember);
        object.accessibleMember.putAll(value.accessibleMember);
    }

    public static void mutateValue(THATObject object, Object value) {
        object.value = value;

        if (Types.isNativeType(value.getClass()))
            object.primaryInference = value.getClass();
        else {
            object.primaryInference = String.class; // TODO: should we place null or String (because every obj can be represented by toString?)
            object.secondaryInferences.add(value.getClass());
        }
    }

    public static THATObject create(Object value) {
        THATObject object = new THATObject();
        if (value != null) mutateValue(object, value);
        return object;
    }

    public static THATObject create(String type, String name, Object value) {
        THATObject object = create(value);
        object.name = name;
        object.objectData.put(DATA_KEY_CONSTRUCTION_TYPE, type);
        return object;
    }

    public static THATObject createValue(Object value) {
        return create(DATA_VALUE_CONSTRUCTION_TYPE_VALUE, null, value);
    }

    public static THATObject createEmptyVariable(String name) {
        return create(DATA_VALUE_CONSTRUCTION_TYPE_VARIABLE, name, null);
    }

//    public static THATObject createEmptyValue() {
//        return createValue(null);
//    }

    public static THATObject createVariable(Object value) {
        return create(DATA_VALUE_CONSTRUCTION_TYPE_VARIABLE, null, value);
    }

    public static THATObject createVariable(String name, Object value) {
        return create(DATA_VALUE_CONSTRUCTION_TYPE_VARIABLE, name, value);
    }

    private static final Pattern rgx_value = Pattern.compile("^([+-]?)(?:([_0-9]+)|([_0-9]*\\.[_0-9]*))([sdlfSDLF]?)$");

    public static THATObject createAfterReducing(String value) {
        Matcher matcher = rgx_value.matcher(value);
        if (matcher.matches()) {
            String sign = matcher.group(1);
            String sunkNum = matcher.group(2);
            String floatingNum = matcher.group(3);
            char modifier = matcher.group(4).equals("") ? 'i' : matcher.group(4).charAt(0);
            if (sunkNum != null) return switch (modifier) {
                case 's' -> create(PrimitiveParser.SHORT.parse(sign + sunkNum));
                case 'i' -> create(PrimitiveParser.INT.parse(sign + sunkNum));
                case 'l' -> create(PrimitiveParser.LONG.parse(sign + sunkNum));
                case 'f' -> create(PrimitiveParser.FLOAT.parse(sign + sunkNum));
                case 'd' -> create(PrimitiveParser.DOUBLE.parse(sign + sunkNum));
                default -> throw new IllegalStateException();
            };
            else return switch (modifier) {
                case 'f' -> create(PrimitiveParser.FLOAT.parse(sign + floatingNum));
                case 'd', 'i' -> create(PrimitiveParser.DOUBLE.parse(sign + floatingNum));
                default -> create(value);
            };

        } else return create(value);
    }
}
