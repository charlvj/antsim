/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui;

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
public class MetricsChart implements EventLogListener {
    private final EventLog eventLog;
    
    private List<Integer> steps = new ArrayList<>();
    private List<Integer> antHomes = new ArrayList<>();
    private List<Integer> ants = new ArrayList<>();
    
    private XYChart chart;
    
    private List<MetricsChartListener> listeners = new ArrayList<>();
    
    public MetricsChart(World world) {
        this.eventLog = world.getEventLog();
        this.eventLog.addListener(this);
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
    
    public XYChart createChart() {
        chart = new XYChartBuilder().width(600).height(400).title("Area Chart").xAxisTitle("Steps").yAxisTitle("Count").build();

        // Customize Chart
        chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);

        // Give it starting values
        steps.add(0);
        antHomes.add(0);
        ants.add(0);
        
        // Series
        chart.addSeries("AntHomes", steps, antHomes);
        chart.addSeries("Ants", steps, ants);
        
        return chart;
    }
    
    private void updateChart() {
        chart.updateXYSeries("AntHomes", steps, antHomes, null);
        chart.updateXYSeries("Ants", steps, ants, null);
        listeners.forEach(listener -> listener.chartUpdated(chart));
    }
    
    public void addChartListener(MetricsChartListener listener) {
        listeners.add(listener);
    }
}

interface MetricsChartListener  {
    public void chartUpdated(XYChart chart);
}