package in.mcxiv.thatlang.universe;

import com.mcxiv.logger.decorations.Format;
import in.mcxiv.utils.LinkedList;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.HashMap;
import java.util.Objects;

import static in.mcxiv.CentralRepository.*;
import static thatlang.core.THOSEObjects.createValue;

@Deprecated
public class Operators {

    public static final String LOG_HEAD = getLogHead();

    public static final HashMap<String, BinaryOperatorOperation> map = new HashMap<>();

    public static final LinkedList<String, String> list;

    private static final Class[] SUNKEN_NUMBER = {Byte.class, Short.class, Integer.class, Long.class};
    private static final Class[] FLOATING_NUMBER = {Float.class, Double.class};
    private static final Class[] NUMBER = {Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class};

    static {
        prt(LOG_HEAD, UNDER_STATIC_INITIALIZER);

        map.put("**", (l, r) -> s(Math.pow(n(l), n(r))));
        map.put("++", unary(l -> getAndIncr(l, +1), r -> incrAndGet(r, +1))); // Unary
        map.put("--", unary(l -> getAndIncr(l, -1), r -> incrAndGet(r, -1))); // Unary
//        operators.put("+"); // Unary // TODO: CONTINUE HERE
//        operators.put("-"); // Unary
//        operators.put("~"); // Unary
//        operators.put("(type cast)"); Unary (cast)
//        operators.put("!"); Unary
//        operators.put("not"); Unary (for operators)
//        map.put("size of"); Unary
        map.put("*", (l, r) -> s(n(l) * n(r)));
        map.put("%", (l, r) -> s(n(l) % n(r)));
        map.put("/", (l, r) -> s(n(l) / n(r)));
        map.put("+", Operators::plusSymbol);
        map.put("-", (l, r) -> s(n(l) - n(r)));
        map.put("<<", (l, r) -> s(i(l) << i(r)));
        map.put(">>", (l, r) -> s(i(l) >> i(r)));
        map.put("<<<", (l, r) -> s(i(l) << i(r)));
        map.put(">>>", (l, r) -> s(i(l) >>> i(r)));
        map.put("<<<>>>", (l, r) -> s(n(l) * n(r))); // TODO: easter egg
        map.put(">>><<<", (l, r) -> s(n(l) * n(r))); // TODO: easter egg
        map.put("in", (l, r) -> s(l.v().contains(r.v())));
        map.put("is", (l, r) -> s(l.equals(r)));
        map.put("<=", (l, r) -> s(n(l) <= n(r)));
        map.put("<>", (l, r) -> s(n(l) != n(r))); // TODO: Is it any diff than !=?
        map.put("<", (l, r) -> s(n(l) < n(r)));
        map.put(">=", (l, r) -> s(n(l) >= n(r)));
        map.put(">", (l, r) -> s(n(l) > n(r)));
//        map.put("instance of", (l, r) -> s(n(l) * n(r))); // TODO: obj instance of Rectangle strictly checks if obj is an instance made from a Rectangle constructor, and not of Quadrilateral
//        map.put("descendant of", (l, r) -> s(n(l) * n(r))); // TODO: an obj made from Rect cons may also return tru if checked against Quad
//        operators.put("from family of", (l, r) -> s(n(l) * n(r))); // an obj made from Rect cons may also return tru if checked against Kite (this is a tertiary operator because we also need to prvide an upper limit lie quad)
        map.put("==", (l, r) -> s(l.equals(r)));
        map.put("!=", (l, r) -> s(!l.equals(r)));
        map.put("&&", (l, r) -> s(b(l) && b(r)));
        map.put("^^", (l, r) -> s(XOR(b(l), b(r))));
        map.put("||", (l, r) -> s(b(l) || b(r)));
        map.put("&", (l, r) -> s(i(l) & i(r)));
        map.put("^", (l, r) -> s(i(l) ^ i(r)));
        map.put("|", (l, r) -> s(i(l) | i(r)));
        map.put("and", (l, r) -> s(b(l) && b(r)));
        map.put("nand", (l, r) -> s(!(b(l) && b(r))));
        map.put("xand", (l, r) -> s(!XOR(b(l), b(r))));
        map.put("xnand", (l, r) -> s(XOR(b(l), b(r))));
        map.put("or", (l, r) -> s(b(l) || b(r)));
        map.put("nor", (l, r) -> s(!(b(l) || b(r))));
        map.put("xor", (l, r) -> s(XOR(b(l), b(r))));
        map.put("xnor", (l, r) -> s(!XOR(b(l), b(r))));
//        map.put("not", (l, r) -> s(n(l) * n(r))); // UNARY
//        map.put("=", (l, r) -> s(n(l) * n(r))); // assignment :eyes:
//        operators.add("<op>="); any operator in place of op is acceptable :eyes:

        list = new LinkedList<>(map, s -> s);
    }

