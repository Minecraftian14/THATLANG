package in.mcxiv.utils;

import java.util.Objects;

public class Triplet<A, B, C> {
    private A a;
    private B b;
    private C c;

    public Triplet() {
        this(null, null, null);
    }

    public Triplet(A a, B b, C c) {
        setA(a);
        setB(b);
        setC(c);
    }

    public void setA(A a) {
        this.a = a;
    }

    public void setB(B b) {
        this.b = b;
    }

    public void setC(C c) {
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

    @Override
    public String toString() {
        return "Triplet{" +
               "a=" + a +
               ", b=" + b +
               ", c=" + c +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (equals2(o)) return true;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return triplet.getA().getClass().isInstance(getA()) && triplet.getB().getClass().isInstance(getB());
    }

    public boolean equals2(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(getA(), triplet.getA()) && Objects.equals(getB(), triplet.getB()) && Objects.equals(getC(), triplet.getC());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB(), getC());
    }
}
