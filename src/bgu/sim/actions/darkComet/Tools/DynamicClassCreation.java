/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.sim.actions.darkComet.Tools;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keren Fruchter
 */
public class DynamicClassCreation {

    static final String pathOfNewClasses = "src\\bgu\\sim\\actions\\darkComet\\";
    static final String pathOfCSVs = "src\\Resources\\DarkComet\\";

    public static void main(String[] a) {
        createClassesFromCSVnames();
        // test
        try {
            Class aa = Class.forName("bgu.sim.actions.darkComet.StartUp");
            aa.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DynamicClassCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DynamicClassCreation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DynamicClassCreation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createClassesFromCSVnames() {
        File folder = new File(pathOfCSVs);
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("File " + listOfFile.getName());
                String className = listOfFile.getName();
                className = className.replace(".CSV", "");
                className = className.replace("DM_", "");
                createClass(className);
            }
        }
    }

    public static void createClass(String className) {
        String fileName = className + ".java";

        try {
            //Creates DynamicTestClass.java file
            FileWriter fileWriter = new FileWriter(pathOfNewClasses + fileName, false);
            fileWriter.write("package bgu.sim.actions.darkComet; \n\n");
            fileWriter.write("import bgu.sim.core.stat.StatisticCollector;\n"
                    + "import bgu.sim.data.NetDevice;\n"
                    + "import bgu.sim.data.SystemEvent;\n"
                    + "import bgu.sim.ruleEngine.action.AbsDeviceAction;\n"
                    + "import bgu.sim.ruleEngine.action.IndicativeFeature;\n"
                    + "import bgu.sim.actions.darkComet.Tools.*;\n"
                    + "import java.util.List;\n\n");

            fileWriter.write("public class " + className + " extends AbsDeviceAction {\n\n");

            fileWriter.write("    private List<SystemEvent> events; \n\n");

            fileWriter.write("    public List<SystemEvent> getEvents() {\n"
                    + "\treturn events;\n"
                    + "    }\n\n");

            fileWriter.write("    public " + className + "() {\n");

            fileWriter.write("\tsuper(\"" + className + "\", new IndicativeFeature[]{});\n");
            fileWriter.write("\treadCSV rc = new readCSV();\n");
            fileWriter.write("\tevents = rc.readCSV(\"DM_" + className + ".CSV\");\n");
            fileWriter.write("    }\n\n");

            fileWriter.write("    @Override\n"
                    + "    public Integer execute(NetDevice input) {\n"
                    + "\tinput.getInfo().addEvents(events);\n"
                    + "\tStatisticCollector.increaseValue(this.getName(), 1);\n"
                    + "\treturn 1;\n"
                    + "    }\n");
            fileWriter.write("}");

            fileWriter.flush();

            fileWriter.close();

            String[] source = {new String(fileName)};
            //javac -> compile method in Main class ..presents in tools.jar
            //Compile the DynamicTestClass.java ,created on the fly

            com.sun.tools.javac.Main.compile(source);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
