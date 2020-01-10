/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.ui.charts;

import com.charlware.ants.AntHome;
import com.charlware.ants.World;
import com.charlware.ants.logs.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author charlvj
 */
public class AntHomesChart extends AntSimChart {

    private final List<Integer> steps = new ArrayList<>();
    private final Map<Integer, List<Integer>> populations = new HashMap<>();
    
    public AntHomesChart(World world) {
        super(world);
    }
    
    @Override
    protected void setupSeries() {
        // Give it starting values
        steps.add(0);
        createListFor(1);
        
        
        // Series
        chart.addSeries("Home #1", steps, populations.get(1));
    }
    
    @Override
    public void logEntryAdded(final Event event) {
        final int antHomeId = event.getAntHome().getId();
        
        switch(event.getEventType()) {
            case ANTHOME_FOUNDED:
                createListFor(antHomeId);
                break;
            case ANTHOME_ENDED:
                // Just stop tracking it
                populations.remove(antHomeId);
                break;
            case ANT_BORN:
            case ANT_DIED:
                List<Integer> population = populations.get(antHomeId);
                int lastStepIndex = steps.size()-1;
                int lastStep = steps.get(lastStepIndex);
                if(lastStep == event.getStep()) {
                    population.set(lastStepIndex, population.get(lastStepIndex) + event.getValue());
                }
                else {
                    populations.forEach((id, antHomePop) -> {
                        if(id == antHomeId) {
                            antHomePop.add(antHomePop.get(lastStepIndex) + event.getValue());
                        }
                        else {
                            antHomePop.add(antHomePop.get(lastStepIndex));
                        }
                    });
                    steps.add(event.getStep());
                }
        }
        
        updateChart();
    }

    private List<Integer> createListFor(int antHomeId) {
        final List<Integer> l = new ArrayList<>();
        for(Integer step: steps) {
            l.add(0);
        }
        populations.put(antHomeId, l);
        return l;
    }
    
    private void updateChart() {
        populations.forEach((antHomeId, antHomePopulation) -> {
            final String key = "Home #" + antHomeId;
            if(!chart.getSeriesMap().containsKey(key)) {
                chart.addSeries(key, steps, antHomePopulation);
            }
            else {
                chart.updateXYSeries("Home #" + antHomeId, steps, antHomePopulation, null);
            }
        });
        fireChartListener();
    }
}