    private static THATObject plusSymbol(THATObject a, THATObject b) {
        if (a.primaryInference == null) return THOSEObjects.createValue(a.v() + b.v());
        if (a.primaryInference == String.class)
            return THOSEObjects.createValue(((String) a.value) + b.value.toString());
        if (Number.class.isAssignableFrom(a.primaryInference)) {
            if (!Number.class.isAssignableFrom(b.primaryInference))
                return THOSEObjects.createValue(a.value.toString() + b.value.toString());
            // TODO: convert number to the lowest possible complexity.
            // Like... say we add 1L + 1, 1st is long, and 2nd is int. Convert both to long and return a long
            // and.. say we add 1 + 1f, 1st is int, and 2nd is float. Convert both to float and return a float
            return THOSEObjects.createValue(((Number) a.value).doubleValue() + ((Number) b.value).doubleValue());
        }
        return THOSEObjects.createValue(a.value.toString() + b.value.toString());
    }

    private static THATObject getAndIncr(THATObject l, int d) {
        double n = n(l);
        THOSEObjects.mutateValue(l, n + d);
        return s(n);
    }

    private static THATObject incrAndGet(THATObject r, int d) {
        double n = n(r) + 1;
        THOSEObjects.mutateValue(r, n - d);
        return s(n);
    }

    private static boolean XOR(boolean a, boolean b) {
        return (a || b) && (!(a && b));
    }

    private static THATObject s(Object o) {
        return createValue(Objects.toString(o));
    }

    private static long i(THATObject r) {
        return PrimitiveParser.LONG.parse(r.v());
    }

    private static boolean b(THATObject r) {
        return PrimitiveParser.BOOLEAN.parse(r.v());
    }

    private static double n(THATObject r) {
        return PrimitiveParser.DOUBLE.parse(r.v());
    }

    @Format({":: :@A890F0 #F %*40s: ::", ":: :@A800F0 #0 n %-100s: ::"})
    public static void prt(Object type, Object msg) {
        log.prt(type, msg);
    }

    public static THATObject operate(THATObject left, String op, THATObject right) {
        return map.getOrDefault(op, (l, r) -> createValue(l.v() + r.v())).act(left, right);
    }

    public interface BinaryOperatorOperation {
        THATObject act(THATObject l, THATObject r);
    }

    public static BinaryOperatorOperation unary(PrefixOperation prefix, PostfixOperation postfix) {
        return (l, r) -> {
            if (l != null) return postfix.act(l);
            else if (r != null) return prefix.act(r);
            else throw new IllegalStateException();
        };
    }

    public static Object simplify(Object a, Object b) {
        if (a instanceof Number numA)
            if (b instanceof Number numB)
                return simplify(numA, numB);
        return null;
    }

    public static Number simplify(Number a, Number b) {
        if (a instanceof Byte A)
            return b instanceof Byte B ? ((byte) (A + B))
                    : b instanceof Short B ? ((short) (A + B))
                    : b instanceof Integer B ? (A + B)
                    : b instanceof Long B ? (A + B)
                    : b instanceof Float B ? (A + B)
                    : b instanceof Double B ? (A + B)
                    : A + b.byteValue();

        else if (a instanceof Short A)
            return b instanceof Byte B ? ((short) (A + B))
                    : b instanceof Short B ? ((short) (A + B))
                    : b instanceof Integer B ? (A + B)
                    : b instanceof Long B ? (A + B)
                    : b instanceof Float B ? (A + B)
                    : b instanceof Double B ? (A + B)
                    : A + b.shortValue();

        else if (a instanceof Integer A)
            return b instanceof Byte B ? (A + B)
                    : b instanceof Short B ? (A + B)
                    : b instanceof Integer B ? (A + B)
                    : b instanceof Long B ? (A + B)
                    : b instanceof Float B ? (A + B)
                    : b instanceof Double B ? (A + B)
                    : A + b.intValue();

        else if (a instanceof Long A)
            return b instanceof Byte B ? (A + B)
                    : b instanceof Short B ? (A + B)
                    : b instanceof Integer B ? (A + B)
                    : b instanceof Long B ? (A + B)
                    : b instanceof Float B ? (double) (A + B)
                    : b instanceof Double B ? (A + B)
                    : A + b.longValue();

        else if (a instanceof Float A)
            return b instanceof Byte B ? (A + B)
                    : b instanceof Short B ? (A + B)
                    : b instanceof Integer B ? (A + B)
                    : b instanceof Long B ? (A + B)
                    : b instanceof Float B ? (A + B)
                    : b instanceof Double B ? (A + B)
                    : A + b.floatValue();

        else if (a instanceof Double A)
            return b instanceof Byte B ? (A + B)
                    : b instanceof Short B ? (A + B)
                    : b instanceof Integer B ? (A + B)
                    : b instanceof Long B ? (A + B)
                    : b instanceof Float B ? (A + B)
                    : b instanceof Double B ? (A + B)
                    : A + b.doubleValue();

        else throw new IllegalStateException();
    }

    public interface UnaryOperatorOperation extends BinaryOperatorOperation {
        THATObject act(THATObject l);

        default THATObject act(THATObject l, THATObject r) {
            return act(l);
        }
    }

    public interface PrefixOperation extends UnaryOperatorOperation {
        THATObject act(THATObject l);
    }

    public interface PostfixOperation extends UnaryOperatorOperation {
        THATObject act(THATObject r);
    }


}
