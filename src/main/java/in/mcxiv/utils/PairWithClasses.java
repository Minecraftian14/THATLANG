package in.mcxiv.utils;

import in.mcxiv.refl.Clazzonic;

public class PairWithClasses<A, B> {
    private A a;
    private Class<A> a_clazz;
    private B b;
    private Class<B> b_clazz;

    public PairWithClasses() {
        this(null, null);
    }

    public PairWithClasses(A a, B b) {
        setA(a);
        setB(b);
    }

    public void setA(A a) {
        this.a = a;
        // FIXME: 10-02-2022 Hmmmm, which accessor is better? The one right below or the one used in setB?
        this.a_clazz = Clazzonic.getClazz();
    }

    @SuppressWarnings("unchecked")
    public void setB(B b) {
        this.b = b;
        this.b_clazz = (Class<B>) b.getClass();
    }
}
