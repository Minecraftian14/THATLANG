package thatlang.core;

import thatlang.THOSEUtils;
import thatlang.core.op.Operable;

import java.util.*;
import java.util.stream.StreamSupport;

public class THOSEOperatorsPrototype {

    public static final ArrayList<String> KNOWN_OPERATORS = new ArrayList<>();

    static {
        ArrayList<String> list = new ArrayList<>();
        list.add("**");
        list.add("*");
        list.add("%");
        list.add("/");
        list.add("+");
        list.add("-");
        list.add("<<");
        list.add(">>");
        list.add("<<<");
        list.add(">>>");
        list.add("<<<>>>");
        list.add(">>><<<");
        list.add("in");
        list.add("is");
        list.add("<=");
        list.add("<>");
        list.add("<");
        list.add(">=");
        list.add(">");
        list.add("instance of");
        list.add("==");
        list.add("equals");
        list.add("!=");
        list.add("not equals");
        list.add("&&");
        list.add("^^");
        list.add("||");
        list.add("&");
        list.add("^");
        list.add("|");
        list.add("and");
        list.add("nand");
        list.add("xand");
        list.add("xnand");
        list.add("or");
        list.add("nor");
        list.add("xor");
        list.add("xnor");
        KNOWN_OPERATORS.addAll(list);
//        map.put("=", (l, r) -> s(n(l) * n(r))); // assignment :eyes:
//        operators.add("<op>="); any operator in place of op is acceptable :eyes:
    }

    public static THATObject operate(THATObject left, String op, THATObject right) {
        return THOSEObjects.createValue(operateStep(left, op, right));
    }

    public static Object operateStep(THATObject left, String op, THATObject right) {
        if (left.primaryInference == null)
            // TODO: use secondary inference
            return null;

        try {
            if (left.value instanceof Integer Left)
                return right.value instanceof Number Right ? IntegerOperation.WithANumber.get(op).process(Left, Right)
                        : IntegerOperation.WithAnObject.get(op).process(Left, right.value);

            if (left.value instanceof Double Left)
                return right.value instanceof Number Right ? DoubleOperation.WithANumber.get(op).process(Left, Right)
                        : DoubleOperation.WithAnObject.get(op).process(Left, right.value);

            if (left.value instanceof Boolean Left)
                return right.value instanceof Boolean Right ? BooleanOperation.WithABoolean.get(op).process(Left, Right)
                        : BooleanOperation.WithAnObject.get(op).process(Left, right.value);
        } catch (NullPointerException ignored) {
        }

        Operable<Object, Object, ?> operable = ObjectOperation.WithAnObject.get(op);

        if (operable == null)
            throw new RuntimeException("No suitable operator for %s %s %s!".formatted(left.value, op, right.value));

        return operable.process(left.value, right.value);
    }

    public interface IntegerOperation {

        List<Operable<Integer, Number, ?>> WithANumberList = List.of(
                Operable.of("**", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> (int) Math.pow(f, n.doubleValue())),
                Operable.of("%", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f % n.intValue()),
                Operable.of("/", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f / n.intValue()),
                Operable.of("*", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f * n.intValue()),
                Operable.of("+", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f + n.intValue()),
                Operable.of("-", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f - n.intValue()),
                Operable.of("<<", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f << n.intValue()),
                Operable.of(">>", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f >> n.intValue()),
                Operable.of("<<<", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f << n.intValue()),
                Operable.of(">>>", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f >>> n.intValue()),
                Operable.of("<<>>", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f * n.intValue()),
                Operable.of(">><<", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f * n.intValue()),
                Operable.of("in", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, o) -> false),
                Operable.of("<=", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f <= n.intValue()),
                Operable.of("<>", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f != n.intValue()),
                Operable.of("<", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f < n.intValue()),
                Operable.of(">=", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f >= n.intValue()),
                Operable.of(">", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f > n.intValue()),
                Operable.of("instance of", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> n.getClass().equals(Integer.class)),
                Operable.of("==", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (a, b) -> a.doubleValue() == b.doubleValue()),
                Operable.of("equals", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> Objects.equals(f.doubleValue(), n.doubleValue())),
                Operable.of("!=", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f.doubleValue() != n.doubleValue()),
                Operable.of("not equals", Integer.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> !Objects.equals(f.doubleValue(), n.doubleValue())),
                Operable.of("&", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f & n.intValue()),
                Operable.of("^", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f ^ n.intValue()),
                Operable.of("|", Integer.class, Number.class, Integer.class)
                        .apply(() -> (f, n) -> f | n.intValue()));

