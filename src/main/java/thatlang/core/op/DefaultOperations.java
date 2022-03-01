package thatlang.core.op;

import in.mcxiv.CentralRepository;
import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.Strings;
import in.mcxiv.utils.Triplet;
import thatlang.THOSEUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Deprecated
public class DefaultOperations {

    public static final HashMap<Triplet<String, Class<?>, Class<?>>, Operable<?, ?, ?>> operables = new HashMap<>();

    static {
        System.out.println(CollectionOfAllTheFreakingDOs.class.getFields().length);
        for (Field field : CollectionOfAllTheFreakingDOs.class.getFields()) {
            if (field.getName().length() == 3) {
                Object o = Try.GetAnd(() -> field.get(null)).ElseNull();
                if (o == null) {
                    System.out.println("Null ?");
                    continue;
                }
                Operable<?, ?, ?> opr = (Operable<?, ?, ?>) o;
                operables.put(
                        new Triplet<>(opr.operator, opr.claA, opr.claB),
                        opr
                );
            }
        }
        CentralRepository.prt("Default Opeartors", "A total of %d default operators were loaded.".formatted(operables.size()));
    }

    public interface CollectionOfAllTheFreakingDOs extends IntegerDO, FloatDO, BooleanDO, StringDO, ObjectDO {
    }

