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
    public static final String SOLID_XY = "xywh-";

    public static final int NOTHING_SPECIAL = -1;
    public static final int SPACE_HUNGRY = -2;
    public static final int HALF = -3;
    public static final int FOURTH = -4;
    public static final int AUTO = -5;
    public static final int FILL = -6;
    public static final int CENTER = -7;

    public static SolidConstraint newInstance(Number a, Number b, Number c, Number d) {
        return newInstance("null", a, b, c, d);
    }

    public static SolidConstraint newInstance(String type, Number a, Number b, Number c, Number d) {
        return new Doppelgänger(switch (type) {
            case SOLID_WH -> new SolidWH(a, b, c, d);
            case SOLID_XY -> new SolidXY(a, b, c, d);
            default -> {
                if (c.floatValue() >= 0 && d.floatValue() >= 0)
                    yield new SolidWH(a, b, c, d);
                yield new SolidXY(a, b, c, d);
            }
        });
    }

    private static int resolveNumber(Number number, int bounds, int deductant) {
        if (number instanceof Byte || number instanceof Short
            || number instanceof Integer || number instanceof Long) {
            int value = number.intValue();
            if (value >= 0) return value;
            return switch (value) {
                case NOTHING_SPECIAL -> 0; // TODO: Probably removing this, and having more vars other than xyxywhrgb is better.
                case SPACE_HUNGRY -> 7;
                case HALF -> 8;
                case FOURTH -> 9;
                case AUTO -> 91;
                case FILL -> 92;
                case CENTER -> 92;
                default -> throw new RuntimeException();
            };
        } else if (number instanceof Float || number instanceof Double) {
            int value = (int) (bounds * number.doubleValue());
            if (value < 0) return bounds + value - deductant;
            return value;
        }
        throw new IllegalStateException();
    }

    private int padding = 0;

    public SolidConstraint setPadding(int padding) {
        this.padding = padding;
        return this;
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
            return resolveNumber(x, Wc, 0);
        }

        @Override
        public int getY(int Hc) {
            return resolveNumber(y, Hc, 0);
        }

        @Override
        public int getW(int Wc) {
            return resolveNumber(w, Wc, 0);
        }

        @Override
        public int getH(int Hc) {
            return resolveNumber(h, Hc, 0);
        }
    }

    @Deprecated(forRemoval = true)
    // Removed because SolidXY also acts the same way if given positive values.
    public static final class RedundantSolidXY extends SolidConstraint {

        Number x1, y1, x2, y2;

        public RedundantSolidXY(Number x1, Number y1, Number x2, Number y2) {
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

    // Renamed from MirrorXY
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

    public static final class SuperConstraint {

        // TODO:
        //  Keyword based constraint setting.
        //  None of the following constants below meant anything, it's just to lay an idea of how i wanted it to be.
        //  Consider the example,
        //   UI().box() .xy 0 0 .wh fill half
        //   It will make a box covering the top half the drawing area.
        //  Add things like:
        //   `preferred` applicable to be used on w or h which seeks the preferred dimensions (if it's a JComponent)
        //   `center` using center in x or y will use the w or h to calculate a suitable x or y to make the component appear in center
        //   `below` places the component below the previously placed component, x remains same, y increases by (prev.hei+layout.pad)
        //   `aside` places the component next to the previously placed component, y remains same, x increases by (prev.wid+layout.pad)
        //   `mirror` if set to x or y, they use the w or h to fit component as if mirroring across the parent component, yes it's very similar to saying x is 0.1 then x2 will be -0.1
        //   `fillH` x = lay.pad, w = Wc-2*lay.pad
        //   `fillV` y = lay.pad, h = Hc-2*lay.pad

        public static final int NOTHING_SPECIAL = -1;
        public static final int SPACE_HUNGRY = -2;
        public static final int HALF = -3;
        public static final int FOURTH = -4;
        public static final int AUTO = -5;
        public static final int FILL = -6;
        public static final int CENTER = -6;
    }

}
