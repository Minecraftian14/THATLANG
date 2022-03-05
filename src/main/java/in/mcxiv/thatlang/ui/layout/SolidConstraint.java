package in.mcxiv.thatlang.ui.layout;

import in.mcxiv.utils.PrimitiveParser;

/**
 * This javadoc is general statement for all methods in this class.
 * <p>
 * any method get<V>(? int [WH]c, ? int [WH]r) means that it will return
 * the <V> value specific to the component this constraint was used
 * on. Here, <V> can be X for abscissa, Y for ordinate, W for width
 * and H for height. [WH]c refers to width/height of parent component
 * and [WH]r refers to width/height of root (JFrame).
 */
public abstract class SolidConstraint {

    public static final String SOLID_WH = "xywh+";
    public static final String UNSPECIFIED_XY = "xyxy";
    public static final String SOLID_XY = "xyxy+";
    public static final String MIRROR_XY = "xywh-";

    public static SolidConstraint newInstance(Number a, Number b, Number c, Number d) {
        return newInstance("null", a, b, c, d);
    }

    public static SolidConstraint newInstance(String type, Number a, Number b, Number c, Number d) {
        return new Doppelgänger(switch (type) {
            case SOLID_WH -> new SolidWH(a, b, c, d);
            case SOLID_XY -> new SolidXY(a, b, c, d);
            case MIRROR_XY -> new MirrorXY(a, b, c, d);
            case UNSPECIFIED_XY -> {
                if (c.floatValue() >= 0 && d.floatValue() >= 0)
                    yield new SolidXY(a, b, c, d);
                yield new MirrorXY(a, b, c, d);
            }
            default -> {
                if (c.floatValue() >= 0 && d.floatValue() >= 0)
                    yield new SolidWH(a, b, c, d);
                yield new MirrorXY(a, b, c, d);
            }
        });
    }

    public abstract int getX(int Wc);

    public abstract int getY(int Hc);

    public abstract int getW(int Wc);

    public abstract int getH(int Hc);

    public static final class SolidWH extends SolidConstraint {

        Number x, y, w, h;

        public SolidWH(Number x, Number y, Number w, Number h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        @Override
        public int getX(int Wc) {
            return PrimitiveParser.resolveNumber(x, Wc);
        }

        @Override
        public int getY(int Hc) {
            return PrimitiveParser.resolveNumber(y, Hc);
        }

        @Override
        public int getW(int Wc) {
            return PrimitiveParser.resolveNumber(w, Wc);
        }

        @Override
        public int getH(int Hc) {
            return PrimitiveParser.resolveNumber(h, Hc);
        }
    }

    public static final class SolidXY extends SolidConstraint {

        Number x1, y1, x2, y2;

        public SolidXY(Number x1, Number y1, Number x2, Number y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public int getX(int Wc) {
            return PrimitiveParser.resolveNumber(x1, Wc);
        }

        @Override
        public int getY(int Hc) {
            return PrimitiveParser.resolveNumber(y1, Hc);
        }

        @Override
        public int getW(int Wc) {
            return PrimitiveParser.resolveNumber(x2, Wc) - getX(Wc);
        }

        @Override
        public int getH(int Hc) {
            return PrimitiveParser.resolveNumber(y2, Hc) - getY(Hc);
        }
    }

    public static final class MirrorXY extends SolidConstraint {

        Number x1, y1, x2, y2;

        public MirrorXY(Number x1, Number y1, Number x2, Number y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public int getX(int Wc) {
            return PrimitiveParser.resolveNumber(x1, Wc);
        }

        @Override
        public int getY(int Hc) {
            return PrimitiveParser.resolveNumber(y1, Hc);
        }

        @Override
        public int getW(int Wc) {
            return Wc - PrimitiveParser.resolveNumber(x2, Wc);
        }

        @Override
        public int getH(int Hc) {
            return Hc - PrimitiveParser.resolveNumber(y2, Hc);
        }
    }

    public static final class Doppelgänger extends SolidConstraint {

        SolidConstraint constraint;

        public Doppelgänger(SolidConstraint constraint) {
            this.constraint = constraint;
        }

        public SolidConstraint getConstraint() {
            return constraint;
        }

        public void setConstraint(SolidConstraint constraint) {
            this.constraint = constraint;
        }

        @Override
        public int getX(int Wc) {
            return constraint.getX(Wc);
        }

        @Override
        public int getY(int Hc) {
            return constraint.getY(Hc);
        }

        @Override
        public int getW(int Wc) {
            return constraint.getW(Wc);
        }

        @Override
        public int getH(int Hc) {
            return constraint.getH(Hc);
        }
    }

}
