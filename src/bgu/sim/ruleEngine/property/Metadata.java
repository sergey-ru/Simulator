/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import java.awt.Image;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bennyl
 */
public class Metadata {

    private static Map<String, Image> iconCache = new HashMap<>();
    private static Map<Object, Metadata> metadataCache = new HashMap<>();
    
    private String name;
    private String info;
    private Image icon;
    private Class type;
    private String defaultValue;

    private Metadata(Class c) {
        this.name = c.getSimpleName();
        this.info = "info not provided";
        this.icon = null;
        this.type = c;
        this.defaultValue = null;

        Meta a = (Meta) c.getAnnotation(Meta.class);
        if (a != null) {
            this.name = !a.name().isEmpty() ? a.name() : name;
            this.info = !a.info().isEmpty() ? a.info() : info;
            //this.icon = getIcon(a.icon());
        }
    }

    private Metadata(Field f) {
        this.name = f.getName();
        this.info = "info not provided";
        this.icon = null;
        this.type = f.getType();

        Meta a = (Meta) f.getAnnotation(Meta.class);
        if (a != null) {
            this.name = !a.name().isEmpty() ? a.name() : name;
            this.info = !a.info().isEmpty() ? a.info() : info;
            //this.icon = getIcon(a.icon());
            this.defaultValue = (a.defaultValue().isEmpty() ? null : a.defaultValue());
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }

//    private Image getIcon(String icon) {
//        if (icon.isEmpty()) {
//            return null;
//        }
//
//        Image res = iconCache.get(icon);
//        if (res == null) {
//            res = ImageUtilities.loadImage(icon);
//            iconCache.put(icon, res);
//        }
//
//        return res;
//    }

    public String getName() {
        return name;
    }

    public Image getIcon() {
        return icon;
    }

    public String getInfo() {
        return info;
    }

    public Class getType() {
        return type;
    }

    public static Metadata lookup(Class c) {
        Metadata res = metadataCache.get(c);
        if (res == null) {
            res = new Metadata(c);
            metadataCache.put(c, res);
        }

        return res;
    }

    public static Metadata lookup(Field f) {
        Metadata res = metadataCache.get(f);
        if (res == null) {
            res = new Metadata(f);
            metadataCache.put(f, res);
        }

        return res;
    }
}
