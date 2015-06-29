/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.sim.data;

import java.util.List;

/**
 *
 * @author Keren Fruchter
 */
public class StatisticsDataStruct {
    public List newList;
    public int ScenarioNumber;
    
    public StatisticsDataStruct(List newList, int ScenarioNumber) {
        this.newList = newList;
        this.ScenarioNumber = ScenarioNumber;
    }
}
