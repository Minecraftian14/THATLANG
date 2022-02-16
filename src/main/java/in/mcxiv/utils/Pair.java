package in.mcxiv.utils;

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
}
