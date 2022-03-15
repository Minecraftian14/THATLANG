package in.mcxiv.thatlang.ui.components.primitives;

import in.mcxiv.thatlang.ui.Drawable;
import in.mcxiv.thatlang.ui.EasyCanvas;
import in.mcxiv.thatlang.ui.components.ShapePanel;
import in.mcxiv.thatlang.ui.layout.SolidConstraint;
import in.mcxiv.thatlang.ui.layout.SolidLayout;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.awt.*;

public class ComponentBinder implements Drawable {

    final Component component;
    final THATObject object;

    public ComponentBinder(Component component) {
        this(component, THOSEObjects.createValue(null));
    }

    public ComponentBinder(Component component, THATObject object) {
        this.component = component;
        this.object = object;
        THOSEObjects.mutateValue(object, this);
    }

    @Override
    public void draw(Graphics2D g, EasyCanvas canvas) {
        Color color = resolveColor();
        if (color != null) {
            g.setColor(color);
            if(component instanceof ShapePanel sp)
                sp.setForeground(color);
        }

        SolidLayout layout = (SolidLayout) canvas.getLayout();
        SolidConstraint constraints = layout.getConstraints(component);
        if (constraints == null) {
            constraints = SolidConstraint.newInstance(SolidConstraint.SOLID_WH, 0, 0, 0, 0);
            layout.addLayoutComponent(component, constraints);
        }
        SolidConstraint resolvedConstraints = resolveConstraints();
        if (resolvedConstraints != null)
            ((SolidConstraint.Doppelgänger) constraints)
                    .setConstraint(resolvedConstraints);
        canvas.revalidate();

    }

    private Color resolveColor() {
        THATObject ro = object.getMember("r");
        THATObject go = object.getMember("g");
        THATObject bo = object.getMember("b");
        THATObject ao = object.getMember("a");
        if (ro == null || go == null || bo == null) return null;
        if (!(ro.value instanceof Number rn) || !(go.value instanceof Number gn) || !(bo.value instanceof Number bn))
            return null;
        int r = PrimitiveParser.resolveNumber(rn, 255);
        int g = PrimitiveParser.resolveNumber(gn, 255);
        int b = PrimitiveParser.resolveNumber(bn, 255);
        int a = ao == null ? 255 : !(ao.value instanceof Number an) ? 0 : PrimitiveParser.resolveNumber(an, 255);
        return new Color(r, g, b, a);
    }

    private SolidConstraint resolveConstraints() {
        THATObject xo = object.getMember("x");
        THATObject yo = object.getMember("y");
        if (xo == null || yo == null) return null;
        if (!(xo.value instanceof Number xn) || !(yo.value instanceof Number yn)) return null;
        TRY_WH:
        {
            THATObject wo = object.getMember("w");
            THATObject ho = object.getMember("h");
            if (wo != null && ho != null)
                if (wo.value instanceof Number wn && ho.value instanceof Number hn)
                    return SolidConstraint.newInstance(SolidConstraint.SOLID_WH, xn, yn, wn, hn);
        }
        TRY_XY:
        {
            // TODO : mak in.mcxiv.thatlang.expression.MultiAssignmentToken parse xyxyx into x, y, x2, y2, x3
            THATObject x2o = object.getMember("x2");
            THATObject y2o = object.getMember("y2");
            if (x2o != null && y2o != null)
                if (x2o.value instanceof Number x2n && y2o.value instanceof Number y2n)
                    return SolidConstraint.newInstance(SolidConstraint.SOLID_XY, xn, yn, x2n, y2n);
        }
        return null;
    }

    public THATObject getObject() {
        return object;
    }

    public Component getComponent() {
        return component;
    }
}
