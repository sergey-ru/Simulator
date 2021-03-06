/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.ruleEngine.property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author bennyl
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Meta {
    String name() default "";
    String info() default "";
    String icon() default "";    
    String defaultValue() default "";
}
