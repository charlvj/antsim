/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.charlware.ants.logs;

import com.charlware.ants.World;
import static com.charlware.ants.logs.EventType.*;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author charlvj
 */
public class MetricsLog {
    List<Metric> metrics = new Vector<>();
    final World world;
    
    Metric lastMetric;
    
    public MetricsLog(World world) {
        this.world = world;
        lastMetric = new Metric(0, 0, 0, 0, 0);
    }
    
    public int size() {
        return metrics.size();
    }
    
    public Metric getLastMetric() {
        return lastMetric;
    }
    
    public Metric getMetric(int idx) {
        return metrics.get(idx);
    }
    
    public void add(Event event) {
        if(lastMetric.steps == event.step) {
            metrics.remove(lastMetric);
        }
        Metric metric = new Metric(
                event.step,
                lastMetric.antsBorn + (event.getEventType() == ANT_BORN ? 1 : 0),
                lastMetric.antsDied + (event.getEventType() == ANT_DIED ? 1 : 0),
                lastMetric.antHomesFounded + (event.getEventType() == ANTHOME_FOUNDED ? 1 : 0),
                lastMetric.antHomesEnded + (event.getEventType() == ANTHOME_ENDED ? 1 : 0)
        );
        metrics.add(metric);
        lastMetric = metric;
    }
}
