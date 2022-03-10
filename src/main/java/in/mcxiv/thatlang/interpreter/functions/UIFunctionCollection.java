package in.mcxiv.thatlang.interpreter.functions;

import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.ui.EasyCanvas;
import in.mcxiv.thatlang.ui.components.ShapePanel;
import in.mcxiv.thatlang.ui.components.primitives.ComponentBinder;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;

public class UIFunctionCollection {

    public static THATObject newRect(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        ComponentBinder binder = new ComponentBinder(canvas.add(new ShapePanel(ShapePanel.ShapeType.Rectangle)));
        canvas.addDrawable(binder);
        return binder.getObject();
    }

    public static THATObject newOval(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        ComponentBinder binder = new ComponentBinder(canvas.add(new ShapePanel(ShapePanel.ShapeType.Oval)));
        canvas.addDrawable(binder);
        return binder.getObject();
    }

    public static THATObject newPolygon(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        Double[] doubles = fct.getArguments().getExpressions().stream().map(e -> e.interpret(vm)).filter(object -> object.value instanceof Number).map(object -> ((Number) object.value).doubleValue()).toArray(Double[]::new);
        Path2D.Double path = new Path2D.Double();
        path.moveTo(doubles[0], doubles[1]);
        for (int i = 0; i + 1 < doubles.length; i += 2)
            path.lineTo(doubles[i], doubles[i + 1]);
        path.closePath();
        ComponentBinder binder = new ComponentBinder(canvas.add(new ShapePanel(ShapePanel.ShapeType.Polygon, path)));
        canvas.addDrawable(binder);
        return binder.getObject();
    }

    public static THATObject newButton(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {

        ExpressionsToken[] args = fct.getArguments().resolve("text", "onclick");

        String text /*   */ = args[0] == null ? "Button" : args[0].interpret(vm).v();
        ActionListener onCk = args[1] == null ? null/**/ : e -> args[1].interpret(vm);

        JButton component = new JButton(text);
        component.addActionListener(onCk);

        return binderize(canvas, component);
    }

    public static THATObject newTextField(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        // TODO: Add a bind parameter which makes a function such that, when called, returns the content
        ExpressionsToken[] args = fct.getArguments().resolve("text");
        String text = args[0] == null ? "..." : args[0].interpret(vm).v();
        JTextField component = new JTextField(text);
        return binderize(canvas, component);
    }

    public static THATObject newCheckBox(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        ExpressionsToken[] args = fct.getArguments().resolve("text", "onclick");

        String text /*   */ = args[0] == null ? "Check Box" : args[0].interpret(vm).v();
        ActionListener onCk = args[1] == null ? null /*  */ : e -> args[1].interpret(vm);

        JCheckBox component = new JCheckBox(text);
        component.addActionListener(onCk);

        return binderize(canvas, component);
    }

    public static THATObject newRadioButton(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        ExpressionsToken[] args = fct.getArguments().resolve("text", "onclick");

        String text /*   */ = args[0] == null ? "Radio Button" : args[0].interpret(vm).v();
        ActionListener onCk = args[1] == null ? null /*     */ : e -> args[1].interpret(vm);

        JRadioButton component = new JRadioButton(text);
        component.addActionListener(onCk);

        return binderize(canvas, component);
    }

    public static THATObject newButtonGroup(AbstractVM vm, FunctionCallToken fct, EasyCanvas canvas) {
        ExpressionsToken[] args = fct.getArguments().getArgs();

        ButtonGroup component = new ButtonGroup();
        for (ExpressionsToken arg : args) {
            Object value = arg.interpret(vm).value;
            if (value instanceof ComponentBinder cb)
                if (cb.getComponent() instanceof JRadioButton jrb)
                    component.add(jrb);
        }

        return THOSEObjects.createValue(component);
    }

    private static THATObject binderize(EasyCanvas canvas, JComponent component) {
        ComponentBinder binder = new ComponentBinder(component);
        canvas.add(component);
        canvas.addDrawable(binder);
        return binder.getObject();
    }
}
