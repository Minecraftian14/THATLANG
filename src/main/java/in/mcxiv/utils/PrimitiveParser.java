package in.mcxiv.utils;

import in.mcxiv.tryCatchSuite.Try;

import java.util.function.Function;

public class PrimitiveParser<Type> {

    public static final PrimitiveParser<Boolean> BOOLEAN = new PrimitiveParser<>(boolean.class, PrimitiveParser::parseBoolean, false);
    public static final PrimitiveParser<Byte> BYTE = new PrimitiveParser<>(byte.class, s -> s.getBytes()[0], (byte) 0);
    public static final PrimitiveParser<Character> CHAR = new PrimitiveParser<>(char.class, s -> s.charAt(0), (char) 0);
    public static final PrimitiveParser<Short> SHORT = new PrimitiveParser<>(short.class, Short::parseShort, (short) 0);
    public static final PrimitiveParser<Integer> INT = new PrimitiveParser<>(int.class, Integer::parseInt, 0);
    public static final PrimitiveParser<Float> FLOAT = new PrimitiveParser<>(float.class, Float::parseFloat, 0f);
    public static final PrimitiveParser<Long> LONG = new PrimitiveParser<>(long.class, Long::parseLong, 0L);
    public static final PrimitiveParser<Double> DOUBLE = new PrimitiveParser<>(double.class, Double::parseDouble, 0d);

    public static final PrimitiveParser<?>[] TYPES = {BYTE, CHAR, SHORT, INT, FLOAT, LONG, DOUBLE};

    public final Class<Type> clazz;
    public final Function<String, Type> parser;
    public final Type def;

    public PrimitiveParser(Class<Type> clazz, Function<String, Type> parser, Type def) {
        this.clazz = clazz;
        this.parser = parser;
        this.def = def;
    }

    public Type parse(String string) {
        return Try.getAnd(() -> parser.apply(string)).elseGet(() -> def);
    }

    public Type parse(Object object) {
        if (clazz.isInstance(object))
            return clazz.cast(object);
        return parse(object.toString());
    }

    @SuppressWarnings("unchecked")
    public static <Type> Type parseOpt(String string) {
        for (PrimitiveParser<?> type : TYPES) {
            try {
                Object parse = type.parse(string);
                if (parse == null) continue;
                return (Type) parse;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static boolean parseBoolean(String s) {
        Double d = Try.getAnd(() -> Double.parseDouble(s)).elseNull();
        if (d != null) return d == 0d;
        return switch (s.toLowerCase()) {
            case "on", "true", "high" -> true;
            case "off", "false", "low" -> false;
            default -> throw new IllegalStateException();
        };
    }

    public static int resolveNumber(Number number, int bounds) {
        if (number instanceof Byte || number instanceof Short
            || number instanceof Integer || number instanceof Long)
            return number.intValue();
        if (number instanceof Float || number instanceof Double)
            return (int) (bounds * number.doubleValue());
        throw new IllegalStateException();
    }

}
