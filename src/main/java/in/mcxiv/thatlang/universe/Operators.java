package in.mcxiv.thatlang.universe;

import com.mcxiv.logger.decorations.Format;
import in.mcxiv.utils.LinkedList;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;

import java.util.HashMap;
import java.util.Objects;

import static in.mcxiv.CentralRepository.*;
import static thatlang.core.THOSEObjects.createValue;

public class Operators {

    public static final String LOG_HEAD = getLogHead();

    public static final HashMap<String, BinaryOperatorOperation> map = new HashMap<>();

    public static final LinkedList<String, String> list;

    static {
        prt(LOG_HEAD, UNDER_STATIC_INITIALIZER);

        map.put("**", (l, r) -> s(Math.pow(n(l), n(r))));
//        operators.put("++"); Unary
//        operators.put("--"); Unary
//        operators.put("+"); Unary
//        operators.put("-"); Unary
//        operators.put("~"); Unary
//        operators.put("(type cast)"); Unary (cast)
//        operators.put("!"); Unary
//        operators.put("not"); Unary (for operators)
//        map.put("size of"); Unary
        map.put("*", (l, r) -> s(n(l) * n(r)));
        map.put("%", (l, r) -> s(n(l) % n(r)));
        map.put("/", (l, r) -> s(n(l) / n(r)));
        map.put("+", (l, r) -> s(n(l) + n(r)));
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

}
