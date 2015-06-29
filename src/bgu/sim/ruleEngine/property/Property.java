/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import com.esotericsoftware.reflectasm.FieldAccess;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bennyl
 */
public class Property {

    private static final Map<Class, Map<String, Property>> lookup = new HashMap<>();
    private final FieldAccess accessor;
    private final int fieldIndex;
    private final Metadata meta;

    Property(FieldAccess accessor, Field f) {
        this.accessor = accessor;
        this.fieldIndex = accessor.getIndex(f.getName());
        this.meta = Metadata.lookup(f);
    }

    public Metadata getMetadata() {
        return meta;
    }

    public Object getValue(Object source) {
        return accessor.get(source, fieldIndex);
    }

    public void setValue(Object source, Object value) {
        accessor.set(source, fieldIndex, value);
    }

    public void setParsedValue(Object source, String value) {
        setValue(source, ValueParser.parse(value, meta.getType()));
    }

    public static Map<String, Property> lookup(Class cls) {
        Map<String, Property> l = lookup.get(cls);
        
        if (l == null) {
            l = new HashMap<>();
            Field[] fields = cls.getFields();
            FieldAccess accessor = FieldAccess.get(cls);
            
            for (Field field : fields) {
                if (Modifier.isPublic(field.getModifiers())) {
                    final Property p = new Property(accessor, field);
                    l.put(p.getMetadata().getName(), p);
                }
            }

            lookup.put(cls, l);
        }

        return l;
    }

    public static void updateProperties(Object s, List<PropertyDefinition> properties) {
        if (properties.size() > 0) {
            Map<String, Property> props = lookup(s.getClass());
            
            for (PropertyDefinition p : properties) {
                final Property cp = props.get(p.getKey());
                if (cp == null && !s.getClass().toString().contains("GUIListener")) {
                    throw new UnsupportedOperationException("property " + p.getKey() + " not exists and 'moreProperties' is not supported");
                }
                if (p.isComplex()) {
                    try {
                        //need to construct the object..
                        ObjectDefinition complexv = p.getComplexValue();
                        Object complexo = Class.forName(complexv.getObjectClass()).newInstance();
                        updateProperties(complexo, complexv.getProperties());
                        cp.setValue(s, complexo);
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                        throw new RuntimeException("error while trying to construct property value", ex);
                    }

                } else {
                    cp.setParsedValue(s, p.getValue());
                }
            }
        }
    }
}
