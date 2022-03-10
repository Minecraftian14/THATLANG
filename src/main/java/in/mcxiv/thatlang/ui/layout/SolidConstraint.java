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
                if (a.floatValue() < 0 || b.floatValue() < 0 || c.floatValue() < 0 || d.floatValue() < 0)
                    yield new MirrorXY(a, b, c, d);
                yield new SolidXY(a, b, c, d);
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
            return resolveNumber(x1, Wc, 0);
        }

        @Override
        public int getY(int Hc) {
            return resolveNumber(y1, Hc, 0);
        }

        @Override
        public int getW(int Wc) {
            return resolveNumber(x2, Wc, getX(Wc));
        }

        @Override
        public int getH(int Hc) {
            return resolveNumber(y2, Hc, getY(Hc));
        }

        private int resolveNumber(Number v, int c, int f) {
            int i = PrimitiveParser.resolveNumber(v, c);
            if (i < 0) return c + i - f;
            return i;
        }
    }

    public static sealed class Doppelgänger extends SolidConstraint permits SuperConstraint {

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

    public static final class SuperConstraint extends Doppelgänger {

        // TODO:
        //  Keyword based constraint setting.
        //  None of the following constants below meant anything, it's just to lay an idea of how i wanted it to be.
        //  Consider the example,
        //   UI().box() .xy 0 0 .wh fill half
        //   It will make a box covering the top half the drawing area.
        //  Add things like:
        //   `preferred` which seeks the preferred dimensions (if it's a JComponent)
        //   `center` which calculates a center based on Wc and getW


        public static final int NOTHING_SPECIAL = -1;
        public static final int SPACE_HUNGRY = -2;
        public static final int HALF = -3;
        public static final int FOURTH = -4;
        public static final int AUTO = -5;
        public static final int FILL = -6;

        int a, b, c, d;

        public SuperConstraint(SolidConstraint constraint, Number a, Number b, Number c, Number d) {
            super(constraint);
            this.a = resolveKeyword(a);
        }

        private int resolveKeyword(Number a) {
            if (!(a instanceof Integer i) || i >= 0) return NOTHING_SPECIAL;
            return i;
        }

        // Example of an override
        @Override
        public int getX(int Wc) {
            if (a == NOTHING_SPECIAL)
                return super.getX(Wc);
            // if a == HALF
            return Wc / 2;
        }
    }

}
