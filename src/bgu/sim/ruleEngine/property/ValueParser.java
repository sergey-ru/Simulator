/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bennyl
 */
public class ValueParser {

    private static final Map<Class, Class> wrappers = new HashMap<>();

    static {
        wrappers.put(int.class, Integer.class);
        wrappers.put(long.class, Long.class);
        wrappers.put(double.class, Double.class);
        wrappers.put(byte.class, Byte.class);
        wrappers.put(char.class, Character.class);
        wrappers.put(float.class, Float.class);
        wrappers.put(boolean.class, Boolean.class);
    }

    public static <T> T parse(String s, Class<T> c) {
        if (c == String.class) return (T) s;
        
        try {
            if (c.isPrimitive()) {
                c = wrappers.get(c);
            }
            Method m = c.getMethod("valueOf", String.class);
            if (Modifier.isStatic(m.getModifiers())) {
                try {
                    return (T) m.invoke(null, s);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    //Exceptions.printStackTrace(ex);
                }
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            //Exceptions.printStackTrace(ex);
        }

        return null;
    }
}
