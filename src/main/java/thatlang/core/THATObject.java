package thatlang.core;

import java.util.ArrayList;
import java.util.HashMap;

public class THATObject {

    Class<?> primaryInference;
    ArrayList<Class<?>> secondaryInferences = new ArrayList<>();

    Object value;
    HashMap< /*DATA_KEY*/ String, /*DATA_VALUE*/ Object> objectData = new HashMap<>();

    HashMap< /*NAME*/ String, /*OBJECT*/ THATObject> accessibleMember = new HashMap<>();

}
