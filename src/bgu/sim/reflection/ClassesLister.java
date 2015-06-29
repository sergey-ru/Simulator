/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.reflection;

import bgu.sim.core.Routing.DB.AbsRoutingAlgorithm;
import bgu.sim.core.Routing.DB.AbsRoutingDBBinary;
import bgu.sim.core.Routing.DB.AbsRoutingDBSQL;
import bgu.sim.core.stat.StatisticListener;
import bgu.sim.ruleEngine.action.AbsDeviceAction;
import bgu.sim.ruleEngine.action.AbsExternalAction;
import bgu.sim.ruleEngine.action.AbsInitAction;
import bgu.sim.ruleEngine.action.AbsLinkAction;
import bgu.sim.ruleEngine.property.Property;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Sergey
 */
public class ClassesLister {

    private ClassesLister() {
    }

    public static ClassesLister getInstance() {
        return ClassesListerHolder.INSTANCE;
    }

    public List<Class> GetStatisticListeners() {
        List<Class> ans = new LinkedList<>();

        ResolverUtil<StatisticListener> resolver = new ResolverUtil<>();
        resolver.findImplementations(StatisticListener.class, "bgu.sim.core.stat");
        Set<Class<? extends StatisticListener>> classes = resolver.getClasses();

        for (Class<? extends StatisticListener> clazz : classes) {
            if (!clazz.toString().equals(StatisticListener.class.toString())) {
                ans.add(clazz);
            }
        }
        return ans;
    }
    
    public List<Class> GetRoutingAlgorithms() {
        List<Class> ans = new LinkedList<>();

        ResolverUtil<AbsRoutingAlgorithm> resolver = new ResolverUtil<>();
        resolver.findImplementations(AbsRoutingAlgorithm.class, "bgu.sim.core.Routing");
        Set<Class<? extends AbsRoutingAlgorithm>> classes = resolver.getClasses();

        for (Class<? extends AbsRoutingAlgorithm> clazz : classes) {
            if (!clazz.toString().equals(AbsRoutingAlgorithm.class.toString())
                    && !clazz.toString().equals(AbsRoutingDBBinary.class.toString())
                    && !clazz.toString().equals(AbsRoutingDBSQL.class.toString())) {
                ans.add(clazz);
            }
        }
        return ans;
    }
    
    public List<Class> GetInitActions() {
        List<Class> ans = new LinkedList<>();

        ResolverUtil<AbsInitAction> resolver = new ResolverUtil<>();
        resolver.findImplementations(AbsInitAction.class, "bgu.sim.actions");
        Set<Class<? extends AbsInitAction>> classes = resolver.getClasses();

        for (Class<? extends AbsInitAction> clazz : classes) {
            if (!clazz.toString().equals(AbsInitAction.class.toString())) {
                ans.add(clazz);
            }
        }
        return ans;
    }
    
    public List<Class> GetDeviceActions() {
        List<Class> ans = new LinkedList<>();

        ResolverUtil<AbsDeviceAction> resolver = new ResolverUtil<>();
        resolver.findImplementations(AbsDeviceAction.class, "bgu.sim.actions");
        Set<Class<? extends AbsDeviceAction>> classes = resolver.getClasses();

        for (Class<? extends AbsDeviceAction> clazz : classes) {
            if (!clazz.toString().equals(AbsDeviceAction.class.toString())) {
                ans.add(clazz);
            }
        }
        return ans;
    }
    
    public List<Class> GetLinkActions() {
        List<Class> ans = new LinkedList<>();

        ResolverUtil<AbsLinkAction> resolver = new ResolverUtil<>();
        resolver.findImplementations(AbsLinkAction.class, "bgu.sim.actions");
        Set<Class<? extends AbsLinkAction>> classes = resolver.getClasses();

        for (Class<? extends AbsLinkAction> clazz : classes) {
            if (!clazz.toString().equals(AbsLinkAction.class.toString())) {
                ans.add(clazz);
            }
        }
        return ans;
    }
    
    public List<Class> GetExternalActions() {
        List<Class> ans = new LinkedList<>();

        ResolverUtil<AbsExternalAction> resolver = new ResolverUtil<>();
        resolver.findImplementations(AbsExternalAction.class, "bgu.sim.actions");
        Set<Class<? extends AbsExternalAction>> classes = resolver.getClasses();

        for (Class<? extends AbsExternalAction> clazz : classes) {
            if (!clazz.toString().equals(AbsExternalAction.class.toString())) {
                ans.add(clazz);
            }
        }
        return ans;
    }
    
    public List<Property> getClassProperties(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        
        return new LinkedList(Property.lookup(Class.forName(className).newInstance().getClass()).values());
    }

    private static class ClassesListerHolder {

        private static final ClassesLister INSTANCE = new ClassesLister();
    }
}