        Map<String, Operable<Integer, Number, ?>> WithANumber = new HashMap<>() {{
            WithANumberList.forEach(op -> put(op.getOperator(), op));
        }};

        List<Operable<Integer, Object, ?>> WithAnObjectList = List.of(
                Operable.of("+", Integer.class, Object.class, String.class)
                        .apply(() -> (f, n) -> f + n.toString()),
                Operable.of("in", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)),
                Operable.of("<>", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Number n && f != n.intValue()),
                Operable.of("instance of", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> n.getClass().equals(Integer.class)),
                Operable.of("==", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (a, o) -> o instanceof Number b && a.doubleValue() == b.doubleValue()),
                Operable.of("equals", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Number n && Objects.equals(f.doubleValue(), n.doubleValue())),
                Operable.of("!=", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> !(o instanceof Number n) || f.doubleValue() != n.doubleValue()),
                Operable.of("not equals", Integer.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> !(o instanceof Number n) || !Objects.equals(f.doubleValue(), n.doubleValue()))
        );

        Map<String, Operable<Integer, Object, ?>> WithAnObject = new HashMap<>() {{
            WithAnObjectList.forEach(op -> put(op.getOperator(), op));
        }};
    }

    public interface DoubleOperation {

        List<Operable<Double, Number, ?>> WithANumberList = List.of(
                Operable.of("**", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> (double) Math.pow(f, n.doubleValue())),
                Operable.of("%", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f % n.doubleValue()),
                Operable.of("/", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f / n.doubleValue()),
                Operable.of("*", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f * n.doubleValue()),
                Operable.of("+", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f + n.doubleValue()),
                Operable.of("-", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f - n.doubleValue()),
                Operable.of("<<", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) << n.intValue())),
                Operable.of(">>", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) >> n.intValue())),
                Operable.of("<<<", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) << n.intValue())),
                Operable.of(">>>", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) >>> n.intValue())),
                Operable.of("<<>>", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f * n.doubleValue()),
                Operable.of(">><<", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> f * n.doubleValue()),
                Operable.of("in", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, o) -> false),
                Operable.of("<=", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f <= n.doubleValue()),
                Operable.of("<>", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f != n.doubleValue()),
                Operable.of("<", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f < n.doubleValue()),
                Operable.of(">=", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f >= n.doubleValue()),
                Operable.of(">", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f > n.doubleValue()),
                Operable.of("instance of", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> n.getClass().equals(Double.class)),
                Operable.of("==", Double.class, Number.class, Boolean.class)
                        .apply(() -> (a, b) -> a.doubleValue() == b.doubleValue()),
                Operable.of("equals", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> Objects.equals(f.doubleValue(), n.doubleValue())),
                Operable.of("!=", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> f.doubleValue() != n.doubleValue()),
                Operable.of("not equals", Double.class, Number.class, Boolean.class)
                        .apply(() -> (f, n) -> !Objects.equals(f.doubleValue(), n.doubleValue())),
                Operable.of("&", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) & n.intValue())),
                Operable.of("^", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) ^ n.intValue())),
                Operable.of("|", Double.class, Number.class, Double.class)
                        .apply(() -> (f, n) -> Double.longBitsToDouble(Double.doubleToLongBits(f) | n.intValue()))
        );

        Map<String, Operable<Double, Number, ?>> WithANumber = new HashMap<>() {{
            WithANumberList.forEach(op -> put(op.getOperator(), op));
        }};

        List<Operable<Double, Object, ?>> WithAnObjectList = List.of(
                Operable.of("+", Double.class, Object.class, String.class)
                        .apply(() -> (f, n) -> f + n.toString()),
                Operable.of("in", Double.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)),
                Operable.of("<>", Double.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Number n && f != n.doubleValue()),
                Operable.of("instance of", Double.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> n.getClass().equals(Double.class)),
                Operable.of("==", Double.class, Object.class, Boolean.class)
                        .apply(() -> (a, o) -> o instanceof Number b && a.doubleValue() == b.doubleValue()),
                Operable.of("equals", Double.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Number n && Objects.equals(f.doubleValue(), n.doubleValue())),
                Operable.of("!=", Double.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> !(o instanceof Number n) || f.doubleValue() != n.doubleValue()),
                Operable.of("not equals", Double.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> !(o instanceof Number n) || !Objects.equals(f.doubleValue(), n.doubleValue()))
        );

        Map<String, Operable<Double, Object, ?>> WithAnObject = new HashMap<>() {{
            WithAnObjectList.forEach(op -> put(op.getOperator(), op));
        }};
    }

    public interface BooleanOperation {

        List<Operable<Boolean, Boolean, ?>> WithABooleanList = List.of(
                Operable.of("==", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (a, b) -> a.booleanValue() == b.booleanValue()),
                Operable.of("equals", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f.equals(n)),
                Operable.of("!=", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f.booleanValue() != n.booleanValue()),
                Operable.of("not equals", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> !f.equals(n)),
                Operable.of("&&", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f && n),
                Operable.of("^^", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f ^ n),
                Operable.of("||", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f || n),
                Operable.of("&", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f & n),
                Operable.of("^", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f ^ n),
                Operable.of("|", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f | n),
                Operable.of("and", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f && n),
                Operable.of("nand", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> !(f && n)),
                Operable.of("or", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> f || n),
                Operable.of("nor", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> !(f || n)),
                Operable.of("xand", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> !((f || n) && (!(f && n)))),
                Operable.of("xnand", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> (f || n) && (!(f && n))),
                Operable.of("xor", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> (f || n) && (!(f && n))),
                Operable.of("xnor", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> !((f || n) && (!(f && n)))),
                Operable.of("implies", Boolean.class, Boolean.class, Boolean.class)
                        .apply(() -> (f, n) -> !f || n)
        );

        Map<String, Operable<Boolean, Boolean, ?>> WithABoolean = new HashMap<>() {{
            WithABooleanList.forEach(op -> put(op.getOperator(), op));
        }};

        List<Operable<Boolean, Object, ?>> WithAnObjectList = List.of(
                Operable.of("in", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)),
                Operable.of("<>", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Boolean n && f != n),
                Operable.of("instance of", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> n.getClass().equals(Boolean.class)),
                Operable.of("==", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (a, o) -> o instanceof Boolean b && a.booleanValue() == b.booleanValue()),
                Operable.of("equals", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Boolean n && Objects.equals(f, n)),
                Operable.of("!=", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> !(o instanceof Boolean n) || f.booleanValue() != n.booleanValue()),
                Operable.of("not equals", Boolean.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> !(o instanceof Boolean n) || !Objects.equals(f, n))
        );

        Map<String, Operable<Boolean, Object, ?>> WithAnObject = new HashMap<>() {{
            WithAnObjectList.forEach(op -> put(op.getOperator(), op));
        }};
    }

    public interface ObjectOperation {

        List<Operable<Object, Object, ?>> WithAnObjectList = List.of(
                Operable.of("%", Object.class, Object.class, String.class)
                        .apply(() -> (f, n) -> n instanceof Iterable<?> i ? f.toString().formatted(StreamSupport.stream(i.spliterator(), false).map(o -> ((THATObject) o).value).toArray()) : f.toString().formatted(n)),
                Operable.of("+", Object.class, Object.class, String.class)
                        .apply(() -> (f, n) -> "" + f.toString() + n.toString()),
                Operable.of("-", Object.class, Object.class, String.class)
                        .apply(() -> (f, n) -> f.toString().replace(n.toString(), "")),
                Operable.of("<<", Object.class, Object.class, String.class)
                        .apply(() -> (f, o) -> o instanceof Number n ? f + " ".repeat(n.intValue()) : f.toString()),
                Operable.of(">>", Object.class, Object.class, Object.class)
                        .apply(() -> (f, o) -> o instanceof Number n ? f.toString().substring(n.intValue()) : f.toString()),
                Operable.of("in", Object.class, Object.class, Boolean.class)
                        .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)),
                Operable.of("<>", Object.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> f != n),
                Operable.of("instance of", Object.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> n instanceof Class<?> cls ? cls.isInstance(f) : n.getClass().equals(f.getClass())),
                Operable.of("==", Object.class, Object.class, Boolean.class)
                        .apply(() -> (a, b) -> a == b),
                Operable.of("equals", Object.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> f.equals(n)),
                Operable.of("!=", Object.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> f != n),
                Operable.of("not equals", Object.class, Object.class, Boolean.class)
                        .apply(() -> (f, n) -> !f.equals(n))
        );

        Map<String, Operable<Object, Object, ?>> WithAnObject = new HashMap<>() {{
            WithAnObjectList.forEach(op -> put(op.getOperator(), op));
        }};
    }
}