    public interface ObjectDO {
        Operable<?, ?, ?> ino = Operable.of("in", Object.class, Object.class, Boolean.class)
                .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)), inOperator = ino;
        Operable<?, ?, ?> neo = Operable.of("<>", Object.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> f != n), notEqualQuadrilateral = neo;
        Operable<?, ?, ?> iso = Operable.of("instance of", Object.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> n instanceof Class<?> cls ? cls.isInstance(f) : n.getClass().isInstance(f)), instanceOfOperator = iso; // Note: cls.equals(Integer.class) is equivalent to cls.isInstance(f) because we know that f is an int
        Operable<?, ?, ?> eto = Operable.of("==", Object.class, Object.class, Boolean.class)
                .apply(() -> (a, b) -> a == b), equalsToOperator = eto;
        Operable<?, ?, ?> etj = Operable.of("equals", Object.class, Object.class, Boolean.class)
                .apply(() -> Object::equals), equalsToJavaOperator = etj;
        Operable<?, ?, ?> net = Operable.of("!=", Object.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> f != n), notEqualsToOperator = net;
        Operable<?, ?, ?> nej = Operable.of("not equals", Object.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToJavaOperator = nej;
    }

    public interface IntegerDO {
        Operable<Integer, Number, ?> pow = Operable.of("**", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> (int) Math.pow(f, n.doubleValue())), power = pow;
        Operable<Integer, Number, ?> mod = Operable.of("%", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f % n.intValue()), modulus = mod;
        Operable<Integer, Number, ?> div = Operable.of("/", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f / n.intValue()), division = div;
        Operable<Integer, Number, ?> add = Operable.of("+", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f + n.intValue()), addition = add;
        Operable<Integer, Number, ?> sub = Operable.of("-", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f + n.intValue()), subtraction = sub;
        Operable<Integer, Number, ?> lso = Operable.of("<<", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f << n.intValue()), leftShiftOperator = lso;
        Operable<Integer, Number, ?> rso = Operable.of(">>", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f >> n.intValue()), rightShiftOperator = rso;
        Operable<Integer, Number, ?> lsi = Operable.of("<<<", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f << n.intValue()), leftShiftIgnorant = lsi;
        Operable<Integer, Number, ?> rsi = Operable.of(">>>", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f >>> n.intValue()), rightShiftIgnorant = rsi;
        Operable<Integer, Number, ?> ee1 = Operable.of("<<>>", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f * n.intValue()), easterEggOne = ee1;
        Operable<Integer, Number, ?> ee2 = Operable.of(">><<", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f * n.intValue()), easterEggTwo = ee2;
        Operable<?, ?, ?> ino = Operable.of("in", Integer.class, Object.class, Boolean.class)
                .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)), inOperator = ino;
        Operable<Integer, Number, ?> loe = Operable.of("<=", Integer.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f <= n.intValue()), lessOrEqual = loe;
        Operable<Integer, Number, ?> neo = Operable.of("<>", Integer.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f != n.intValue()), notEqualQuadrilateral = neo;
        Operable<Integer, Number, ?> lto = Operable.of("<", Integer.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f < n.intValue()), lessThanOperator = lso;
        Operable<Integer, Number, ?> goe = Operable.of(">=", Integer.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f >= n.intValue()), greaterOrEqual = goe;
        Operable<Integer, Object, ?> iso = Operable.of("instance of", Integer.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> n instanceof Class<?> cls ? cls.equals(Integer.class) : n.getClass().equals(Integer.class)), instanceOfOperator = iso; // Note: cls.equals(Integer.class) is equivalent to cls.isInstance(f) because we know that f is an int
        Operable<Integer, Integer, ?> eto = Operable.of("==", Integer.class, Integer.class, Boolean.class)
                .apply(() -> (a, b) -> a == b), equalsToOperator = eto;
        Operable<Integer, Number, ?> etj = Operable.of("equals", Integer.class, Number.class, Boolean.class)
                .apply(() -> Integer::equals), equalsToJavaOperator = etj;
        Operable<Integer, Integer, ?> net = Operable.of("!=", Integer.class, Integer.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToOperator = net;
        Operable<Integer, Integer, ?> nej = Operable.of("not equals", Integer.class, Integer.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToJavaOperator = nej;
        Operable<Integer, Number, ?> bwa = Operable.of("&", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f & n.intValue()), bitWiseAnd = bwa;
        Operable<Integer, Number, ?> bwx = Operable.of("^", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f ^ n.intValue()), bitWiseXor = bwx;
        Operable<Integer, Number, ?> bwo = Operable.of("|", Integer.class, Number.class, Integer.class)
                .apply(() -> (f, n) -> f | n.intValue()), bitWiseOr = bwo;
    }

    public interface FloatDO {
        Operable<?, ?, ?> pow = Operable.of("**", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> (float) Math.pow(f, n.doubleValue())), power = pow;
        Operable<?, ?, ?> mod = Operable.of("%", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> f % n.floatValue()), modulus = mod;
        Operable<?, ?, ?> div = Operable.of("/", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> f / n.floatValue()), division = div;
        Operable<?, ?, ?> add = Operable.of("+", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> f + n.floatValue()), addition = add;
        Operable<?, ?, ?> sub = Operable.of("-", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> f + n.floatValue()), subtraction = sub;
        Operable<?, ?, ?> lso = Operable.of("<<", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) << n.intValue())), leftShiftOperator = lso;
        Operable<?, ?, ?> rso = Operable.of(">>", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) >> n.intValue())), rightShiftOperator = rso;
        Operable<?, ?, ?> lsi = Operable.of("<<<", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) << n.intValue())), leftShiftIgnorant = lsi;
        Operable<?, ?, ?> rsi = Operable.of(">>>", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) >>> n.intValue())), rightShiftIgnorant = rsi;
        Operable<?, ?, ?> ee1 = Operable.of("<<>>", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> f * n.floatValue()), easterEggOne = ee1;
        Operable<?, ?, ?> ee2 = Operable.of(">><<", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> f * n.floatValue()), easterEggTwo = ee2;
        Operable<?, ?, ?> ino = Operable.of("in", Float.class, Object.class, Boolean.class)
                .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)), inOperator = ino;
        Operable<?, ?, ?> loe = Operable.of("<=", Float.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f <= n.floatValue()), lessOrEqual = loe;
        Operable<?, ?, ?> neo = Operable.of("<>", Float.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f != n.floatValue()), notEqualQuadrilateral = neo;
        Operable<?, ?, ?> lto = Operable.of("<", Float.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f < n.floatValue()), lessThanOperator = lso;
        Operable<?, ?, ?> goe = Operable.of(">=", Float.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f >= n.floatValue()), greaterOrEqual = goe;
        Operable<?, ?, ?> iso = Operable.of("instance of", Float.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> n instanceof Class<?> cls ? cls.equals(Float.class) : n.getClass().equals(Float.class)), instanceOfOperator = iso; // Note: cls.equals(Float.class) is equivalent to cls.isInstance(f) because we know that f is an int
        Operable<?, ?, ?> eto = Operable.of("==", Float.class, Float.class, Boolean.class)
                .apply(() -> Objects::equals), equalsToOperator = eto;
        Operable<?, ?, ?> etj = Operable.of("equals", Float.class, Number.class, Boolean.class)
                .apply(() -> Float::equals), equalsToJavaOperator = etj;
        Operable<?, ?, ?> net = Operable.of("!=", Float.class, Float.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToOperator = net;
        Operable<?, ?, ?> nej = Operable.of("not equals", Float.class, Float.class, Boolean.class)
                .apply(() -> (f, n) -> !f.equals(n)), notEqualsToJavaOperator = nej;
        Operable<?, ?, ?> bwa = Operable.of("&", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) & n.intValue())), bitWiseAnd = bwa;
        Operable<?, ?, ?> bwx = Operable.of("^", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) ^ n.intValue())), bitWiseXor = bwx;
        Operable<?, ?, ?> bwo = Operable.of("|", Float.class, Number.class, Float.class)
                .apply(() -> (f, n) -> Float.intBitsToFloat(Float.floatToIntBits(f) | n.intValue())), bitWiseOr = bwo;
    }

    public interface BooleanDO {
        Operable<?, ?, ?> pow = Operable.of("**", Boolean.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f || ((int) Math.pow(-1, n.doubleValue())) != 0), power = pow;
        Operable<?, ?, ?> mod = Operable.of("%", Boolean.class, Number.class, Boolean.class)
                .apply(() -> (f, n) -> f), modulus = mod;
        Operable<?, ?, ?> ino = Operable.of("in", Boolean.class, Object.class, Boolean.class)
                .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)), inOperator = ino;
        Operable<?, ?, ?> neo = Operable.of("<>", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> !f.equals(n)), notEqualQuadrilateral = neo;
        Operable<?, ?, ?> iso = Operable.of("instance of", Boolean.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> n instanceof Class<?> cls ? cls.equals(Boolean.class) : n.getClass().equals(Boolean.class)), instanceOfOperator = iso; // Note: cls.equals(Boolean.class) is equivalent to cls.isInstance(f) because we know that f is an int
        Operable<?, ?, ?> eto = Operable.of("==", Boolean.class, Object.class, Boolean.class)
                .apply(() -> Objects::equals), equalsToOperator = eto;
        Operable<?, ?, ?> etj = Operable.of("equals", Boolean.class, Object.class, Boolean.class)
                .apply(() -> Boolean::equals), equalsToJavaOperator = etj;
        Operable<?, ?, ?> net = Operable.of("!=", Boolean.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToOperator = net;
        Operable<?, ?, ?> nej = Operable.of("not equals", Boolean.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToJavaOperator = nej;
        Operable<?, ?, ?> sao = Operable.of("&&", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f && n), symbolicAndOperator = sao;
        Operable<?, ?, ?> sxo = Operable.of("^^", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f ^ n), symbolicXorOperator = sxo;
        Operable<?, ?, ?> soo = Operable.of("||", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f || n), symbolicOrOperator = soo;
        Operable<?, ?, ?> bwa = Operable.of("&", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f & n), bitWiseAnd = bwa;
        Operable<?, ?, ?> bwx = Operable.of("^", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f ^ n), bitWiseXor = bwx;
        Operable<?, ?, ?> bwo = Operable.of("|", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f | n), bitWiseOr = bwo;
        Operable<?, ?, ?> vao = Operable.of("and", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f && n), verbalAndOperator = vao;
        Operable<?, ?, ?> nao = Operable.of("nand", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> !(f && n)), verbalNotAndOperator = nao;
        Operable<?, ?, ?> voo = Operable.of("or", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> f || n), verbalOrOperator = voo;
        Operable<?, ?, ?> noo = Operable.of("nor", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> !(f || n)), verbalNotOrOperator = noo;
        Operable<?, ?, ?> xao = Operable.of("xand", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> !((f || n) && (!(f && n)))), verbalExclusiveAndOperator = xao;
        Operable<?, ?, ?> xna = Operable.of("xnand", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> (f || n) && (!(f && n))), verbalExclusiveNotOperator = xna;
        Operable<?, ?, ?> xoo = Operable.of("xor", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> (f || n) && (!(f && n))), verbalExclusiveOrOperator = xoo;
        Operable<?, ?, ?> xno = Operable.of("xnor", Boolean.class, Boolean.class, Boolean.class)
                .apply(() -> (f, n) -> !((f || n) && (!(f && n)))), verbalExclusiveNotOrOperator = xno;
    }

    public interface StringDO {
        Operable<?, ?, ?> mod = Operable.of("%", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> n instanceof Iterable<?> i ? f.formatted(StreamSupport.stream(i.spliterator(), false).toArray()) : f.formatted(n)), modulus = mod;
        Operable<?, ?, ?> add = Operable.of("+", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f + n), addition = add;
        Operable<?, ?, ?> sub = Operable.of("-", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f.replace(n.toString(), "")), subtraction = sub;
        Operable<?, ?, ?> lso = Operable.of("<<", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f + " "), leftShiftOperator = lso;
        Operable<?, ?, ?> rso = Operable.of(">>", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f.length() > 0 ? f.substring(1) : f), rightShiftOperator = rso;
        Operable<?, ?, ?> lsi = lso, leftShiftIgnorant = lsi;
        Operable<?, ?, ?> rsi = rso, rightShiftIgnorant = rsi;
        Operable<?, ?, ?> ee1 = Operable.of("<<>>", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> Strings.interlumble(f, n.toString())), easterEggOne = ee1; // TODO: Something else?
        Operable<?, ?, ?> ee2 = Operable.of(">><<", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> Strings.interlumble(f, n.toString())), easterEggTwo = ee2;
        Operable<?, ?, ?> ino = Operable.of("in", String.class, Object.class, Boolean.class)
                .apply(() -> (f, o) -> o instanceof Iterable<?> i && THOSEUtils.contains(i, o)), inOperator = ino;
        Operable<?, ?, ?> neo = Operable.of("<>", String.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> f != n), notEqualQuadrilateral = neo;
        Operable<?, ?, ?> iso = Operable.of("instance of", String.class, Object.class, Boolean.class)
                .apply(() -> (f, n) -> n instanceof Class<?> cls ? cls.equals(String.class) : n.getClass().equals(String.class)), instanceOfOperator = iso; // Note: cls.equals(String.class) is equivalent to cls.isInstance(f) because we know that f is an int
        Operable<?, ?, ?> eto = Operable.of("==", String.class, String.class, Boolean.class)
                .apply(() -> (f, n) -> f == n), equalsToOperator = eto;
        Operable<?, ?, ?> etj = Operable.of("equals", String.class, Object.class, Boolean.class)
                .apply(() -> Object::equals), equalsToJavaOperator = etj;
        Operable<?, ?, ?> net = Operable.of("!=", String.class, String.class, Boolean.class)
                .apply(() -> (f, n) -> f != n), notEqualsToOperator = net;
        Operable<?, ?, ?> nej = Operable.of("not equals", String.class, String.class, Boolean.class)
                .apply(() -> (f, n) -> !Objects.equals(f, n)), notEqualsToJavaOperator = nej;
        Operable<?, ?, ?> sro = Operable.of("remove", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f.replace(n.toString(), "")), stringRemovalOperation = sro;
        Operable<?, ?, ?> rao = Operable.of("remove all", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f.replaceAll(n.toString(), "")), stringRemoveAllOperation = rao;
        Operable<?, ?, ?> rfo = Operable.of("remove first", String.class, Object.class, String.class)
                .apply(() -> (f, n) -> f.replaceFirst(n.toString(), "")), stringRemoveFirstOperation = rfo;
    }
}
