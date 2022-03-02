package thatlang.core.op;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Operable<A, B, R> {

    protected final String operator;
    protected final Class<A> claA;
    protected final Class<B> claB;
    protected final Class<R> claR;

    public Operable(String operator, Class<A> claA, Class<B> claB, Class<R> claR) {
        this.claA = claA;
        this.claB = claB;
        this.claR = claR;
        this.operator = operator;
    }

    public abstract R process(A a, B b);

    public String getOperator() {
        return operator;
    }

    public static <A, B, R> Function<Supplier<OperableDummy<A, B, R>>, Operable<A, B, R>> of(String operator, Class<A> clsA, Class<B> clsB, Class<R> claR) {
        return sup -> new Operable<>(operator, clsA, clsB, claR) {
            @Override
            public R process(A a, B b) {
                return sup.get().process(a, b);
            }
        };
    }

    public interface OperableDummy<A, B, R> {
        R process(A a, B b);
    }
}
