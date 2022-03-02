package in.mcxiv.utils;

import java.util.Objects;

public class Pair<A, B> {
    private A a;
    private B b;

    public Pair() {
        this(null, null);
    }

    public Pair(A a, B b) {
        setA(a);
        setB(b);
    }

    public void setA(A a) {
        this.a = a;
    }

    public void setB(B b) {
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ",b=" + b +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(getA(), pair.getA()) && Objects.equals(getB(), pair.getB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB());
    }
}
