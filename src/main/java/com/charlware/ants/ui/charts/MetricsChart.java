/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui.charts;

import com.charlware.ants.World;
import com.charlware.ants.logs.Event;
import com.charlware.ants.logs.EventLog;
import com.charlware.ants.logs.EventLogListener;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 *
 * @author charlvj
 */
public class MetricsChart extends AntSimChart {
    private final List<Integer> steps = new ArrayList<>();
    private final List<Integer> antHomes = new ArrayList<>();
    private final List<Integer> ants = new ArrayList<>();
    
    public MetricsChart(World world) {
        super(world);
    }
    
    @Override
    public void logEntryAdded(Event event) {
        int lastStep = steps.get(steps.size()-1);
        if(lastStep == event.getStep()) {
            replaceStep(lastStep, event);
        }
        else {
            addStep(event);
        }
        updateChart();
    }
    
    private void replaceStep(int step, Event event) {
        int idx = steps.size()-1;
        int antHomesValue = antHomes.get(idx);
        int antsValue = ants.get(idx);
        switch(event.getEventType()) {
            case ANTHOME_FOUNDED: antHomesValue++; break;
            case ANTHOME_ENDED: antHomesValue--; break;
            case ANT_BORN: antsValue++; break;
            case ANT_DIED: antsValue--; break;
        }
        antHomes.set(idx, antHomesValue);
        ants.set(idx, antsValue);
    }
    
    private void addStep(Event event) {
        int idx = steps.size()-1;
        int antHomesValue = antHomes.get(idx);
        int antsValue = ants.get(idx);
        switch(event.getEventType()) {
            case ANTHOME_FOUNDED: antHomesValue++; break;
            case ANTHOME_ENDED: antHomesValue--; break;
            case ANT_BORN: antsValue++; break;
            case ANT_DIED: antsValue--; break;
        }
        steps.add(event.getStep());
        antHomes.add(antHomesValue);
        ants.add(antsValue);
    }
    
    @Override
    public void setupSeries() {
        // Give it starting values
        steps.add(0);
        antHomes.add(0);
        ants.add(0);
        
        // Series
        chart.addSeries("AntHomes", steps, antHomes);
        chart.addSeries("Ants", steps, ants);
    }
    
    private void updateChart() {
        chart.updateXYSeries("AntHomes", steps, antHomes, null);
        chart.updateXYSeries("Ants", steps, ants, null);
        fireChartListener();
    }
    
    
}
